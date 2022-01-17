package com.czl.module_user.viewmodel

import androidx.databinding.ObservableField
import com.czl.base.base.AppManager
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.UserInfo
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

class UserViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()
    var userName = ObservableField("")
    var avatarUrl = ObservableField("")
    var userPhone = ObservableField("")
    var areaName = ObservableField(model.getAreaName())
    var hotLinePhone = ""
    var deleteNotice = ""

    class UiChangeEvent {
        val confirmLayoutEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val confirmDeleteAcountEvent: SingleLiveEvent<String> = SingleLiveEvent()
        val hotLineEvent: SingleLiveEvent<String> = SingleLiveEvent()
        val changeAreaEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val onLoginOutCommand: BindingCommand<Any> = BindingCommand(BindingAction {
        uc.confirmLayoutEvent.call()
    })
    val onDeleteAccountCommand: BindingCommand<Any> = BindingCommand(BindingAction {
        uc.confirmDeleteAcountEvent.postValue(deleteNotice)
    })
    val onKefuPhoneCommand: BindingCommand<Any> = BindingCommand(BindingAction {
        uc.hotLineEvent.postValue(hotLinePhone)
    })
    val onAboutClick: BindingCommand<Any> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.User.F_ABOUT)
    })
    val onOrderClick: BindingCommand<Any> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Order.F_ORDER_MANAGER)
    })
    val onGoUserInfoCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.User.F_USER_INFO)
    })
    val onTransferOrderClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Order.F_TRANSFER_ORDER)
    })
    val onReceiveOrderClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Order.F_RECEIVE_ORDER)
    })
    val onVerifyPhoneCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Login.F_VERIFYPHONE)
    })
    val onVerifyPwdCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Login.F_VERIFYPASSWORD)
    })
    val onChangeAreaCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.changeAreaEvent.call()
    })

    /**
     * 获取用户详细信息
     */
    fun getUserInfo() {
        model.apply {
            getUserInfoNet()
                .compose(RxThreadHelper.rxSchedulerHelper(this@UserViewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<UserInfo>>() {
                    override fun onResult(t: BaseBean<UserInfo>) {
                        t.data?.let {
                            saveUserInfoData(it)
                            userName.set(it.name)
                            userPhone.set(it.phone)
                            avatarUrl.set(it.avatarUrl)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        showNormalToast(msg)
                    }

                })
        }
    }

    /**
     * 退出登陆
     */
    fun logout() {
        model.apply {
            logout()
                .compose(RxThreadHelper.rxSchedulerHelper(this@UserViewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        clearLoginState()
                        startContainerActivity(AppConstants.Router.Login.F_LOGIN)
                        AppManager.instance.finishAllActivity()
                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                    }

                })
        }
    }

    /**
     * 获取热线
     */
    fun getHotLine() {
        model.apply {
            getHotLine()
                .compose(RxThreadHelper.rxSchedulerHelper(this@UserViewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<String>>() {
                    override fun onResult(t: BaseBean<String>) {
                        if (t.code == 200) {
                            hotLinePhone = t.data.toString()
                        }
                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                    }

                })
        }
    }

    /**
     * 获取注销须知
     */
    fun getDeleteNotice() {
        model.apply {
            getDeleteNotice()
                .compose(RxThreadHelper.rxSchedulerHelper(this@UserViewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<String>>() {
                    override fun onResult(t: BaseBean<String>) {
                        if (t.code == 200) {
                            deleteNotice = t.data.toString()
                        }
                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                    }

                })
        }
    }

    /**
     * 注销账户
     */
    fun deleteUserAccount() {
        model.apply {
            deleteUserAccount()
//            logout()
                .compose(RxThreadHelper.rxSchedulerHelper(this@UserViewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        if (t.code == 200) {
                            clearAllData()
                            startContainerActivity(AppConstants.Router.Login.F_LOGIN)
                            AppManager.instance.finishAllActivity()
                        } else {
                            showErrorToast(t.msg)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                    }
                })
        }
    }
}