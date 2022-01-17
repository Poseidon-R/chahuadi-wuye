package com.czl.module_user.viewmodel

import android.os.Bundle
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.AppUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper
import java.util.*

class AboutViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val versionName = ObservableField(AppUtils.getAppVersionName())

    val onAgreeClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Web.F_WEB, Bundle().apply {
            putString(
                AppConstants.BundleKey.WEB_MENU_KEY,
                AppConstants.Constants.AGREEMENT_TEXT_URL
            )
        })
    })

    val onPrivateClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Web.F_WEB, Bundle().apply {
            putString(
                AppConstants.BundleKey.WEB_MENU_KEY,
                AppConstants.Constants.PRIVATE_TEXT_URL
            )
        })
    })

    val onVersionClick: BindingCommand<Void> = BindingCommand(BindingAction {
        getVersionName()
    })

    private fun getVersionName() {
        model.getVersionName()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading() }
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                override fun onResult(t: BaseBean<Any?>) {
                    dismissLoading()
                    if (t.code == 200) {
                        t.msg?.let {
                            var code = it.replace(".", "")
                            var oldCode = versionName.get()?.replace(".", "")
                            if (oldCode != null) {
                                if (code.toLong() > oldCode.toLong()) {
                                    showNormalToast("当前最新版本为${it}，请移步应用商店下载最新版本")
                                }else{
                                    showNormalToast("已为最新版本")
                                }
                            }else{
                                showNormalToast("已为最新版本")
                            }
                        }
                    } else {

                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    showErrorToast(msg)
                }

            })
    }


}