package com.kna.image2pdfmvp.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;

public abstract class BaseActivity<T extends ViewDataBinding, P extends BasePresenter> extends LocalizationActivity {
    protected T binding;
    protected P presenter;

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding.executePendingBindings();
        initPresenter();
        initView();
        addEvent();
    }

    protected abstract void initPresenter();

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void addEvent();

}
