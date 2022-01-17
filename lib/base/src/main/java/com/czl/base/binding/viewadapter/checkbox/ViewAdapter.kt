package com.czl.base.binding.viewadapter.checkbox

import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.bean.CarItem

object ViewAdapter {
    /**
     * @param bindingCommand //绑定监听
     */
    @JvmStatic
    @BindingAdapter(value = ["onCheckedChangedCommand"], requireAll = false)
    fun setCheckedChanged(checkBox: CheckBox, bindingCommand: BindingCommand<Boolean?>) {
        checkBox.setOnCheckedChangeListener { _, b -> bindingCommand.execute(b) }
    }

    @JvmStatic
    @BindingAdapter(value = ["onCheckedCommand", "rvItemBean"], requireAll = false)
    fun setCheckChanged(checkBox: CheckBox, bindingCommand: BindingCommand<Boolean?>, item: Any?) {
        checkBox.setOnCheckedChangeListener { _, b ->
            if (item is CarItem) {
                item.status = b
            }
            bindingCommand.execute(b)
        }
    }

}