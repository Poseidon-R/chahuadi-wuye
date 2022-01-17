package com.czl.module_login.fragment

import android.content.ContentValues
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.sdk.android.push.CommonCallback
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.czl.base.base.AppManager
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.AreaInfo
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.DialogHelper
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.ActivityLoginBinding
import com.czl.module_login.viewmodel.LoginViewModel
import com.mpt.android.stv.Slice
import com.mpt.android.stv.callback.OnTextClick
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import java.util.*

@Route(path = AppConstants.Router.Login.F_LOGIN)
class LoginFragment : BaseFragment<ActivityLoginBinding, LoginViewModel>(), OnTextClick {

    private var areaList: List<AreaInfo> = arrayListOf()

    override fun initContentView(): Int {
        return R.layout.activity_login
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewObservable() {
        viewModel.uc.areaListEvent.observe(this, {
            areaList = it
        })
        viewModel.uc.bindAccountEvent.observe(this, {
            initChannel(it)
        })
        LiveBusCenter.observeSetPwdEvent(this, {
            viewModel.saveData()
        })
        binding.cbPasswordEye.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.etLoginPassword.transformationMethod= if (isChecked)
                HideReturnsTransformationMethod.getInstance()
            else
                PasswordTransformationMethod.getInstance()
        }
    }

    override fun initData() {
        var appName = resources.getString(R.string.app_name)
        binding.tvSecret.addSlice(
            Slice.Builder("我已阅读并同意")
                .textColor(Color.parseColor("#999999"))
                .textSize(35)
                .build()
        )
        binding.tvSecret.addSlice(
            Slice.Builder("《${appName}用户协议》")
                .textColor(Color.parseColor("#00A468"))
                .setOnTextClick(this)
                .setSliceId(0)
                .textSize(35)
                .build()
        )
        binding.tvSecret.addSlice(
            Slice.Builder("和")
                .textColor(Color.parseColor("#999999"))
                .textSize(35)
                .build()
        )
        binding.tvSecret.addSlice(
            Slice.Builder("《${appName}隐私政策》")
                .textColor(Color.parseColor("#00A468"))
                .setOnTextClick(this)
                .setSliceId(1)
                .textSize(35)
                .build()
        )
        binding.tvSecret.display()

    }

    private fun initChannel(phone: String) {
//        val pushService = PushServiceFactory.getCloudPushService()
//        pushService.bindAccount(phone, object : CommonCallback {
//            override fun onSuccess(response: String) {
//                Log.d(ContentValues.TAG, "init cloudchannel success")
//            }
//
//            override fun onFailed(errorCode: String, errorMessage: String) {
//                Log.d(
//                    ContentValues.TAG,
//                    "init cloudchannel failed -- errorcode:$errorCode -- errorMessage:$errorMessage"
//                )
//            }
//        })
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun onTextClick(view: View?, slice: Slice?) {
        when (slice?.sliceId) {
            0 -> {
                startContainerActivity(AppConstants.Router.Web.F_WEB, Bundle().apply {
                    putString(
                        AppConstants.BundleKey.WEB_MENU_KEY,
                        AppConstants.Constants.AGREEMENT_TEXT_URL
                    )
                })
            }
            1 -> {
                startContainerActivity(AppConstants.Router.Web.F_WEB, Bundle().apply {
                    putString(
                        AppConstants.BundleKey.WEB_MENU_KEY,
                        AppConstants.Constants.PRIVATE_TEXT_URL
                    )
                })
            }
        }
    }
}