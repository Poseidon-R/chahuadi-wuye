package com.czl.module_main.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import com.alibaba.android.arouter.launcher.ARouter;
import com.czl.base.config.AppConstants;
import com.czl.base.mvvm.ui.ContainerFmActivity;
import com.czl.base.view.ViewBase;
import com.czl.module_main.R;
import com.czl.module_main.adapter.OtherFeatureAdapter;
import com.czl.module_main.bean.OtherFeatureBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author:xch
 * Date:2021/8/25
 * Do:其它功能
 */
public class OtherFeatureView extends ViewBase {
    private RecyclerView rv;
    private OtherFeatureAdapter otherFeatureAdapter;
    private List<OtherFeatureBean> list;

    public OtherFeatureView(Context context) {
        super(context);
    }

    public OtherFeatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OtherFeatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_other_feature;
    }

    @Override
    public void initData() {
        rv = findViewById(R.id.rv);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        rv.setLayoutManager(layoutManager);
        otherFeatureAdapter = new OtherFeatureAdapter();
        rv.setAdapter(otherFeatureAdapter);
        list = new ArrayList<>();
        list.add(new OtherFeatureBean(R.mipmap.ic_yk, "月卡"));
//        list.add(new OtherFeatureBean(R.mipmap.ic_yhq, "优惠券"));
//        list.add(new OtherFeatureBean(R.mipmap.ic_tcfp, "停车发票"));
        list.add(new OtherFeatureBean(R.mipmap.ic_jfjl, "缴费记录"));
//        list.add(new OtherFeatureBean(R.mipmap.ic_fw, "服务"));
        otherFeatureAdapter.setNewData(list);

        otherFeatureAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (position){
                case 0:
                    Intent intent = new Intent(mContext, ContainerFmActivity.class);
                    intent.putExtra(ContainerFmActivity.FRAGMENT, AppConstants.Router.Park.F_CAR_LIST);
                    mContext.startActivity(intent);
                    break;
                case 1:
                    Intent intent1 = new Intent(mContext, ContainerFmActivity.class);
                    intent1.putExtra(ContainerFmActivity.FRAGMENT, AppConstants.Router.Park.F_PAY_RECORDS);
                    mContext.startActivity(intent1);
                    break;
            }
        });
    }
}
