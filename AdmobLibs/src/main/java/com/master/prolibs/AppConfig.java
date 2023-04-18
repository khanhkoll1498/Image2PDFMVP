package com.master.prolibs;

public class AppConfig {
    private String policyUrl;
    private String emailSupport;
    private String subjectSupport;
    private String subjectShare;
    private boolean showLogIdAd;

    public void setShowLogIdAd(boolean showLogIdAd) {
        this.showLogIdAd = showLogIdAd;
    }

    public boolean isShowLogIdAd() {
        return showLogIdAd;
    }

    public String getPolicyUrl() {
        return policyUrl;
    }

    public String getEmailSupport() {
        return emailSupport;
    }

    public String getSubjectSupport() {
        return subjectSupport;
    }

    public String getSubjectShare() {
        return subjectShare;
    }

    public void setPolicyUrl(String policyUrl) {
        this.policyUrl = policyUrl;
    }

    public void setEmailSupport(String emailSupport) {
        this.emailSupport = emailSupport;
    }

    public void setSubjectSupport(String subjectSupport) {
        this.subjectSupport = subjectSupport;
    }

    public void setSubjectShare(String subjectShare) {
        this.subjectShare = subjectShare;
    }

    public static class AppConfigBuilder {
        private String policyUrl;
        private String emailSupport;
        private String subjectSupport;
        private String subjectShare;
        private boolean showLogIdAd;

        public AppConfigBuilder setShowLogIdAd(boolean showLogIdAd) {
            this.showLogIdAd = showLogIdAd;
            return this;
        }

        public AppConfigBuilder setPolicyUrl(String policyUrl) {
            this.policyUrl = policyUrl;
            return this;
        }

        public AppConfigBuilder setEmailSupport(String emailSupport) {
            this.emailSupport = emailSupport;
            return this;
        }

        public AppConfigBuilder setSubjectSupport(String subjectSupport) {
            this.subjectSupport = subjectSupport;
            return this;
        }

        public AppConfigBuilder setSubjectShare(String subjectShare) {
            this.subjectShare = subjectShare;
            return this;
        }

        public AppConfig build() {
            AppConfig appConfig = new AppConfig();
            appConfig.setEmailSupport(emailSupport);
            appConfig.setPolicyUrl(policyUrl);
            appConfig.setSubjectSupport(subjectSupport);
            appConfig.setSubjectShare(subjectShare);
            appConfig.setShowLogIdAd(showLogIdAd);
            return appConfig;
        }
    }

}
