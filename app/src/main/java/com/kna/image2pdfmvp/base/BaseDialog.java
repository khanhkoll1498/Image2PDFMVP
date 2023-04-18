package com.kna.image2pdfmvp.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

import com.kna.image2pdfmvp.mvp.view.OnActionCallback;

public abstract class BaseDialog<T extends ViewDataBinding> extends DialogFragment {
    protected T binding;

    protected OnActionCallback callback;

    public void setCallback(OnActionCallback callback) {
        this.callback = callback;
    }

    public void showDialog(BaseActivity activity) {
        show(activity.getSupportFragmentManager(), null);
    }

    public void showDialog(BaseActivity activity, OnActionCallback callback) {
        setCallback(callback);
        show(activity.getSupportFragmentManager(), null);
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        addEvent();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void addEvent();


}
