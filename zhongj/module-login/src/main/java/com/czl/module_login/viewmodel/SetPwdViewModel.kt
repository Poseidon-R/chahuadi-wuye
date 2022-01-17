package com.czl.module_login.viewmodel

import androidx.databinding.ObservableField
import com.blankj.utilcode.util.StringUtils
import com.czl.base.base.AppManager
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.event.LiveBusCenter
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.route.RouteCenter
import com.czl.base.util.FormUtils
import com.czl.base.util.RxThreadHelper

class SetPwdViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var newPwd = ObservableField("")
    var confirmPwd = ObservableField("")

    val onNewPwdChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        newPwd.set(it)
    })
    val onConfirmPwdChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        confirmPwd.set(it)
    })

    val onConfirmBtnClick: BindingCommand<Any> = BindingCommand(BindingAction {
        verifyPwdNet()
    })

    private fun verifyPwdNet() {
        if (newPwd.get().isNullOrBlank()) {
            showErrorToast("密码不能为空")
            return
        }
        if (confirmPwd.get().isNullOrBlank()) {
            showErrorToast("确认密码不能为空")
            return
        }
        if (!newPwd.get().equals(confirmPwd.get())) {
            showErrorToast("密码和确认密码不一致")
            return
        }
        if (!FormUtils.isPassword(newPwd.get())) {
            showErrorToast("密码格式不正确")
            return
        }

        model.apply {
            initPassword(newPwd.get()!!)
                .compose(RxThreadHelper.rxSchedulerHelper(this@SetPwdViewModel))
                .doOnSubscribe { showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        dismissLoading()
                        if (t.code == 200) {
                            saveLoginPwd(newPwd.get().toString())
                            LiveBusCenter.postSetPwdEvent()
                            RouteCenter.navigate(AppConstants.Router.Main.A_MAIN)
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