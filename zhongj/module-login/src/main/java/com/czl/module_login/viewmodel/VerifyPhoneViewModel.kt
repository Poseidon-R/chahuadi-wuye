package com.czl.module_login.viewmodel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.blankj.utilcode.util.RegexUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.data.DataRepository
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

/**
 *  Author:xch
 *  Date:2021/9/10
 *  Do:
 */
class VerifyPhoneViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var phone = ObservableField("")
    var code = ObservableField("")
    val uc = UiChangeEvent()

    inner class UiChangeEvent {
        val codeClickedEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }
    val onPhoneChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        phone.set(it)
    })
    val onCodeChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        code.set(it)
    })

    val onCodeClickCommand: BindingCommand<Any> = BindingCommand(BindingAction {
        getPhoneCode()
    })

    val onSubmitClickCommand: BindingCommand<Any> = BindingCommand(BindingAction {
        getPhoneCode()
    })

    private fun getPhoneCode() {
        if (phone.get().isNullOrBlank()) {
            showNormalToast("手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(phone.get())) {
            showErrorToast("请输入正确手机号")
            return
        }
        model.getPhoneCode(phone.get()!!)
            .compose(RxThreadHelper.rxSchedulerHelper(this@VerifyPhoneViewModel))
            .doOnSubscribe { showLoading() }
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                override fun onResult(t: BaseBean<Any?>) {
                    dismissLoading()
                    if (t.code == 200) {
                        uc.codeClickedEvent.call()
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    showErrorToast(msg)
                }

            })

    }

}