package com.czl.module_login.viewmodel

import androidx.databinding.ObservableField
import com.blankj.utilcode.util.RegexUtils
import com.czl.base.base.AppManager
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.data.DataRepository
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.FormUtils
import com.czl.base.util.RxThreadHelper

/**
 *  Author:xch
 *  Date:2021/9/11
 *  Do:
 */
class ForgetPwViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    val uc = UiChangeEvent()
    var phone = ""
    var pwd = ""
    var code = ""

    inner class UiChangeEvent {
        val codeClickedEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val onPhoneChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        phone = it
    })
    val onPwdChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        pwd = it
    })
    val onCodeChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        code = it
    })

    val onCodeClickCommand: BindingCommand<Any> = BindingCommand(BindingAction {
        getPhoneCode()
    })

    val btnSubmitClick: BindingCommand<Any> = BindingCommand(BindingAction {
        retrievePassword()
    })

    private fun getPhoneCode() {
        if (phone.isNullOrBlank()) {
            showNormalToast("手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showErrorToast("请输入正确手机号")
            return
        }
        model.getPhoneCode(phone)
            .compose(RxThreadHelper.rxSchedulerHelper(this@ForgetPwViewModel))
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

    private fun retrievePassword() {
        if (phone.isNullOrBlank() || pwd.isNullOrBlank()) {
            showNormalToast("手机号或密码不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showErrorToast("请输入正确手机号")
            return
        }
        if (!FormUtils.isPassword(pwd)) {
            showErrorToast("密码格式不正确")
            return
        }
        model.apply {
            retrievePassword(phone, code, pwd)
                .compose(RxThreadHelper.rxSchedulerHelper(this@ForgetPwViewModel))
                .doOnSubscribe { showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        dismissLoading()
                        showNormalToast(t.msg)
                        if (t.code == 200) {
                            AppManager.instance.finishActivity()
                        }
                    }

                    override fun onFailed(msg: String?) {
                        dismissLoading()
                        showNormalToast(msg)
                    }

                })
        }
    }
}