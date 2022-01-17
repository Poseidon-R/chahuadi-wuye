package com.czl.module_user.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.AppUtils
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.FragmentAboutBinding
import com.czl.module_user.viewmodel.AboutViewModel

@Route(path = AppConstants.Router.User.F_ABOUT)
class AboutFragment : BaseFragment<FragmentAboutBinding, AboutViewModel>() {

    override fun initContentView(): Int {
        return R.layout.fragment_about
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("关于")

        AppUtils.getAppVersionCode()
    }
}