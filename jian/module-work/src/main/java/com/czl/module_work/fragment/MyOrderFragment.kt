package com.czl.module_work.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.ToastHelper
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MyOrderAdapter
import com.czl.module_work.databinding.WorkFragmentMyOrderBinding
import com.czl.module_work.view.FilterOrderPop
import com.czl.module_work.view.OrderReceivingPop
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.czl.module_work.viewModel.MyOrderViewModel
import com.lxj.xpopup.XPopup


/**
 * 创建日期：2021/12/28  14:45
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_MY_ORDER)
class MyOrderFragment :
    BaseFragment<WorkFragmentMyOrderBinding, MyOrderViewModel>() {

    private lateinit var mAdapter: MyOrderAdapter

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_my_order

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("我的工单")
        initAdapter()
        loadData()
    }

    override fun initViewObservable() {
        viewModel.uc.clearOrderEvent.observe(this, {
            mAdapter.setNewInstance(mutableListOf())
        })
        viewModel.uc.loadOrderEvent.observe(this, { data ->
            binding.smartCommon.finishRefresh(true)
            binding.smartCommon.finishLoadMore()
            data?:return@observe
            mAdapter.addData(data)
        })
        LiveBusCenter.observeWorkOrderRefreshEvent(this, {
            loadData()
        })
        viewModel.uc.showFilter.observe(this, {
            val pop = FilterOrderPop(requireContext())
            pop.setFilterResultListener {
                if (it != 0)
                    viewModel.setFilter(it)
            }
            XPopup.Builder(context)
                .asCustom(pop)
                .show()
        })
    }

    private fun loadData() {
        binding.smartCommon.autoRefresh()
    }

    private fun initAdapter() {
        mAdapter = MyOrderAdapter()
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val data = mAdapter.data.get(position)
            val type = if (data.orderState == 1) {
                MyOrderDetailViewModel.FILTER_TYPE_WAITING
            } else if (data.orderState == 2) {
                MyOrderDetailViewModel.FILTER_TYPE_PENDING
            } else {
                MyOrderDetailViewModel.FILTER_TYPE_COMPLETE
            }
            val orderType = if (data.orderTypeId == 1) {
                MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE
            } else if (data.orderTypeId == 3) {
                MyOrderDetailViewModel.FILTER_ORDER_TYPE_CHECK
            } else if (data.orderTypeId == 4) {
                MyOrderDetailViewModel.FILTER_ORDER_TYPE_WARNING
            } else {
                ToastHelper.showNormalToast("其他工单无法查看!")
                return@setOnItemClickListener
            }
            startContainerActivity(AppConstants.Router.Work.F_WORK_MY_ORDER_DETAIL, Bundle().apply {
                putInt(AppConstants.BundleKey.WORK_ORDER_STATUS, type)
                putInt(AppConstants.BundleKey.WORK_ORDER_TYPE, orderType)
                putInt(AppConstants.BundleKey.WORK_ORDER_ID, mAdapter.getItem(position).orderId)
            })
        }
        binding.ryCommon.apply {
            adapter = mAdapter
        }
    }


}