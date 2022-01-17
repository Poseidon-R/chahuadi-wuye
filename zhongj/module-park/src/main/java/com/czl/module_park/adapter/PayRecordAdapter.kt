package com.czl.module_park.adapter

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.PayRecordListBean
import com.czl.module_park.R
import com.czl.module_park.databinding.ItemPayRecordBinding
import com.czl.module_park.fragment.PayRecordsFragment

/**
 *  Author:xch
 *  Date:2021/9/12
 *  Do:
 */
class PayRecordAdapter(val fragment: PayRecordsFragment) :
    BaseQuickAdapter<PayRecordListBean.Record, BaseDataBindingHolder<ItemPayRecordBinding>>(
        R.layout.item_pay_record
    ) {

    override fun convert(
        holder: BaseDataBindingHolder<ItemPayRecordBinding>,
        record: PayRecordListBean.Record
    ) {
        holder.dataBinding?.apply {
            item = record
            adapter = this@PayRecordAdapter
            executePendingBindings()
        }
    }

    val onItemClick: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is PayRecordListBean.Record) {
            fragment.startContainerActivity(
                AppConstants.Router.Park.F_PAY_RECORD_DETAIL,
                Bundle().apply {
                    putString(AppConstants.BundleKey.ORDER_NO_KEY, it.orderNo)
                })
        }
    })
}