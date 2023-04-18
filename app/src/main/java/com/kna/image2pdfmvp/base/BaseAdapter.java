package com.kna.image2pdfmvp.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kna.image2pdfmvp.mvp.view.OnActionCallback;

import java.util.List;

abstract public class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected OnActionCallback mCallback;
    protected List<T> mList;
    protected Context context;
    private int count = -1;

    public BaseAdapter(List<T> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCallback(OnActionCallback mCallback) {
        this.mCallback = mCallback;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewHolder(parent, viewType);
    }

    protected abstract RecyclerView.ViewHolder viewHolder(ViewGroup parent, int viewType);


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        onBindView(viewHolder, position);
    }

    protected abstract void onBindView(RecyclerView.ViewHolder viewHolder, int position);

    @Override
    public int getItemCount() {
        return (count >= 0 && count <= mList.size()) ? count : mList.size();
    }


}
