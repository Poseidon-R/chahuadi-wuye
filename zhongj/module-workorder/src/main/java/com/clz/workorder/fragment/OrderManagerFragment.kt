package com.clz.workorder.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.clz.workorder.BR
import com.clz.workorder.R
import com.clz.workorder.databinding.FragmentOrderManagerBinding
import com.clz.workorder.viewmodel.OrderManagerViewModel
import com.czl.base.adapter.ViewPagerFmAdapter
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.route.RouteCenter
import me.yokeyword.fragmentation.SupportFragment

/**
 *
 * @Description:工单管理
 * @Author: XCH
 * @CreateDate: 2021/12/14 10:07
 */
@Route(path = AppConstants.Router.Order.F_ORDER_MANAGER)
class OrderManagerFragment : BaseFragment<FragmentOrderManagerBinding, OrderManagerViewModel>() {
    override fun initContentView(): Int {
        return R.layout.fragment_order_manager
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("工单管理")

        var fragmentOne =
            RouteCenter.navigate(AppConstants.Router.Order.F_ORDER_LIST, Bundle().apply {
                putString(AppConstants.BundleKey.PROPERTY_STATE, "0")
            }) as SupportFragment
        var fragmentTwo =
            RouteCenter.navigate(AppConstants.Router.Order.F_ORDER_LIST, Bundle().apply {
                putString(AppConstants.BundleKey.PROPERTY_STATE, "1")
            }) as SupportFragment
        var fragmentThree =
            RouteCenter.navigate(AppConstants.Router.Order.F_ORDER_LIST, Bundle().apply {
                putString(AppConstants.BundleKey.PROPERTY_STATE, "2")
            }) as SupportFragment
        var fragmentFour =
            RouteCenter.navigate(AppConstants.Router.Order.F_ORDER_LIST, Bundle().apply {
                putString(AppConstants.BundleKey.PROPERTY_STATE, "3")
            }) as SupportFragment
        var fragments = arrayListOf(fragmentOne, fragmentTwo, fragmentThree, fragmentFour)
        binding.viewPager.apply {
            adapter = ViewPagerFmAdapter(childFragmentManager, lifecycle, fragments)
        }
    }

    override fun initViewObservable() {
        viewModel.uc.tabChangeLiveEvent.observe(this, {
            binding.viewPager.setCurrentItem(it, false)
        })
        viewModel.uc.pageChangeLiveEvent.observe(this, {
            binding.tabLayout.currentTab = it
        })
    }
}