package com.czl.module_work.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MyOrderAdapter
import com.czl.module_work.databinding.WorkFragmentAppointmentRecordBinding
import com.czl.module_work.view.FilterOrderPop
import com.czl.module_work.viewModel.AppointmentRecordViewModel
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.lxj.xpopup.XPopup


/**
 * 创建日期：2022/1/4  16:30
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_DEVICE_APPOINTMENT_RECORD)
class AppointmentRecordFragment :
    BaseFragment<WorkFragmentAppointmentRecordBinding, AppointmentRecordViewModel>() {

    private lateinit var mAdapter: MyOrderAdapter

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_appointment_record

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("访客记录")
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
            mAdapter.addData(data)
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
            val type = if (position == 0) {
                MyOrderDetailViewModel.FILTER_TYPE_WAITING
            } else if (position == 1) {
                MyOrderDetailViewModel.FILTER_TYPE_PENDING
            } else {
                MyOrderDetailViewModel.FILTER_TYPE_COMPLETE
            }
            val orderType = if (position == 0) {
                MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE
            } else if (position == 1) {
                MyOrderDetailViewModel.FILTER_ORDER_TYPE_CHECK
            } else {
                MyOrderDetailViewModel.FILTER_ORDER_TYPE_WARNING
            }
            startContainerActivity(AppConstants.Router.Work.F_WORK_MY_ORDER_DETAIL, Bundle().apply {
                putInt(AppConstants.BundleKey.WORK_ORDER_STATUS, type)
                putInt(AppConstants.BundleKey.WORK_ORDER_TYPE, orderType)
            })
        }
        binding.ryCommon.apply {
            adapter = mAdapter
        }
    }
}