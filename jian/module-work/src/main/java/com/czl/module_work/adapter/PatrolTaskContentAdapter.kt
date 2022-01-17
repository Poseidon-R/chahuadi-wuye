package com.czl.module_work.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.PatrolOrderDetailBean
import com.czl.base.data.bean.WorkOrderDetailBean
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkItemDispatchDeviceBinding
import com.czl.module_work.databinding.WorkItemPatrolTaskContentBinding


/**
 * 创建日期：2022/1/15  16:24
 * 类说明:
 * @author：86152
 */
class PatrolTaskContentAdapter :
    BaseQuickAdapter<PatrolOrderDetailBean.StandardList, BaseDataBindingHolder<WorkItemPatrolTaskContentBinding>>(
        R.layout.work_item_patrol_task_content
    ) {

    override fun convert(
        holder: BaseDataBindingHolder<WorkItemPatrolTaskContentBinding>,
        roomItem: PatrolOrderDetailBean.StandardList
    ) {
        holder.dataBinding?.apply {
            content = "要求:" + roomItem.optionName
            option1 = roomItem.option1
            option2 = roomItem.option2
            select = if (TextUtils.isEmpty(roomItem.selected)) 0 else roomItem.selected.toInt()
            option3 = roomItem.option3
            executePendingBindings()
        }
        val options1 = holder.getView<View>(R.id.ll_patrol_1)
        options1.setOnClickListener {
            holder.dataBinding?.apply {
                select = 1
                roomItem.selected = "1"
                executePendingBindings()
            }
        }
        val options2 = holder.getView<View>(R.id.ll_patrol_2)
        options2.setOnClickListener {
            holder.dataBinding?.apply {
                select = 2
                roomItem.selected = "2"
                executePendingBindings()
            }
        }
        val options3 = holder.getView<View>(R.id.ll_patrol_3)
        options3.setOnClickListener {
            holder.dataBinding?.apply {
                select = 3
                roomItem.selected = "3"
                executePendingBindings()
            }
        }
    }
}