package com.czl.base.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Author:xch
 * Date:2021/8/25
 * Do:
 */
public abstract class ViewBase extends LinearLayout {

    public Context mContext;
    private View root;

    public ViewBase(Context context) {
        super(context);
        initView(context);
    }

    public ViewBase(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ViewBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    void initView(Context ctx) {
        this.mContext = ctx;
        root = LayoutInflater.from(getContext()).inflate(getViewId(), this);
//        DataBindingUtil.bind(root);
        initData();
    }

    public abstract int getViewId();

    public abstract void initData();

    public Activity getActivity() {
        return (Activity) getContext();
    }

    public View getRoot() {
        return root;
    }

    public ViewDataBinding getBind(){
        return DataBindingUtil.getBinding(root);
    }

}

