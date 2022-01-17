package com.czl.module_park.adapter

import android.widget.CompoundButton
import android.widget.RadioGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.bean.CarItem
import com.czl.module_park.R
import com.czl.module_park.databinding.ItemChoiceCarBinding

/**
 *  Author:xch
 *  Date:2021/9/26
 *  Do:
 */
class ChoiceCarAdapter :
    BaseQuickAdapter<CarItem, BaseDataBindingHolder<ItemChoiceCarBinding>>(
        R.layout.item_choice_car
    ) {
    override fun convert(holder: BaseDataBindingHolder<ItemChoiceCarBinding>, item: CarItem) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@ChoiceCarAdapter
            executePendingBindings()
        }

        holder.dataBinding?.cb?.setOnCheckedChangeListener { _, check ->
            if (check) {
                data.forEach {
                    it.status = false
                }
            }else {
                item.status = true
            }
        }
    }
}