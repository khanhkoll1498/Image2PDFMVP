package com.master.prolibs.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.master.prolibs.R;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.master.prolibs.dialog.PrepareLoadingAdsDialog;
import com.master.prolibs.interfaces.AdCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

public class AdmobManager {
    private static AdmobManager instance;
    private boolean hasAds = true;
    private final LoadAdError errAd = new LoadAdError(2, "No Ad", "", null, null);
    private boolean isShowLoadingDialog;
    private long customTimeLoadingDialog = 1500;
    private boolean hasAdjust;
    private boolean hasLog;

    public LoadAdError getErrAd() {
        return errAd;
    }

    public boolean isShowLoadingDialog() {
        return isShowLoadingDialog;
    }

    public void setHasAds(boolean hasAds) {
        this.hasAds = hasAds;
    }

    public void setHasAdjust(boolean hasAdjust) {
        this.hasAdjust = hasAdjust;
    }

    public boolean isHasAds() {
        return hasAds;
    }

    public void setHasLog(boolean hasLog) {
        this.hasLog = hasLog;
    }

    public boolean isHasAdjust() {
        return hasAdjust;
    }

    public boolean isHasLog() {
        return hasLog;
    }

    public void setCustomTimeLoadingDialog(long customTimeLoadingDialog) {
        this.customTimeLoadingDialog = customTimeLoadingDialog;
    }

    public long getCustomTimeLoadingDialog() {
        return customTimeLoadingDialog;
    }

    public void setShowLoadingDialog(boolean showLoadingDialog) {
        isShowLoadingDialog = showLoadingDialog;
    }

    public static AdmobManager getInstance() {
        if (instance == null) {
            instance = new AdmobManager();
        }
        return instance;
    }

    private AdmobManager() {

    }

