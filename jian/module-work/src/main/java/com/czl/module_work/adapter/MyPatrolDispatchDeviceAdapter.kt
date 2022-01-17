package com.czl.module_work.adapter


import android.graphics.Color
import com.blankj.utilcode.util.ResourceUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.PatrolOrderDetailBean
import com.czl.base.data.bean.WorkOrderDetailBean
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkItemDispatchDeviceBinding
import com.czl.module_work.databinding.WorkItemTakeCareOrderBinding

class MyPatrolDispatchDeviceAdapter :
    BaseQuickAdapter<PatrolOrderDetailBean.TaskList, BaseDataBindingHolder<WorkItemDispatchDeviceBinding>>(
        R.layout.work_item_dispatch_device
    ) {

    private val selectPos = mutableListOf<String>()

    private var isCheckMode = false

    public fun setCheckMode(isCheckMode: Boolean) {
        this.isCheckMode = isCheckMode
    }

    public fun isCheckMode() = isCheckMode

    override fun convert(
        holder: BaseDataBindingHolder<WorkItemDispatchDeviceBinding>,
        roomItem: PatrolOrderDetailBean.TaskList
    ) {
        holder.dataBinding?.apply {
            item = roomItem.proDeviceInfo.deviceName
            isSelectMode = isCheckMode
            isSelected = selectPos.contains(holder.adapterPosition.toString())
            executePendingBindings()
        }
        if (roomItem.taskDetail.handleState == "1") {
            holder.setBackgroundColor(R.id.fl_root, Color.parseColor("#999999"))
        } else if (roomItem.taskDetail.handleState == "2") {
            holder.setBackgroundColor(R.id.fl_root, Color.parseColor("#00A468"))
        } else {
            holder.setBackgroundColor(R.id.fl_root, Color.parseColor("#DF3D38"))
        }
        if (isCheckMode) {
            holder.itemView.setOnClickListener {
                if (selectPos.contains(holder.adapterPosition.toString())) {
                    selectPos.remove(holder.adapterPosition.toString())
                } else {
                    selectPos.add(holder.adapterPosition.toString())
                }
                notifyItemChanged(holder.adapterPosition)
            }
        }

    }
}