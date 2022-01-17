package com.czl.module_park.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.PayHelper
import com.czl.module_park.BR
import com.czl.module_park.R
import com.czl.module_park.databinding.FragmentPayRecordDetailBinding
import com.czl.module_park.viewmodel.PayRecordDetailViewModel

@Route(path = AppConstants.Router.Park.F_PAY_RECORD_DETAIL)
class PayRecordDetailFragment :
    BaseFragment<FragmentPayRecordDetailBinding, PayRecordDetailViewModel>() {

    private var orderNo: String = ""
    private var pauseStatus = false

    override fun initContentView(): Int {
        return R.layout.fragment_pay_record_detail
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("缴费详情")

        orderNo = arguments?.getString(AppConstants.BundleKey.ORDER_NO_KEY).toString()
        viewModel.getRecordDetail(orderNo)
    }

    override fun initViewObservable() {
        viewModel.uc.openAliPayEvent.observe(this, {
            PayHelper.openAlipay(it, requireContext())
        })

        viewModel.uc.openWxPayEvent.observe(this, {
            PayHelper.openWxPay(
                "payInfo=${it}&ispayType=1&areaId=${viewModel.model.getAreaId()}&payAmount=${viewModel.amount.get()}&reqsn=${viewModel.orderNo.get()}&appUserId=${viewModel.model.getUserId()}",
                requireContext()
            )
        })

        LiveBusCenter.observePayResultEvent(this, {
            when (it.payType) {
                AppConstants.Constants.PAY_SUCCESS_TYPE, AppConstants.Constants.PAY_FAIL_TYPE -> {
                    back()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (this.pauseStatus) {
            startContainerActivity(AppConstants.Router.Pay.F_PAY_RESULT, Bundle().apply {
                putString(AppConstants.BundleKey.ORDER_NO_KEY, orderNo)
            })
        }
        this.pauseStatus = false
    }

    override fun onPause() {
        super.onPause()
        this.pauseStatus = true
    }

}