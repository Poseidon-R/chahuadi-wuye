package com.clz.workorder.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.clz.workorder.databinding.FragmentOrderManagerBinding
import com.clz.workorder.databinding.FragmentTransferOrderBinding
import com.clz.workorder.viewmodel.OrderManagerViewModel
import com.clz.workorder.viewmodel.TransferOrderViewModel
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.clz.workorder.BR
import com.clz.workorder.R

/**
 *
 * @Description:转单
 * @Author: XCH
 * @CreateDate: 2021/12/15 10:06
 */
@Route(path = AppConstants.Router.Order.F_TRANSFER_ORDER)
class TransferOrderFragment : BaseFragment<FragmentTransferOrderBinding, TransferOrderViewModel>() {
    override fun initContentView(): Int {
        return R.layout.fragment_transfer_order
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("转单")
    }
}