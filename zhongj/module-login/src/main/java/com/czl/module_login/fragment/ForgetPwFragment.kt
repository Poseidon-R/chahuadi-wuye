package com.czl.module_login.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseActivity
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.ActivityForgetPwBinding
import com.czl.module_login.viewmodel.ForgetPwViewModel

@Route(path = AppConstants.Router.Login.F_FORGETPW)
class ForgetPwFragment : BaseFragment<ActivityForgetPwBinding, ForgetPwViewModel>() {

    override fun initContentView(): Int {
        return R.layout.activity_forget_pw
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("忘记密码")

        viewModel.uc.codeClickedEvent.observe(this, {
            binding.codeBtn.start()
        })
    }

}