package com.czl.module_user.adapter


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.RoomBean
import com.czl.module_user.databinding.ItemMyRoomBinding
import com.czl.module_user.fragment.MyRoomFragment
import com.czl.module_user.R

class MyRoomAdapter(private val fragment: MyRoomFragment) :
    BaseQuickAdapter<RoomBean, BaseDataBindingHolder<ItemMyRoomBinding>>(R.layout.item_my_room) {

    override fun convert(holder: BaseDataBindingHolder<ItemMyRoomBinding>, roomItem: RoomBean) {
        holder.dataBinding?.apply {
            item = roomItem
            executePendingBindings()

        }
    }
}