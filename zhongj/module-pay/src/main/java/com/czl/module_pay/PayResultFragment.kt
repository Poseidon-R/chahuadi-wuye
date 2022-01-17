package com.czl.module_pay

import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ObjectUtils
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.czl.module_pay.databinding.FragmentPayResultBinding
import com.czl.module_pay.viewmodel.PayResultViewModel

@Route(path = AppConstants.Router.Pay.F_PAY_RESULT)
class PayResultFragment : BaseFragment<FragmentPayResultBinding, PayResultViewModel>() {

    private var orderNo = ""

    override fun initContentView(): Int {
        return R.layout.fragment_pay_result
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initParam() {
        orderNo = arguments?.getString(AppConstants.BundleKey.ORDER_NO_KEY).toString()
    }

    override fun initData() {
        viewModel.tvTitle.set("查询中...")
        if (ObjectUtils.isEmpty(orderNo)) {
            //订单为空说明是从微信跳过来的，没有带orderNo，先查询缴费列表取出第一个订单no，在轮训查出缴费结果
            viewModel.getPayRecordList()
        } else {
            viewModel.queryPayResult(orderNo)
        }
    }

    override fun initViewObservable() {
        viewModel.uc.onSetTitleEvent.observe(this, {
            viewModel.tvTitle.set(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (viewModel.resultType.get() == AppConstants.Constants.PAY_FAIL_TYPE) {
            startContainerActivity(AppConstants.Router.Park.F_PAY_RECORDS)
        }
    }

}