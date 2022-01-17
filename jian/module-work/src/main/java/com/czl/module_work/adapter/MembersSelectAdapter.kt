package com.czl.module_work.adapter


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.MembersBean
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkFragmentMembersSelectBinding
import com.czl.module_work.databinding.WorkItemMembersSelectBinding
import com.czl.module_work.databinding.WorkItemTakeCareOrderBinding

class MembersSelectAdapter :
    BaseQuickAdapter<MembersBean, BaseDataBindingHolder<WorkItemMembersSelectBinding>>(R.layout.work_item_members_select) {

    private var selectPos = -1

    private var isSelectMode = true

    fun setSelectMode(isSelectMode: Boolean) {
        this.isSelectMode = isSelectMode
    }

    override fun convert(
        holder: BaseDataBindingHolder<WorkItemMembersSelectBinding>,
        roomItem: MembersBean
    ) {
        holder.dataBinding?.apply {
            item = roomItem.nickName
            isSelected = selectPos == holder.adapterPosition
            executePendingBindings()
        }
        if (isSelectMode) {
            holder.itemView.setOnClickListener {
                selectPos = holder.absoluteAdapterPosition
                notifyDataSetChanged()
            }
        }
    }

    fun getSelectMembers(): List<MembersBean> {
        if (selectPos == -1) return listOf()
        val result = arrayListOf<MembersBean>()
        result.add(data.get(selectPos))
        return result
    }
}