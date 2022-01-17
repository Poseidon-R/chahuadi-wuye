package com.czl.module_main.view;

import android.content.Context;
import android.util.AttributeSet;

import com.czl.base.view.ViewBase;
import com.czl.module_main.R;

import androidx.annotation.Nullable;

/**
 * Author:xch
 * Date:2021/8/25
 * Do:
 */
public class ImgTopView extends ViewBase {
    public ImgTopView(Context context) {
        super(context);
    }

    public ImgTopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImgTopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_img_top;
    }

    @Override
    public void initData() {

    }
}
