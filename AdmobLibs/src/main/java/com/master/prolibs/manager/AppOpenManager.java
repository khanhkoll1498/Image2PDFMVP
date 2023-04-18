package com.master.prolibs.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.master.prolibs.dialog.WelcomeBackDialog;
import com.master.prolibs.interfaces.AdCallback;
import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String TAG = "AppOpenManager";

    @SuppressLint("StaticFieldLeak")
    private static volatile AppOpenManager INSTANCE;
    private AppOpenAd appResumeAd = null;

    private String appResumeAdId;

    private Activity currentActivity;

    private Application myApplication;

    private boolean isShowingAd = false;

    private boolean isInitialized = false;
    private boolean isAppResumeEnabled = true;

    private final List<Class> disabledAppOpenList;

    private long loadTime;
    private WelcomeBackDialog dialog;
    private long timeShowLoading = 100;

    public void setTimeShowLoading(long timeShowLoading) {
        this.timeShowLoading = timeShowLoading;
    }

    public AppOpenAd getAppResumeAd() {
        return appResumeAd;
    }

    public String getAppResumeAdId() {
        return appResumeAdId;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public Application getMyApplication() {
        return myApplication;
    }

    public boolean isShowingOpenAd() {
        return isShowingAd;
    }

    public boolean isAppResumeEnabled() {
        return isAppResumeEnabled;
    }

    public List<Class> getDisabledAppOpenList() {
        return disabledAppOpenList;
    }

    public long getLoadTime() {
        return loadTime;
    }

    private AppOpenManager() {
        disabledAppOpenList = new ArrayList<>();
    }


    public static synchronized AppOpenManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppOpenManager();
        }
        return INSTANCE;
    }

    public void init(Application application, String appOpenAdId) {
        isInitialized = true;
        this.myApplication = application;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        this.appResumeAdId = appOpenAdId;
    }

    public boolean isInitialized() {
        return isInitialized;
    }


    public void disableAppResumeWithActivity(Class activityClass) {
        Log.d(TAG, "disableAppResumeWithActivity: " + activityClass.getName());
        disabledAppOpenList.add(activityClass);
    }

    public void enableAppResumeWithActivity(Class activityClass) {
        Log.d(TAG, "enableAppResumeWithActivity: " + activityClass.getName());
        disabledAppOpenList.remove(activityClass);
    }

    public void disableAppResume() {
        isAppResumeEnabled = false;
    }

    public void enableAppResume() {
        isAppResumeEnabled = true;
    }


    public void fetchAd() {
        fetchAd(null);
    }

    public void fetchAd(AdCallback callback) {
        if (isAdAvailable()) {
            if (callback != null) callback.onAdLoaded();
            return;
        }
        AdRequest request = AdmobManager.getInstance().getAdRequest();
        if (request == null) {
            return;
        }

        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                AppOpenManager.this.appResumeAd = ad;
                AppOpenManager.this.loadTime = (new Date()).getTime();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                AppOpenManager.this.appResumeAd = null;
            }
        };
        AdmobManager.getInstance().log("Request OpenAd :" + appResumeAdId);
        AppOpenAd.load(myApplication, appResumeAdId, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    public boolean isAdAvailable() {
        return appResumeAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
        fetchAd();
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }


    public void showAdIfAvailable() {
        if (currentActivity != null && !AdmobManager.getInstance().isHasAds()) {
            return;
        }

        Log.d(TAG, "showAdIfAvailable: " + ProcessLifecycleOwner.get().getLifecycle().getCurrentState());
        if (!ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            Log.d(TAG, "showAdIfAvailable: return");
            return;
        }
        if (isAdAvailable()) {
            Log.d(TAG, "Will show ad.");
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Set the reference to null so isAdAvailable() returns false.
                    AppOpenManager.this.appResumeAd = null;
                    isShowingAd = false;
//                            fetchAd();
                    dismissDialogLoading();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    dismissDialogLoading();
                    AppOpenManager.this.appResumeAd = null;
                    fetchAd();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    isShowingAd = true;
                }
            };
            showAdsWithLoading(fullScreenContentCallback);
        }
    }

    private void dismissDialogLoading() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAdsWithLoading(FullScreenContentCallback fullScreenContentCallback) {
        if (isShowingAd || appResumeAd == null) {
            return;
        }
        if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            try {
                dismissDialogLoading();
                dialog = new WelcomeBackDialog(currentActivity);
                dialog.show();
            } catch (Exception e) {
                dialog = null;
                e.printStackTrace();
            }
            final Dialog finalDialog = dialog;

            if (fullScreenContentCallback != null) {
                appResumeAd.setFullScreenContentCallback(fullScreenContentCallback);
            }

            new Handler().postDelayed(() -> {
                if (dialog != null && dialog.isShowing()) {
                    AdmobManager.getInstance().log("Show OpenAd :" + appResumeAdId);
                    appResumeAd.show(currentActivity);
                }
            }, timeShowLoading);
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onResume() {
        if (!isAppResumeEnabled /*|| !AdsConfigManager.getInstance().hasAds(R.string.oa_return)*/) {
            Log.d(TAG, "onResume: app resume is disabled");
            return;
        }

        for (Class activity : disabledAppOpenList) {
            if (activity.getName().equals(currentActivity.getClass().getName())) {
                Log.d(TAG, "onStart: activity is disabled");
                return;
            }
        }

        if (!currentActivity.getClass().getName().equals(AdActivity.class.getName())) {
            showAdIfAvailable();
        }
    }
}

