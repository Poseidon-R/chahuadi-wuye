package com.czl.module_login.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.ActivityRegisterBinding
import com.czl.module_login.databinding.FragmentSetPwdBinding
import com.czl.module_login.viewmodel.SetPwdViewModel
@Route(path = AppConstants.Router.Login.F_SET_PWD)
class SetPwdFragment : BaseFragment<FragmentSetPwdBinding, SetPwdViewModel>() {

    override fun initContentView(): Int {
        return R.layout.fragment_set_pwd
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("设置密码")
    }
}