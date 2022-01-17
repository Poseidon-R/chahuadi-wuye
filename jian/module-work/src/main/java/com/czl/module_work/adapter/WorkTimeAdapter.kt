package com.czl.module_work.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.WorkOrderTimeBean
import com.czl.module_work.databinding.WorkItemWorkTimeBindingImpl
import com.czl.module_work.R

class WorkTimeAdapter :
    BaseQuickAdapter<WorkOrderTimeBean, BaseDataBindingHolder<WorkItemWorkTimeBindingImpl>>(R.layout.work_item_work_time) {

    override fun convert(
        holder: BaseDataBindingHolder<WorkItemWorkTimeBindingImpl>,
        roomItem: WorkOrderTimeBean
    ) {
        holder.dataBinding?.apply {
            label = roomItem.label
            value = roomItem.value
        }
    }
}