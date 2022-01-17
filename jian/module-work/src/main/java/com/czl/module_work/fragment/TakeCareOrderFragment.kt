package com.czl.module_work.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.TakeCareBean
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.TakeCareOrderAdapter
import com.czl.module_work.databinding.WorkFragmentTakeCareOrderBinding
import com.czl.module_work.view.FilterPop
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.czl.module_work.viewModel.TakeCareOrderViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition


/**
 * 创建日期：2021/12/27  13:59
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_TAKE_CARE_ORDER)
class TakeCareOrderFragment :
    BaseFragment<WorkFragmentTakeCareOrderBinding, TakeCareOrderViewModel>() {


    override fun useBaseLayout(): Boolean = false;

    private lateinit var filterPop: FilterPop

    private lateinit var mAdapter: TakeCareOrderAdapter

    override fun initContentView(): Int = R.layout.work_fragment_take_care_order

    override fun initVariableId(): Int = BR.viewModel


    override fun initData() {
        viewModel.tvTitle.set("保养工单")
        initAdapter()
        viewModel.getWaitingOrders()
        filterPop = FilterPop()
        filterPop.setOnResultListener(object : FilterPop.OnTimePickResultListener {
            override fun query(
                startTime: String,
                endTime: String,
                name: String,
                orderNo: String,
                assetsNo: String
            ) {
                viewModel.setFilterParams(startTime, endTime, name, orderNo, assetsNo)
            }
        })
    }

    override fun initViewObservable() {

        viewModel.uc.clearOrderEvent.observe(this, {
            mAdapter.setNewInstance(mutableListOf())
        })
        viewModel.uc.loadOrderEvent.observe(this, { data ->
            binding.smartCommon.finishRefresh(true)
            binding.smartCommon.finishLoadMore()
            mAdapter.addData(data)
        })
        viewModel.uc.showFilter.observe(this, {
            filterPop.show(parentFragmentManager, "")
            filterPop.setFilter(viewModel.filterParams)
        })
    }

    private fun loadData() {
        binding.smartCommon.autoRefresh()
    }

    private fun initAdapter() {
        mAdapter = TakeCareOrderAdapter()
        binding.ryCommon.apply {
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            startContainerActivity(
                AppConstants.Router.Work.F_WORK_TAKE_CARE_ORDER_DETAIL,
                Bundle().apply {
                    putInt(
                        AppConstants.BundleKey.WORK_ORDER_STATUS,
                        MyOrderDetailViewModel.FILTER_ORDER_TYPE_WARNING
                    )
                    putInt(
                        AppConstants.BundleKey.WORK_ORDER_TYPE,
                        MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE
                    )
                    putString(
                        AppConstants.BundleKey.WORK_ORDER_ID,
                        mAdapter.data.get(position).orderId.toString()
                    )
                })
        }
    }
}