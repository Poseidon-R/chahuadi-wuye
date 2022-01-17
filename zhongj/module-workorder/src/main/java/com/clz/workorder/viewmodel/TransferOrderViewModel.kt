package com.clz.workorder.viewmodel

import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository

/**
 *
 * @Description:
 * @Author: XCH
 * @CreateDate: 2021/12/15 10:06
 */
class TransferOrderViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val btnSubmitClick: BindingCommand<Any> = BindingCommand(BindingAction {

    })
}