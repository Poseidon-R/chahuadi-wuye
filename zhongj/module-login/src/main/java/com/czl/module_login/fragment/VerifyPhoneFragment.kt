package com.czl.module_login.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseActivity
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.ActivityVerifyPhoneBinding
import com.czl.module_login.viewmodel.VerifyPhoneViewModel

@Route(path = AppConstants.Router.Login.F_VERIFYPHONE)
class VerifyPhoneFragment : BaseFragment<ActivityVerifyPhoneBinding, VerifyPhoneViewModel>() {

    override fun initContentView(): Int {
        return R.layout.activity_verify_phone
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("更换手机")

        viewModel.uc.codeClickedEvent.observe(this, {
            binding.codeBtn.start()
        })
    }
}