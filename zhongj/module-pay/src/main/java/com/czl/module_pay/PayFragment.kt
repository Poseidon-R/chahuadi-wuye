package com.czl.module_pay

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.GsonUtils
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.PayHelper
import com.czl.module_pay.databinding.FragmentPayBinding
import com.czl.module_pay.viewmodel.PayViewModel

@Route(path = AppConstants.Router.Pay.F_PAY_DETAIL)
class PayFragment : BaseFragment<FragmentPayBinding, PayViewModel>() {

    private var payInfoParams: String = ""
    private var paramsMap: Map<String, String> = HashMap()
    private var reqsn = ""
    private var pauseStatus = false

    override fun initContentView(): Int {
        return R.layout.fragment_pay
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("支付详情")
        payInfoParams = arguments?.getString(AppConstants.BundleKey.PAY_ORDER_KEY).toString()
        paramsMap = GsonUtils.fromJson(payInfoParams, Map::class.java) as Map<String, String>
        viewModel.setPayParams(paramsMap)
    }

    override fun initViewObservable() {
        viewModel.uc.openAliPayEvent.observe(this, {
            reqsn = it?.reqsn.toString()
            PayHelper.openAlipay(it.payinfo, requireContext())
        })
        viewModel.uc.openWxPayEvent.observe(this, {
            var parStr = ""
            paramsMap.forEach { parStr += "${it.key}=${it.value}&" }
            PayHelper.openWxPay(
                "${parStr}ispayType=0&appUserId=${viewModel.model.getUserId()}",
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
        LiveBusCenter.observeWxPayCompleteEvent(this, {
            this.pauseStatus = false
            var resCode = it.msg.toString()
            if (resCode.isNotBlank() && resCode != "1") {
                showSuccessToast("支付成功")
                LiveBusCenter.postPayResultEvent(AppConstants.Constants.PAY_SUCCESS_TYPE)
            } else {
                showErrorToast("支付失败")
                startContainerActivity(AppConstants.Router.Park.F_PAY_RECORDS)
                LiveBusCenter.postPayResultEvent(AppConstants.Constants.PAY_FAIL_TYPE)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (this.pauseStatus) {
            startContainerActivity(AppConstants.Router.Pay.F_PAY_RESULT, Bundle().apply {
                putString(AppConstants.BundleKey.ORDER_NO_KEY, reqsn)
            })
        }
        this.pauseStatus = false
    }

    override fun onPause() {
        super.onPause()
        this.pauseStatus = true
    }
}