package com.czl.module_work.adapter


import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.TakeCareBean
import com.czl.base.data.bean.TakeCareRecordBean
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.util.FormUtils
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkItemTakeCareOrderBinding
import com.czl.module_work.databinding.WorkItemTakeCareRecordBinding

class TakeCareRecordAdapter :
    BaseQuickAdapter<TakeCareRecordBean.Data, BaseDataBindingHolder<WorkItemTakeCareRecordBinding>>(
        R.layout.work_item_take_care_record
    ) {

    private var isCheckMode = false

    public fun setCheckMode(isCheckMode: Boolean) {
        this.isCheckMode = isCheckMode
    }

    override fun convert(
        holder: BaseDataBindingHolder<WorkItemTakeCareRecordBinding>,
        orderItem: TakeCareRecordBean.Data
    ) {
        holder.dataBinding?.apply {
            item = orderItem
            orderNo = "工单编号：${orderItem.taskNo}"
            state = orderItem.execUserName
            date =
                "完成日期：" + if (TextUtils.isEmpty(orderItem.finishTime)) "" else FormUtils.dealDateFormat(
                    orderItem.finishTime
                )
            executePendingBindings()
        }
    }
}