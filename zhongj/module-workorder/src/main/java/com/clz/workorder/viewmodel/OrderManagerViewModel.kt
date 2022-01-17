package com.clz.workorder.viewmodel

import androidx.databinding.ObservableField
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.data.DataRepository
import com.czl.base.event.SingleLiveEvent

/**
 *
 * @Description:
 * @Author: XCH
 * @CreateDate: 2021/12/14 10:07
 */
class OrderManagerViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val tabData = ObservableField("待接单,处理中,待支付/待审核,已完成")
    val uc = UiChangeEvent()

    inner class UiChangeEvent {
        val tabChangeLiveEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val pageChangeLiveEvent: SingleLiveEvent<Int> = SingleLiveEvent()
    }

    val onPageSelectedListener: BindingCommand<Int> = BindingCommand(BindingConsumer {
        uc.pageChangeLiveEvent.postValue(it)
    })

    val onTabSelectedCommand: BindingCommand<Int> = BindingCommand(BindingConsumer {
        uc.tabChangeLiveEvent.postValue(it)
    })
}