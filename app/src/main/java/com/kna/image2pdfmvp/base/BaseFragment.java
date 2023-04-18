package com.kna.image2pdfmvp.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.kna.image2pdfmvp.mvp.view.OnActionCallback;


abstract public class BaseFragment<T extends ViewDataBinding, P extends BasePresenter> extends Fragment implements View.OnClickListener {
    protected Context context;
    protected T binding;
    protected P presenter;
    protected OnActionCallback callback;

    public void setCallback(OnActionCallback callback) {
        this.callback = callback;
    }

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
        initPresenter();
        initView();
        addEvent();
    }

    protected abstract void initPresenter();

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void addEvent();

    @Override
    public void onClick(View v) {
    }

}
