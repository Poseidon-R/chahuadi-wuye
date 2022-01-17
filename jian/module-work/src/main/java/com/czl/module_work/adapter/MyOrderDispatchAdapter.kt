package com.czl.module_work.adapter


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.TakeCareBean
import com.czl.base.data.bean.WorkOrderBean
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkItemTakeCareOrderBinding

class MyOrderDispatchAdapter :
    BaseQuickAdapter<TakeCareBean.Data, BaseDataBindingHolder<WorkItemTakeCareOrderBinding>>(R.layout.work_item_take_care_order) {

    override fun convert(holder: BaseDataBindingHolder<WorkItemTakeCareOrderBinding>, roomItem: TakeCareBean.Data) {
        holder.dataBinding?.apply {
            orderName = roomItem.orderName
            executePendingBindings()
        }
    }
}