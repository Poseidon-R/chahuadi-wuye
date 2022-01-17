package com.czl.module_work.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.PatrolBean
import com.czl.base.data.bean.TakeCareBean
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.event.LiveBusCenter
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.*
import com.czl.module_work.databinding.WorkFragmentPatrolAuditBinding
import com.czl.module_work.databinding.WorkFragmentTakeCareAuditBinding
import com.czl.module_work.databinding.WorkFragmentTakeCareDispatchDetailBinding
import com.czl.module_work.viewModel.*
import com.google.gson.Gson


/**
 * 创建日期：2021/12/28  10:26
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_PATROL_AUDIT)
class PatrolAuditFragment :
    BaseFragment<WorkFragmentPatrolAuditBinding, PatrolAuditViewModel>() {

    private lateinit var mAdapter: PatrolAuditOrderAdapter

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_patrol_audit

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("工单确认")
        initAdapter()
        viewModel.getWaitingOrders()
    }

    override fun initViewObservable() {
        viewModel.uc.loadRoomEvent.observe(this, { data ->
            binding.smartCommon.finishRefresh(true)
            binding.smartCommon.finishLoadMore()
            mAdapter.addData(data as MutableList<PatrolBean.Data>)
        })
        viewModel.uc.clearOrderEvent.observe(this, { data ->
            mAdapter.setNewInstance(mutableListOf())
        })
        viewModel.uc.changTab.observe(this, Observer {
            if (viewModel.filterType.get() == PatrolAuditViewModel.FILTER_TYPE_COMPLETE) {
                mAdapter.setCheckMode(false)
            } else {
                mAdapter.setCheckMode(true)
            }
        })
        viewModel.uc.batchAuditEvent.observe(this, {
            startContainerActivity(
                AppConstants.Router.Work.F_WORK_TAKE_CARE_BATCH_DISPATCH,
                Bundle().apply {
                    putInt(
                        AppConstants.BundleKey.WORK_BATCH_ORDER_TYPE,
                        TakeCareBatchDispatchViewModel.ORDER_TYPE_PATROL
                    )
                    putInt(
                        AppConstants.BundleKey.WORK_BATCH_TYPE,
                        TakeCareBatchDispatchViewModel.ORDER_BATCH_TYPE_AUDIT
                    )
                    putString(
                        AppConstants.BundleKey.WORK_BATCH_ORDER_IDS,
                        Gson().toJson(mAdapter.getSelectOrders())
                    )
                })
        })
        LiveBusCenter.observeAuditRefreshEvent(this, {
            viewModel.refresh()
        })
    }

    private fun loadData() {
        binding.smartCommon.autoRefresh()
    }

    private fun initAdapter() {
        mAdapter = PatrolAuditOrderAdapter()
        mAdapter.setCheckMode(true)
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val data = mAdapter.data.get(position)
            val type = if (data.orderState == "1") {
                startContainerActivity(
                    AppConstants.Router.Work.F_WORK_PATROL_AUDIT_DETAIL,
                    Bundle().apply {
                        putString(
                            AppConstants.BundleKey.WORK_ORDER_ID,
                            mAdapter.data.get(position).orderId
                        )
                    })
                return@setOnItemClickListener
            } else if (data.orderState == "2") {
                MyOrderDetailViewModel.FILTER_TYPE_PENDING
            } else if (data.orderState == "3") {
                MyOrderDetailViewModel.FILTER_TYPE_COMPLETE
            } else {
                MyOrderDetailViewModel.FILTER_TYPE_COMPLETE
            }
            val orderType = MyOrderDetailViewModel.FILTER_ORDER_TYPE_CHECK
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