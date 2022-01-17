package com.czl.module_main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.czl.module_main.R;
import com.czl.module_main.bean.OtherFeatureBean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author:xch
 * Date:2021/8/26
 * Do:
 */
public class OtherFeatureAdapter extends BaseQuickAdapter<OtherFeatureBean, BaseViewHolder> {
    public OtherFeatureAdapter() {
        super(R.layout.item_other_feature);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, @Nullable OtherFeatureBean otherFeatureBean) {
       baseViewHolder.setImageResource(R.id.img_tag,otherFeatureBean.getImg());
       baseViewHolder.setText(R.id.tv_title,otherFeatureBean.getTitle());
    }
}
