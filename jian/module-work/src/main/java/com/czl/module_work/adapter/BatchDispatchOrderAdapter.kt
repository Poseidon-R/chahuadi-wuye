package com.czl.module_work.adapter


import android.graphics.Color
import com.blankj.utilcode.util.ResourceUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.TakeCareDispatchBean
import com.czl.base.data.bean.WorkOrderDetailBean
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkItemBatchDispatchOrderBinding
import com.czl.module_work.databinding.WorkItemDispatchDeviceBinding
import com.czl.module_work.databinding.WorkItemTakeCareOrderBinding

class BatchDispatchOrderAdapter :
    BaseQuickAdapter<String, BaseDataBindingHolder<WorkItemBatchDispatchOrderBinding>>(
        R.layout.work_item_batch_dispatch_order
    ) {

    override fun convert(
        holder: BaseDataBindingHolder<WorkItemBatchDispatchOrderBinding>,
        roomItem: String
    ) {
        holder.dataBinding?.apply {
            order = "工单：${roomItem}"
            executePendingBindings()
        }
    }
}