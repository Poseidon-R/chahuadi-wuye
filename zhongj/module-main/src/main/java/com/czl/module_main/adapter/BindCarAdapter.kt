package com.czl.module_main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.data.bean.CarItem
import com.czl.module_main.R
import com.czl.module_main.databinding.ItemBindCarBinding
import com.czl.module_main.listener.DeleteCarListener

class BindCarAdapter() :
    BaseQuickAdapter<CarItem, BaseDataBindingHolder<ItemBindCarBinding>>(R.layout.item_bind_car) {

    private lateinit var listener: DeleteCarListener

    override fun convert(holder: BaseDataBindingHolder<ItemBindCarBinding>, item: CarItem) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@BindCarAdapter
            executePendingBindings()
        }
    }

    val onDeleteCarClick: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is CarItem) {
            listener?.deleteCar(it.vehicleNo)
        }
    })

    fun setDeleteCarListener(listener: DeleteCarListener) {
        this.listener = listener
    }

}