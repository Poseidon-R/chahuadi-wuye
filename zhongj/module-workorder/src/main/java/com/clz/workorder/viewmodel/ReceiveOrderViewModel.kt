package com.clz.workorder.viewmodel

import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.data.DataRepository
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

/**
 *
 * @Description:
 * @Author: XCH
 * @CreateDate: 2021/12/15 10:06
 */
class ReceiveOrderViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var no = ""
    var memo = ""

    val onCommentChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        memo = it
    })

    val btnSubmitClick: BindingCommand<Any> = BindingCommand(BindingAction {
        takeRepaire()
    })

    private fun takeRepaire() {
        model.takeRepair(
            mapOf(
                "memo" to memo,
                "no" to no
            )
        ).compose(RxThreadHelper.rxSchedulerHelper(this@ReceiveOrderViewModel))
            .doOnSubscribe { showLoading() }
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                override fun onResult(t: BaseBean<Any?>) {
                    dismissLoading()
                    if (t.code == 200) {
//                        uc.evaluateCompleteEvent.call()
                    } else {
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    showErrorToast(msg)
                }

            })
    }
}