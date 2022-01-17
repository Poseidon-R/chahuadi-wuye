package com.czl.base.view

import android.content.Context
import androidx.databinding.DataBindingUtil
import com.czl.base.R
import com.czl.base.adapter.EasyAdapter
import com.czl.base.base.BaseActivity
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.databinding.PopBottomListViewBinding
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.interfaces.OnSelectListener


/**
 *  Author:xch
 *  Date:2021/9/27
 *  Do:
 */
class BottomListPopView(
    ctx: Context,
    private val list: ArrayList<String>? = null,
    val listener: OnSelectListener
) : BottomPopupView(ctx) {
    private var dataBinding: PopBottomListViewBinding? = null
    override fun getImplLayoutId(): Int {
        return R.layout.pop_bottom_list_view
    }

    override fun onCreate() {
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.apply {
            pop = this@BottomListPopView
            rv.apply {
                setupDivider(false)
                adapter = EasyAdapter().apply {
                    setList(list as List<String>)
                    setOnItemClickListener() { adapter, _, position ->
                        dismiss()
                        listener.onSelect(position, adapter.getItem(position).toString())
                    }
                }
            }
            executePendingBindings()
        }
    }

    val onClosePopClick: BindingCommand<Void> = BindingCommand(BindingAction {
        dismiss()
    })

}