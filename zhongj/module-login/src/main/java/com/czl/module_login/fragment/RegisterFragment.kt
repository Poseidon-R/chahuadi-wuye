package com.czl.module_login.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseActivity
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.ActivityRegisterBinding
import com.czl.module_login.viewmodel.RegisterViewModel

@Route(path = AppConstants.Router.Login.F_REGISTER)
class RegisterFragment : BaseFragment<ActivityRegisterBinding, RegisterViewModel>() {

    override fun initContentView(): Int {
        return R.layout.activity_register
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("注册")
    }
}