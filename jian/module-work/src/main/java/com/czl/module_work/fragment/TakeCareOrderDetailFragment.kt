package com.czl.module_work.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MyDispatchDeviceAdapter
import com.czl.module_work.adapter.OrderDetailPhotoAdapter
import com.czl.module_work.databinding.WorkFragmentMyOrderDetailBinding
import com.czl.module_work.databinding.WorkFragmentTakeCareOrderDetailBinding
import com.czl.module_work.databinding.WorkFragmentTakeCareOrderDetailBindingImpl
import com.czl.module_work.view.OrderReceivingPop
import com.czl.module_work.view.TransferFormPop
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.lxj.xpopup.XPopup


/**
 * 创建日期：2021/12/27  17:49
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_TAKE_CARE_ORDER_DETAIL)
class TakeCareOrderDetailFragment :
    BaseFragment<WorkFragmentTakeCareOrderDetailBinding, MyOrderDetailViewModel>() {

    var type = MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE

    var status = MyOrderDetailViewModel.FILTER_TYPE_WAITING

    private lateinit var mAdapter: MyDispatchDeviceAdapter

    private lateinit var mPhotoAdapter: OrderDetailPhotoAdapter

    private var orderId = ""

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_take_care_order_detail

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("工单详情")
        viewModel.filterType.set(status)
        viewModel.filterOrderType.set(type)
        initAdapter()
        viewModel.getTakeOrderFull(orderId)
    }

    override fun initParam() {
        status = arguments?.getInt(
            AppConstants.BundleKey.WORK_ORDER_STATUS,
            MyOrderDetailViewModel.FILTER_TYPE_WAITING
        ) ?: MyOrderDetailViewModel.FILTER_TYPE_WAITING
        type = arguments?.getInt(
            AppConstants.BundleKey.WORK_ORDER_TYPE,
            MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE
        ) ?: MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE

        orderId = arguments?.getString(AppConstants.BundleKey.WORK_ORDER_ID) ?: ""
    }

    override fun initViewObservable() {
        viewModel.uc.doTransferFormClick.observe(this, {
            XPopup.Builder(context)
                .asCustom(TransferFormPop(requireContext()))
                .show()
        })
        viewModel.uc.doOrderReceivingClick.observe(this, {
            XPopup.Builder(context)
                .asCustom(OrderReceivingPop(requireContext()))
                .show()
        })
        viewModel.uc.loadDeviceEvent.observe(this, { data ->
            mAdapter.addData(data)
        })
    }

    private fun initAdapter() {
        mAdapter = MyDispatchDeviceAdapter()
        mPhotoAdapter = OrderDetailPhotoAdapter()
        binding.ryCommon.apply {
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            startContainerActivity(
                AppConstants.Router.Work.F_WORK_RECORD_CERTIFICATE,
                Bundle().apply {
                    putString(
                        AppConstants.BundleKey.WORK_RECORD_IMG,
                        mAdapter.data.get(position).handleImage
                    )
                })
        }

    }
}