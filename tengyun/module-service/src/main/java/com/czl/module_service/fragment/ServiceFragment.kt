package com.czl.module_service.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_service.BR
import com.czl.module_service.R
import com.czl.module_service.databinding.ServiceFragmentBinding
import com.czl.module_service.viewmodel.ServiceViewModel

/**
 * 服务
 */
@Route(path = AppConstants.Router.Service.F_SERVICE)
class ServiceFragment : BaseFragment<ServiceFragmentBinding, ServiceViewModel>() {
    override fun initContentView(): Int {
        return R.layout.service_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {

    }

    override fun useBaseLayout(): Boolean {
        return false
    }

}