package com.czl.module_work.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.MembersBean
import com.czl.base.event.LiveBusCenter
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MembersSelectAdapter
import com.czl.module_work.adapter.MyDispatchDeviceAdapter
import com.czl.module_work.adapter.MyPatrolDispatchDeviceAdapter
import com.czl.module_work.databinding.WorkFragmentPatrolAuditDetailBinding
import com.czl.module_work.databinding.WorkFragmentTakeCareAuditBinding
import com.czl.module_work.databinding.WorkFragmentTakeCareAuditDetailBinding
import com.czl.module_work.viewModel.PatrolAuditDetailViewModel
import com.czl.module_work.viewModel.TakeCareAuditDetailViewModel


/**
 * 创建日期：2021/12/28  11:01
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_PATROL_AUDIT_DETAIL)
class PatrolAuditDetailFragment :
    BaseFragment<WorkFragmentPatrolAuditDetailBinding, PatrolAuditDetailViewModel>() {

    private lateinit var mAdapter: MyPatrolDispatchDeviceAdapter

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_patrol_audit_detail

    override fun initVariableId(): Int = BR.viewModel

    private var orderId = ""

    override fun initData() {
        viewModel.tvTitle.set("工单审核")
        initAdapter()
        viewModel.getTakeOrderFull(orderId)
    }

    override fun initParam() {
        orderId = arguments?.getString(AppConstants.BundleKey.WORK_ORDER_ID) ?: ""
    }

    private fun initAdapter() {
        mAdapter = MyPatrolDispatchDeviceAdapter()
        binding.ryCommon.apply {
            adapter = mAdapter
        }
    }

    override fun initViewObservable() {
        viewModel.uc.loadDeviceEvent.observe(this, { data ->
            mAdapter.addData(data)
        })
        viewModel.uC.finishEvent.observe(this, {
            back()
        })
    }
}