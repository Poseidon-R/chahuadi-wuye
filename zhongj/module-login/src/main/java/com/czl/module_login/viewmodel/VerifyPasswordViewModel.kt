package com.czl.module_login.viewmodel

import androidx.databinding.ObservableField
import com.czl.base.base.AppManager
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.FormUtils
import com.czl.base.util.RxThreadHelper

/**
 *  Author:xch
 *  Date:2021/9/10
 *  Do:
 */
class VerifyPasswordViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var oldPwd = ObservableField("")
    var newPwd = ObservableField("")
    var confirmPwd = ObservableField("")

    val onOldPwdChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        oldPwd.set(it)
    })
    val onNewPwdChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        newPwd.set(it)
    })
    val onConfirmPwdChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        confirmPwd.set(it)
    })

    val onConfirmBtnClick: BindingCommand<Any> = BindingCommand(BindingAction {
        verifyPwdNet()
    })
    val onForgetPwdCommand: BindingCommand<Any> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Login.F_FORGETPW)
    })

    private fun verifyPwdNet() {
        if (oldPwd.get().isNullOrBlank()) {
            showErrorToast("旧密码不能为空")
            return
        }
        if (newPwd.get().isNullOrBlank()) {
            showErrorToast("新密码不能为空")
            return
        }
        if (confirmPwd.get().isNullOrBlank()) {
            showErrorToast("确认密码不能为空")
            return
        }
        if (!newPwd.get().equals(confirmPwd.get())) {
            showErrorToast("新密码和确认密码不一致")
            return
        }
        if (!FormUtils.isPassword(newPwd.get())) {
            showErrorToast("新密码格式不正确")
            return
        }
        model.apply {
            verifyPwdNet(newPwd.get()!!, oldPwd.get()!!)
                .compose(RxThreadHelper.rxSchedulerHelper(this@VerifyPasswordViewModel))
                .doOnSubscribe { showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        dismissLoading()
                        if (t.code == 200) {
                            showNormalToast(t.msg)
                            saveLoginPwd("")
                            clearLoginState()
                            startContainerActivity(AppConstants.Router.Login.F_LOGIN)
                            AppManager.instance.finishAllActivity()
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
}