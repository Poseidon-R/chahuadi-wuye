package com.czl.module_car.adapter

import androidx.databinding.ObservableBoolean
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.blankj.utilcode.util.GsonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.data.bean.CarItem
import com.czl.module_car.R
import com.czl.module_car.databinding.ItemMyCarBinding
import com.czl.module_car.fragment.MyCarFragment
import com.czl.module_car.utils.RoomUtil

class MyCarAdapter(private val fragment: MyCarFragment) :
    BaseQuickAdapter<CarItem, BaseDataBindingHolder<ItemMyCarBinding>>(R.layout.item_my_car) {

    val edit = ObservableBoolean(false)
    private val rooms = fragment.viewModel.model.getRoomIdAndCode()?.let {
        GsonUtils.fromJson(
            it,
            List::class.java
        ) as List<String>?
    }

    override fun convert(holder: BaseDataBindingHolder<ItemMyCarBinding>, carItem: CarItem) {
        var houseCode = rooms?.let {
            RoomUtil.getRoomName(
                it,
                carItem.houseId
            )
        }
        holder.dataBinding?.apply {
            item = carItem
            houseName = houseCode
            adapter = this@MyCarAdapter
            executePendingBindings()
        }
    }

    val checkChangedClick:BindingCommand<Boolean> = BindingCommand(BindingConsumer {
        var list = mutableListOf<CarItem>()
        with(fragment) {
            list.clear()
            list = Stream.of(data).filter { e -> e.status }
                .collect(Collectors.toList()) as MutableList<CarItem>
            viewModel.setDeleteCarList(list)
        }
    })


    fun checkEditStatus(isEdit: Boolean) {
        this.edit.set(isEdit)
        notifyDataSetChanged()
    }
}