package com.czl.module_work.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.WorkOrderBean
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MyOrderAdapter
import com.czl.module_work.databinding.WorkFragmentInspectionOrderBinding
import com.czl.module_work.view.FilterPop
import com.czl.module_work.viewModel.InspectionOrderViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
@Route(path = AppConstants.Router.Work.F_WORK_INSPECTION_ORDER)
class InspectionOrderFragment :
    BaseFragment<WorkFragmentInspectionOrderBinding, InspectionOrderViewModel>() {

    override fun useBaseLayout(): Boolean = false;

    private lateinit var mAdapter: MyOrderAdapter

    override fun initContentView(): Int = R.layout.work_fragment_inspection_order

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("巡检工单")
        initAdapter()
        loadData()
    }

    override fun initViewObservable() {
        viewModel.uc.loadRoomEvent.observe(this, { data ->
            binding.smartCommon.finishRefresh(true)
            mAdapter.setNewInstance(data as MutableList<WorkOrderBean.Data>?)
        })
        viewModel.uc.showFilter.observe(this, {
//            XPopup.Builder(context)
//                .popupPosition(PopupPosition.Right)
//                .asCustom(FilterPop())
//                .show()
        })
    }

    private fun loadData() {
        binding.smartCommon.autoRefresh()
    }

    private fun initAdapter() {
        mAdapter = MyOrderAdapter()
        mAdapter.setOnItemClickListener { adapter, view, position ->
            startContainerActivity(AppConstants.Router.Work.F_WORK_INSPECTION_ORDER_DETAIL)
        }
        binding.ryCommon.apply {
            adapter = mAdapter
        }
    }
}