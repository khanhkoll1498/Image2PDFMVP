package com.master.prolibs.interfaces;

public interface PermissionCallback {
    void onPermissionGranted();

    void onPermissionDenied();

    void onPressDenied();

    void onPressGrant();
}
