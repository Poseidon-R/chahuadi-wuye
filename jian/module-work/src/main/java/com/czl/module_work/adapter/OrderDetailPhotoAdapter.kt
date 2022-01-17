package com.czl.module_work.adapter

import android.text.TextUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkItemMembersSelectBinding
import com.czl.module_work.databinding.WorkItemOrderDetailPhotoBinding


/**
 * 创建日期：2021/12/28  18:17
 * 类说明:
 * @author：86152
 */
class OrderDetailPhotoAdapter :
    BaseQuickAdapter<String, BaseDataBindingHolder<WorkItemOrderDetailPhotoBinding>>(R.layout.work_item_order_detail_photo) {

    override fun convert(
        holder: BaseDataBindingHolder<WorkItemOrderDetailPhotoBinding>,
        item: String
    ) {
        if (!TextUtils.isEmpty(item)) {
            Glide.with(context).load(item).into(holder.getView(R.id.iv_pic))
        }
    }

}