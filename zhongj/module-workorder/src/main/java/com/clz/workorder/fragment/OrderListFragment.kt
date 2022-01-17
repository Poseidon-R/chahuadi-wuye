package com.clz.workorder.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.NumberUtils
import com.clz.workorder.adapter.OrderAdapter
import com.clz.workorder.databinding.FragmentOrderListBinding
import com.clz.workorder.viewmodel.OrderListViewModel
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.clz.workorder.BR
import com.clz.workorder.R

/**
 *
 * @Description:
 * @Author: XCH
 * @CreateDate: 2021/12/14 10:09
 */
@Route(path = AppConstants.Router.Order.F_ORDER_LIST)
class OrderListFragment : BaseFragment<FragmentOrderListBinding, OrderListViewModel>() {

    private lateinit var mAdapter: OrderAdapter

    override fun initContentView(): Int {
        return R.layout.fragment_order_list
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        if (arguments != null) {
            viewModel.propertyState =
                requireArguments().getString(AppConstants.BundleKey.PROPERTY_STATE).toString()
        }
        initAdapter()
        reload()
    }

    private fun initAdapter() {
        mAdapter = OrderAdapter(this)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
    }

    override fun initViewObservable() {
        viewModel.uc.loadCompleteEvent.observe(this, { data ->
            handleRecyclerviewData(
                data == null,
                data?.models as MutableList<*>?,
                mAdapter,
                binding.ryCommon,
                binding.smartCommon,
                viewModel.currentPage,
                data?.pageIndex == data?.pages
            )
        })
        LiveBusCenter.observePayResultEvent(this, {
            when (it.payType) {
                AppConstants.Constants.PAY_SUCCESS_TYPE -> {
                    reload()
                }
                AppConstants.Constants.PAY_FAIL_TYPE -> {
                    back()
                }
            }
        })
    }

    override fun reload() {
        super.reload()
        binding.smartCommon.autoRefresh()
    }

    override fun useBaseLayout(): Boolean {
        return false
    }
}