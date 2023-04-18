package com.master.prolibs;

import android.app.Application;

import com.master.prolibs.manager.AdmobManager;
import com.master.prolibs.manager.AppOpenManager;
import com.master.prolibs.manager.PurchaseManager;
import com.master.prolibs.model.PurchaseModel;
import com.master.prolibs.utils.AppUtils;
import com.master.prolibs.utils.SharePrefUtils;

import java.util.List;


public abstract class MyApplication extends Application {

    @Override
    public final void onCreate() {
        super.onCreate();
        onApplicationCreate();

        SharePrefUtils.getInstance().init(this);
        AdmobManager.getInstance().init(this, isShowAdsTest() ? AdmobManager.getInstance().getDeviceId(this) : "");
        AdmobManager.getInstance().hasAds(hasAds());
        if (enableAdsResume()) {
            AppOpenManager.getInstance().init(this, getOpenAppAdId());
        }
        AdmobManager.getInstance().setShowLoadingDialog(isShowDialogLoadingAd());

        AppConfig appConfig = getAppConfig();
        AppUtils.getInstance().setAppConfig(appConfig);
        AdmobManager.getInstance().setHasLog(appConfig.isShowLogIdAd());

        if (hasPurchase()) {
            PurchaseManager.getInstance().init(this, getPurchaseList());
        }

    }


    protected abstract void onApplicationCreate();

    protected abstract boolean hasAds();

    protected abstract boolean isShowDialogLoadingAd();

    protected abstract boolean isShowAdsTest();

    protected abstract boolean enableAdsResume();

    protected abstract String getOpenAppAdId();

    protected abstract boolean hasPurchase();

    protected abstract List<PurchaseModel> getPurchaseList();

    protected abstract AppConfig getAppConfig();
}
