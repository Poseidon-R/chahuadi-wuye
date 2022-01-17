package com.clz.workorder.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.clz.workorder.BR
import com.clz.workorder.R
import com.clz.workorder.databinding.FragmentReceiveOrderBinding
import com.clz.workorder.viewmodel.ReceiveOrderViewModel
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants

/**
 *
 * @Description:接单
 * @Author: XCH
 * @CreateDate: 2021/12/15 10:12
 */
@Route(path = AppConstants.Router.Order.F_RECEIVE_ORDER)
class ReceiveOrderFragment : BaseFragment<FragmentReceiveOrderBinding, ReceiveOrderViewModel>() {
    override fun initContentView(): Int {
        return R.layout.fragment_receive_order
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("接单")
        if (arguments != null){
            viewModel.no = requireArguments().getString(AppConstants.BundleKey.ORDER_NO_KEY).toString()
        }
    }
}