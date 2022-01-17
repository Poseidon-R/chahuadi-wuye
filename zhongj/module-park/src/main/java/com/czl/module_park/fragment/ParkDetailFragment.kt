package com.czl.module_park.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.czl.module_park.BR
import com.czl.module_park.R
import com.czl.module_park.databinding.ActivityParkDetailBinding
import com.czl.module_park.viewmodel.ParkDetailViewModel

@Route(path = AppConstants.Router.Park.F_PARK_DETAIL)
class ParkDetailFragment : BaseFragment<ActivityParkDetailBinding, ParkDetailViewModel>() {

    private var vehicleNo: String = ""

    override fun initContentView(): Int {
        return R.layout.activity_park_detail
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("停车详情")

        vehicleNo = arguments?.getString(AppConstants.BundleKey.VEHICLE_NO_KEY).toString()
        viewModel.parkCharging(vehicleNo)
    }

    override fun initViewObservable() {
        viewModel.uc.payEvent.observe(this, {
            startContainerActivity(AppConstants.Router.Pay.F_PAY_DETAIL, Bundle().apply {
                putString(AppConstants.BundleKey.PAY_ORDER_KEY, it)
            })
        })

        LiveBusCenter.observePayResultEvent(this, {
            when (it.payType) {
                AppConstants.Constants.PAY_SUCCESS_TYPE, AppConstants.Constants.PAY_FAIL_TYPE -> {
                    back()
                }
            }
        })
    }

    override fun onStop() {
        LiveBusCenter.postRefreshHisCompleteEvent("")
        super.onStop()
    }
}