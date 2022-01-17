package com.czl.module_work.adapter


import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.PatrolBean
import com.czl.base.data.bean.TakeCareBean
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.util.FormUtils
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkItemTakeCareOrderBinding

class PatrolOrderAdapter :
    BaseQuickAdapter<PatrolBean.Data, BaseDataBindingHolder<WorkItemTakeCareOrderBinding>>(R.layout.work_item_take_care_order) {

    private val selectPos = mutableListOf<String>()

    private var isCheckMode = false

    public fun setCheckMode(isCheckMode: Boolean) {
        this.isCheckMode = isCheckMode
    }

    override fun convert(
        holder: BaseDataBindingHolder<WorkItemTakeCareOrderBinding>,
        orderItem: PatrolBean.Data
    ) {
        holder.dataBinding?.apply {
            orderName = orderItem.orderName
            itemType = 3
            total = orderItem.num
            okNum = orderItem.okNum
            exceptionNum = orderItem.exceptionNum
            undoNum = orderItem.undoNum
            orderNo = "工单编号：${orderItem.orderNo}"
            state = if (orderItem.taskState == "0") {
                "待接单"
            } else if (orderItem.taskState == "1") {
                "未开始"
            } else if (orderItem.taskState == "2") {
                "处理中"
            } else if (orderItem.taskState == "3") {
                "待确认"
            } else if (orderItem.taskState == "4") {
                "已超期"
            } else {
                "已完成"
            }
            date =
                "计划日期：" + if (TextUtils.isEmpty(orderItem.startTime)) "" else FormUtils.dealDateFormat(
                    orderItem.startTime
                ) + "-" + if (TextUtils.isEmpty(orderItem.endTime)) "" else FormUtils.dealDateFormat(
                    orderItem.endTime
                )
            executePendingBindings()
        }
        holder.dataBinding?.ivCheck?.setOnClickListener {
            if (selectPos.contains(holder.adapterPosition.toString())) {
                selectPos.remove(holder.adapterPosition.toString())
            } else {
                selectPos.add(holder.adapterPosition.toString())
            }
            notifyItemChanged(holder.adapterPosition)
        }

        holder.dataBinding?.ivCheck?.isSelected =
            selectPos.contains(holder.adapterPosition.toString())
        holder.dataBinding?.ivCheck?.visibility = if (isCheckMode) View.VISIBLE else View.GONE
    }
}