package com.clz.workorder.adapter

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.clz.workorder.databinding.ItemOrderBinding
import com.clz.workorder.fragment.OrderListFragment
import com.clz.workorder.R
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.PayRecordListBean
import com.czl.base.data.bean.RepairBean

/**
 *
 * @Description:
 * @Author: XCH
 * @CreateDate: 2021/12/14 10:10
 */
class OrderAdapter(val fragment: OrderListFragment) :
    BaseQuickAdapter<RepairBean, BaseDataBindingHolder<ItemOrderBinding>>(
        R.layout.item_order
    ) {

    override fun convert(
        holder: BaseDataBindingHolder<ItemOrderBinding>,
        item: RepairBean
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@OrderAdapter
            executePendingBindings()
        }
    }

    val onItemClick: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is RepairBean) {
            fragment.startContainerActivity(
                AppConstants.Router.Order.F_ORDER_DETAIL,
                Bundle().apply {
                    putString(AppConstants.BundleKey.ORDER_NO_KEY, it.no)
                })
        }
    })
}