package com.kna.image2pdfmvp.base;

public abstract class BasePresenter<V extends BaseView> {
    protected V mView;

    public BasePresenter(V mView) {
        this.mView = mView;
    }
}
