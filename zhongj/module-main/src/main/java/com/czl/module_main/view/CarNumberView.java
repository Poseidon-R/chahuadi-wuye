package com.czl.module_main.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.czl.base.config.AppConstants;
import com.czl.base.route.RouteCenter;
import com.czl.base.view.CarBoardView;
import com.czl.base.view.ViewBase;
import com.czl.module_main.R;
import com.czl.module_main.adapter.BindCarAdapter;
import com.czl.module_main.listener.BindCarListener;

import androidx.annotation.Nullable;

/**
 * Author:xch
 * Date:2021/8/25
 * Do:
 */
public class CarNumberView extends ViewBase {
    private TextView queryBtn;
    private CarBoardView carBoardView;
    private String carNumber = null;
    private BindCarListener bindCarListener;

    public CarNumberView(Context context) {
        super(context);
    }

    public CarNumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CarNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_car_number;
    }

    @Override
    public void initData() {
        queryBtn = findViewById(R.id.btn_query);
        carBoardView = findViewById(R.id.view_car_board);
        carBoardView.setUpdateCarNumListener(number -> {
            carNumber = number;
        });

        queryBtn.setOnClickListener(v -> {
            if (bindCarListener != null) {
                bindCarListener.scanCarDetail(carNumber);
                carNumber="";
                carBoardView.clearNum();
            }
        });
    }
    //处理按返回建隐藏车牌号键盘
    public boolean keyboardDismiss(){
        if (null==carBoardView){
            return false;
        }
        return carBoardView.keyboardDismiss();
    }

    public void setBindCarListener(BindCarListener listener) {
        this.bindCarListener = listener;
    }
}
