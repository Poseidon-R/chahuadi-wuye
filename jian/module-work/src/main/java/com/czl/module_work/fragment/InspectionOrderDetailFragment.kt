package com.czl.module_work.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MyDispatchDeviceAdapter
import com.czl.module_work.databinding.WorkFragmentInspectionOrderDetailBinding
import com.czl.module_work.viewModel.InspectionOrderDetailViewMode

@Route(path = AppConstants.Router.Work.F_WORK_INSPECTION_ORDER_DETAIL)
class InspectionOrderDetailFragment: BaseFragment<WorkFragmentInspectionOrderDetailBinding, InspectionOrderDetailViewMode>() {

    private lateinit var mAdapter: MyDispatchDeviceAdapter

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_inspection_order_detail

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("工单详情")
        initAdapter()
    }

    private fun initAdapter() {
        mAdapter = MyDispatchDeviceAdapter()
        binding.ryCommon.apply {
            adapter = mAdapter
        }
//        mAdapter.addData("云台2(未巡检)")
//        mAdapter.addData("云台2(未巡检)")
//        mAdapter.addData("云台2(未巡检)")
//        mAdapter.addData("云台2(未巡检)")
//        mAdapter.addData("云台2(未巡检)")

    }
}