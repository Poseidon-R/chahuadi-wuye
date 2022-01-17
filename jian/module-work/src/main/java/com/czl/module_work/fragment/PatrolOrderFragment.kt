package com.czl.module_work.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.PatrolBean
import com.czl.base.data.bean.TakeCareBean
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.PatrolOrderAdapter
import com.czl.module_work.adapter.TakeCareOrderAdapter
import com.czl.module_work.databinding.WorkFragmentPatrolOrderBinding
import com.czl.module_work.view.FilterPop
import com.czl.module_work.viewModel.PatrolOrderViewModel
import com.czl.module_work.viewModel.TakeCareOrderViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition


/**
 * 创建日期：2021/12/27  13:59
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_DEVICE_PATROL_ORDER)
class PatrolOrderFragment :
    BaseFragment<WorkFragmentPatrolOrderBinding, PatrolOrderViewModel>() {

    override fun useBaseLayout(): Boolean = false;

    private lateinit var mAdapter: PatrolOrderAdapter

    override fun initContentView(): Int = R.layout.work_fragment_patrol_order

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("巡检工单")
        initAdapter()
        viewModel.getWaitingOrders()
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
    }

    private fun initAdapter() {
        mAdapter = PatrolOrderAdapter()
        mAdapter.setOnItemClickListener { adapter, view, position ->
            startContainerActivity(AppConstants.Router.Work.F_WORK_INSPECTION_ORDER_DETAIL)
        }
        binding.ryCommon.apply {
            adapter = mAdapter
        }
    }
}