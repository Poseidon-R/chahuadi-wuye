package com.czl.module_login.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.ActivityVerifyPasswordBinding
import com.czl.module_login.viewmodel.VerifyPasswordViewModel

@Route(path = AppConstants.Router.Login.F_VERIFYPASSWORD)
class VerifyPasswordFragment :
    BaseFragment<ActivityVerifyPasswordBinding, VerifyPasswordViewModel>() {

    override fun initContentView(): Int {
        return R.layout.activity_verify_password
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("密码设置")
    }
}