package com.czl.module_park.adapter

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.MonthCardListBean
import com.czl.module_park.databinding.ItemMonthCardsBinding
import com.czl.module_park.R
import com.czl.module_park.fragment.CardListFragment

/**
 *  Author:xch
 *  Date:2021/9/11
 *  Do:
 */
class CardAdapter(val fragment: CardListFragment) :
    BaseQuickAdapter<MonthCardListBean.Data, BaseDataBindingHolder<ItemMonthCardsBinding>>(
        R.layout.item_month_cards
    ) {

    override fun convert(
        holder: BaseDataBindingHolder<ItemMonthCardsBinding>,
        item: MonthCardListBean.Data
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@CardAdapter
            executePendingBindings()
        }
    }

    val onRenewClick: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is MonthCardListBean.Data) {
            fragment.startContainerActivity(AppConstants.Router.Park.F_RENEW_CARD, Bundle().apply {
                putSerializable(AppConstants.BundleKey.MONTH_CARD_DATA, it)
            })
        }
    })
}