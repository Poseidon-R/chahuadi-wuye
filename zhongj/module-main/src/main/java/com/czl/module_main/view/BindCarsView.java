package com.czl.module_main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.czl.base.data.bean.CarItem;
import com.czl.base.view.ViewBase;
import com.czl.module_main.R;
import com.czl.module_main.adapter.BindCarAdapter;
import com.czl.module_main.listener.DeleteCarListener;
import com.lxj.xpopup.widget.VerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2021/8/25
 * Do:已绑定车牌号
 */
public class BindCarsView extends ViewBase {
    private BindCarAdapter bindCarAdapter;
    private List<CarItem> list;
    private VerticalRecyclerView rv;
    private DeleteCarListener deleteCarListener;

    public BindCarsView(Context context) {
        super(context);
    }

    public BindCarsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BindCarsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_bind_cars;
    }

    @Override
    public void initData() {
        rv = findViewById(R.id.rv);
        list = new ArrayList<>();
        bindCarAdapter = new BindCarAdapter();
        rv.setAdapter(bindCarAdapter);
        bindCarAdapter.setEmptyView(R.layout.car_empty_layout);
        bindCarAdapter.setList(list);
        bindCarAdapter.setOnItemClickListener((adapter, view, position) -> {
            CarItem carItem = (CarItem) adapter.getItem(position);
            if (deleteCarListener != null)
                deleteCarListener.scanCar(carItem.getVehicleNo());
        });
    }

    public void setList(List<CarItem> list) {
        bindCarAdapter.setList(list);
    }

    public void setDeleteListener(DeleteCarListener deleteCarListener) {
        this.deleteCarListener = deleteCarListener;
        bindCarAdapter.setDeleteCarListener(deleteCarListener);
    }
}