    public void init(Context context, String deviceID) {
        try {
            Log.d("log_admob", "init: Admob");
            MobileAds.initialize(context, initializationStatus -> {
            });
            MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Collections.singletonList(deviceID)).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public AdRequest getAdRequest() {
        if (!hasAds || PurchaseManager.getInstance().isPurchased()) {
            return null;
        }
        AdRequest.Builder builder = new AdRequest.Builder();
        return builder.build();
    }

    /*CollapsibleType.TOP
     * CollapsibleType.BOTTOM*/
    public AdRequest getAdRequest(@NonNull String collapsibleType) {
        if (!hasAds || PurchaseManager.getInstance().isPurchased()) {
            return null;
        }
        AdRequest.Builder builder = new AdRequest.Builder();
        Bundle extras = new Bundle();
        extras.putString("collapsible", collapsibleType);

        builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
        return builder.build();
    }

    public void loadInterAds(Activity context, String id, AdCallback callback) {
        log("request inter: " + id + "");
        AdRequest request = getAdRequest();
        if (request == null) {
            callback.onAdFailedToLoad(errAd);
            return;
        }
        InterstitialAd.load(context, id, request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (callback != null) {
                    callback.onAdFailedToLoad(loadAdError);
                }
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                if (callback != null) {
                    callback.onResultInterstitialAd(interstitialAd);
                }
            }
        });
    }

    public void log(String s) {
        if (hasLog) {
            Log.d("android_log", "log: " + s);
        }
    }

    public void showSplashOpenApp(Context context, String id, AdCallback callback) {
        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd appResumeAd) {
                appResumeAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        if (callback != null) {
                            callback.onNextScreen();
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        if (callback != null) {
                            callback.onNextScreen();

                        }
                    }
                });
                appResumeAd.show((Activity) context);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                if (callback != null) {
                    callback.onNextScreen();
                }
            }
        };

        AdRequest request = AdmobManager.getInstance().getAdRequest();
        AppOpenAd.load(context, id, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    public void showInterstitial(Activity context, final InterstitialAd mInterstitialAd, AdCallback callback) {

        if (mInterstitialAd == null || PurchaseManager.getInstance().isPurchased()) {
            if (callback != null) {
                callback.onNextScreen();
            }
            return;
        }


        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                context.sendBroadcast(new Intent(PrepareLoadingAdsDialog.ACTION_DISMISS_DIALOG));
                if (AppOpenManager.getInstance().isInitialized()) {
                    AppOpenManager.getInstance().enableAppResume();
                }
                if (callback != null) {
                    callback.onNextScreen();
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                if (AppOpenManager.getInstance().isInitialized()) {
                    AppOpenManager.getInstance().enableAppResume();
                }
                context.sendBroadcast(new Intent(PrepareLoadingAdsDialog.ACTION_DISMISS_DIALOG));
                if (callback != null) {
                    callback.onNextScreen();
                }
            }

            @Override
            public void onAdShowedFullScreenContent() {
                context.sendBroadcast(new Intent(PrepareLoadingAdsDialog.ACTION_DISMISS_DIALOG));
                if (callback != null) {
                    callback.onAdShowedFullScreenContent();
                }
            }
        });

        if (context != null && !context.isDestroyed() && ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            long timeShowLoadingDlg = 0;
            if (isShowLoadingDialog) {
                PrepareLoadingAdsDialog.start(context);
                timeShowLoadingDlg = customTimeLoadingDialog;
            }
            if (AppOpenManager.getInstance().isInitialized()) {
                AppOpenManager.getInstance().disableAppResume();
            }
            new Handler().postDelayed(() -> {
                log("show inter: " + mInterstitialAd.getAdUnitId());
                mInterstitialAd.show(context);
            }, timeShowLoadingDlg);
        } else {
            if (callback != null) {
                callback.onNextScreen();
            }
        }
    }


    public void loadBanner(final Activity mActivity, String id, final FrameLayout adContainer) {
        loadBanner(mActivity, id, adContainer, null);
    }

    public void loadBanner(final Activity mActivity, String id, final FrameLayout adContainer, String collapsibleType) {
        log("Request Banner :" + id);

        AdRequest request = collapsibleType == null ? getAdRequest() : getAdRequest(collapsibleType);
        if (request == null) {
            adContainer.removeAllViews();
            adContainer.setVisibility(View.GONE);
            return;
        }
        try {
            AdView adView = new AdView(mActivity);
            adView.setAdUnitId(id);
            AdSize adSize = getAdSize(mActivity);
            adView.setAdSize(adSize);
            adView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            adView.loadAd(request);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    adContainer.removeAllViews();
                    adContainer.setVisibility(View.GONE);
                }


                @Override
                public void onAdLoaded() {
                    adContainer.removeAllViews();
                    adContainer.setVisibility(View.VISIBLE);
                    adContainer.addView(adView);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private AdSize getAdSize(Activity mActivity) {
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth);

    }

    @Deprecated
    public void loadNative(Context context, String id, FrameLayout placeHolder) {
        loadNative(context, id, placeHolder, R.layout.custom_native);
    }

    public void loadNative(Context context, String id, FrameLayout placeHolder, int customNative) {
        log("Request NativeAd :" + id);
        loadUnifiedNativeAd(context, id, new AdCallback() {
            @Override
            public void onNativeAds(NativeAd nativeAd) {
                @SuppressLint("InflateParams") NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(context).inflate(customNative, null);
                onBindAdView(nativeAd, nativeAdView);
                placeHolder.removeAllViews();
                placeHolder.addView(nativeAdView);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError i) {
                placeHolder.removeAllViews();
                placeHolder.setVisibility(View.GONE);
            }
        });
    }

    private void loadUnifiedNativeAd(Context context, String id, final AdCallback callback) {
        AdRequest request = getAdRequest();
        if (request == null) {
            callback.onAdFailedToLoad(errAd);
            return;
        }
        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        AdLoader adLoader = new AdLoader.Builder(context, id).forNativeAd(nativeAd -> {
            if (callback != null) {
                callback.onNativeAds(nativeAd);
            }
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (callback != null) {
                    callback.onAdFailedToLoad(loadAdError);
                }
            }
        }).withNativeAdOptions(adOptions).build();
        adLoader.loadAd(request);
    }

    private void onBindAdView(NativeAd nativeAd, NativeAdView adView) {
        try {
            adView.setMediaView(adView.findViewById(R.id.ad_media));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.INVISIBLE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.INVISIBLE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            if (nativeAd.getPrice() == null) {
//                adView.getPriceView().setVisibility(View.INVISIBLE);
//            } else {
//                adView.getPriceView().setVisibility(View.VISIBLE);
//                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            if (nativeAd.getStore() == null) {
//                adView.getStoreView().setVisibility(View.INVISIBLE);
//            } else {
//                adView.getStoreView().setVisibility(View.VISIBLE);
//                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.GONE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adView.setNativeAd(nativeAd);

    }

    public void loadRewardAd(Context context, String id, RewardedAdLoadCallback adLoadCallback) {
        log("Request RewardAd :" + id);

        AdRequest request = getAdRequest();
        if (request == null) {
            adLoadCallback.onAdFailedToLoad(errAd);
            return;
        }

        RewardedAd.load(context, id, request, adLoadCallback);
    }

    public void showRewardAd(Activity activity, RewardedAd rewardedAd, AdCallback callback) {
        if (!hasAds || rewardedAd == null || PurchaseManager.getInstance().isPurchased()) {
            callback.onAdFailedToShowFullScreenContent(errAd);
            return;
        }
        log("Show RewardAd :" + rewardedAd.getAdUnitId());

        rewardedAd.show(activity, callback::onUserEarnedReward);
    }

    @SuppressLint("HardwareIds")
    public String getDeviceId(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return md5(android_id).toUpperCase();
    }

    private String md5(final String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuilder hexString;
            hexString = new StringBuilder();
            for (byte b : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & b));
                while (h.length() < 2) h.insert(0, "0");
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException ignored) {
        }
        return "";
    }

    public void hasAds(boolean hasAds) {
        this.hasAds = hasAds;
    }

    public void hasAdjust(boolean b) {
        this.hasAdjust = b;
    }
}
