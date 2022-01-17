package com.czl.module_login.viewmodel

import androidx.databinding.ObservableField
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.blankj.utilcode.util.*
import com.czl.base.base.AppManager
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.AreaInfo
import com.czl.base.data.bean.LoginUser
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.route.RouteCenter
import com.czl.base.util.RxThreadHelper
import com.czl.base.util.SystemUtil
import com.czl.module_login.bean.LoginParamsBean

class LoginViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    private lateinit var userBean: LoginUser
    private var isAgree: Boolean = false
    val uc = UiChangeEvent()

    inner class UiChangeEvent {
        val areaListEvent: SingleLiveEvent<List<AreaInfo>> = SingleLiveEvent()
        val bindAccountEvent: SingleLiveEvent<String> = SingleLiveEvent()
    }

    var phone = ObservableField(model.getLoginPhone())
    var pwd = ObservableField(model.getLoginPwd())
    var isRememberPwd = ObservableField(model.isRememberPwd())

    //    val onPhoneChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
//        phone.set(it)
//    })
//    val onPwdChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
//        pwd.set(it)
//    })
    val onPhoneCleanClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        phone.set("")
    })
    val onPwdCleanClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        pwd.set("")
    })

    val forgetPwdClickCommand: BindingCommand<Any> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Login.F_FORGETPW)
    })
    val btnLoginClick: BindingCommand<Any> = BindingCommand(BindingAction {
        loginByPwd()
    })
    val onCheckChangeClick: BindingCommand<Boolean> = BindingCommand(BindingConsumer {
        isAgree = it
    })

    fun saveData() {
        model.saveUserData(userBean)
    }

    private fun loginByPwd() {
        phone.set("18598104558")
        pwd.set("123456")
        if (phone.get().isNullOrBlank()) {
            showNormalToast("手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(phone.get())) {
            showNormalToast("手机号码格式不正确")
            return
        }
        if (!isAgree) {
            showNormalToast("请先同意相关协议政策")
            return
        }
        if (pwd.get().isNullOrBlank()) {
            showNormalToast("请输入密码")
            return
        }


        uc.bindAccountEvent.postValue(phone.get())
        model.apply {

            val paramsBean = LoginParamsBean(
                phone.get()!!, pwd.get()!!, null,
                LoginParamsBean.PushBind(
                    SystemUtil.getDeviceBrand(),
                    "",
                    0,
                    PushServiceFactory.getCloudPushService().deviceId
                )
            )
            loginByPwd(GsonUtils.toJson(paramsBean))
                .compose(RxThreadHelper.rxSchedulerHelper(this@LoginViewModel))
                .doOnSubscribe { showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<LoginUser>>() {
                    override fun onResult(t: BaseBean<LoginUser>) {
                        dismissLoading()
                        if (t.code == 200) {
                            if (ObjectUtils.isNotEmpty(t.data)) {
                                userBean = t.data!!
                                saveData()
                                if (isRememberPwd.get() == true) {
                                    saveLoginPwd(pwd.get().toString())
                                } else {
                                    saveLoginPwd("")
                                }
                                isRememberPwd.get()?.let { saveIsRememberPwd(it) }
                                RouteCenter.navigate(AppConstants.Router.Main.A_MAIN)
                                AppManager.instance.finishAllActivity()
                            } else {
                                showErrorToast(t.msg)
                            }
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