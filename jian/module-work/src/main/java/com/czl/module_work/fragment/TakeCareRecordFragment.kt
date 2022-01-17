package com.czl.module_work.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.TakeCareBean
import com.czl.base.data.bean.TakeCareRecordBean
import com.czl.base.data.bean.WorkOrderBean
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.TakeCareRecordAdapter
import com.czl.module_work.databinding.WorkFragmentTakeCareRecordBinding
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.czl.module_work.viewModel.TakeCareRecordViewModel


/**
 * 创建日期：2021/12/27  15:27
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_TAKE_CARE_RECORD)
class TakeCareRecordFragment :
    BaseFragment<WorkFragmentTakeCareRecordBinding, TakeCareRecordViewModel>() {
    private lateinit var mAdapter: TakeCareRecordAdapter

    override fun useBaseLayout(): Boolean = false;


    override fun initContentView(): Int = R.layout.work_fragment_take_care_record

    override fun initVariableId(): Int = BR.viewModel


    override fun initData() {
        viewModel.tvTitle.set("保养记录")
        initAdapter()
        viewModel.refresh()
    }

    override fun initViewObservable() {
        viewModel.uc.clearOrderEvent.observe(this, {
            mAdapter.setNewInstance(mutableListOf())
        })
        viewModel.uc.loadOrderEvent.observe(this, { data ->
            binding.smartCommon.finishRefresh(true)
            mAdapter.setNewInstance(data as MutableList<TakeCareRecordBean.Data>?)
        })
    }

    private fun initAdapter() {
        mAdapter = TakeCareRecordAdapter()
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
                        mAdapter.data[position].orderId
                    )
                })
        }
    }
}