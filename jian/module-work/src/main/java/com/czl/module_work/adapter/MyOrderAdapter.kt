package com.czl.module_work.adapter


import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.util.FormUtils
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkItemMyOrderBinding
import com.czl.module_work.databinding.WorkItemTakeCareOrderBinding
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyOrderAdapter :
    BaseQuickAdapter<WorkOrderBean.Data, BaseDataBindingHolder<WorkItemMyOrderBinding>>(R.layout.work_item_my_order) {

    private val selectPos = mutableListOf<String>()

    private var isCheckMode = false

    public fun setCheckMode(isCheckMode: Boolean) {
        this.isCheckMode = isCheckMode
    }

    /**
     * 维保 1   巡检 3   报修2
     *
     * 待接单 1  处理中 2   已完成3
     */
    public fun isCheckMode() = isCheckMode

    override fun convert(
        holder: BaseDataBindingHolder<WorkItemMyOrderBinding>,
        orderItem: WorkOrderBean.Data
    ) {
        holder.dataBinding?.apply {
            item = orderItem
            itemType = orderItem.orderTypeId
            orderNo = "工单编号：${orderItem.orderNo}"
            state = if (orderItem.orderState == 1) {
                "待接单"
            } else if (orderItem.orderState == 2) {
                "处理中"
            } else {
                "已完成"
            }
            date = if (TextUtils.isEmpty(orderItem.orderDate)) "" else FormUtils.dealDateFormat(
                orderItem.orderDate
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