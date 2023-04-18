package com.kna.image2pdfmvp.base;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


abstract public class BaseViewHolder<T extends ViewDataBinding, O> extends RecyclerView.ViewHolder {
    protected T binding;

    public BaseViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public abstract void onBind(O data);
}
