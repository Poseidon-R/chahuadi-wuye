package com.czl.module_main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.DayModeUtil
import com.czl.base.util.DialogHelper
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.databinding.FragmentHomeBinding
import com.czl.module_main.listener.BindCarListener
import com.czl.module_main.listener.DeleteCarListener
import com.czl.module_main.viewmodel.HomeViewModel
import com.gyf.immersionbar.ImmersionBar

@Route(path = AppConstants.Router.Main.F_HOME)
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun initContentView(): Int {
        return R.layout.fragment_home
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun onSupportVisible() {
        ImmersionBar.with(this).fitsSystemWindows(true)
            .statusBarDarkFont(!DayModeUtil.isNightMode(requireContext())).init()
    }

    override fun initData() {

//        binding.viewBindCars.setDeleteListener(object : DeleteCarListener {
//            override fun deleteCar(vehicleNo: String) {
//                DialogHelper.showBaseDialog(requireContext(), "提示", "确定删除吗？") {
//                    viewModel.deleteQueryCar(vehicleNo)
//                }
//            }
//
//            override fun scanCar(vehicleNo: String) {
//                startContainerActivity(AppConstants.Router.Park.F_PARK_DETAIL, Bundle().apply {
//                    putString(AppConstants.BundleKey.VEHICLE_NO_KEY, vehicleNo)
//                })
//            }
//        })
//        binding.viewCarNumber.setBindCarListener(object : BindCarListener {
//            override fun bindCar(vehicleNo: String?) {
//                if (vehicleNo.isNullOrEmpty()) {
//                    showNormalToast("请输入车牌号")
//                    return
//                }
//            }
//
//            override fun scanCarDetail(vehicleNo: String?) {
//                if (vehicleNo.isNullOrEmpty()) {
//                    showNormalToast("请输入车牌号")
//                    return
//                }
//                startContainerActivity(AppConstants.Router.Park.F_PARK_DETAIL, Bundle().apply {
//                    putString(AppConstants.BundleKey.VEHICLE_NO_KEY, vehicleNo)
//                })
//            }
//
//        })
//        viewModel.getMyQueryHistory()
    }

//    override fun onBackPressedSupport(): Boolean {
        //处理按返回建隐藏车牌号键盘
//        return binding.viewCarNumber.keyboardDismiss()
//    }


    override fun setFragmentResult(resultCode: Int, bundle: Bundle?) {
//        Log.e("test","responsecode:"+resultCode+"data:"+bundle.toString())
//        when{
//            bundle?.getBoolean("hidekeyboard") == true ->binding.viewCarNumber.keyboardDismiss()}
    }

    override fun initViewObservable() {
//        viewModel.uc.loadCompleteEvent.observe(this, {
//            binding.viewBindCars.setList(it)
//        })
        viewModel.uc.deleteResultEvent.observe(this, {
            viewModel.getMyQueryHistory()
        })

        LiveBusCenter.observeRefreshHisCompleteEvent(this,{
            viewModel.getMyQueryHistory()
        })
    }

    override fun useBaseLayout(): Boolean {
        return false
    }
}