package com.czl.module_work.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.MembersBean
import com.czl.base.data.bean.PatrolOrderDetailBean
import com.czl.base.data.bean.UserInfo
import com.czl.base.data.bean.WorkOrderDetailBean
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.ToastHelper
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MyDispatchDeviceAdapter
import com.czl.module_work.adapter.MyPatrolDispatchDeviceAdapter
import com.czl.module_work.adapter.OrderDetailPhotoAdapter
import com.czl.module_work.databinding.WorkFragmentMyOrderDetailBinding
import com.czl.module_work.view.OrderReceivingPop
import com.czl.module_work.view.PatrolCertificatePop
import com.czl.module_work.view.TransferFormPop
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.google.gson.Gson
import com.lxj.xpopup.XPopup


/**
 * 创建日期：2021/12/27  17:49
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_MY_ORDER_DETAIL)
class MyOrderDetailFragment :
    BaseFragment<WorkFragmentMyOrderDetailBinding, MyOrderDetailViewModel>() {

    var type = MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE

    var status = MyOrderDetailViewModel.FILTER_TYPE_WAITING

    private lateinit var mAdapter: MyDispatchDeviceAdapter

    private lateinit var mPatrolAdapter: MyPatrolDispatchDeviceAdapter

    private lateinit var mPhotoAdapter: OrderDetailPhotoAdapter

    private lateinit var transferFormPop: TransferFormPop

    private lateinit var orderReceivingPop: OrderReceivingPop

    private lateinit var patrolCetificatePop: PatrolCertificatePop

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_my_order_detail

    override fun initVariableId(): Int = BR.viewModel

    private var orderId = ""

    override fun initData() {
        viewModel.tvTitle.set("工单详情")
        viewModel.filterType.set(status)
        viewModel.filterOrderType.set(type)
        initAdapter()
        refresh()
    }

    private fun refresh() {
        if (type == MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE) {
            viewModel.timeTag.set("保养时长")
            viewModel.btnText.set("完成保养")
            viewModel.getTakeOrderFull(orderId)
        }
        if (type == MyOrderDetailViewModel.FILTER_ORDER_TYPE_CHECK) {
            viewModel.timeTag.set("巡检时长")
            viewModel.btnText.set("完成巡检")
            viewModel.getPatrolFull(orderId)
        }
        if (type == MyOrderDetailViewModel.FILTER_ORDER_TYPE_WARNING) {
            viewModel.btnText.set("完成提交")
            viewModel.getAlarmFull(orderId)
        }
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
        orderId = arguments?.getInt(AppConstants.BundleKey.WORK_ORDER_ID, 0).toString()
    }


    override fun initViewObservable() {
        transferFormPop = TransferFormPop(requireContext())
        orderReceivingPop = OrderReceivingPop(requireContext())
        patrolCetificatePop = PatrolCertificatePop(requireContext())
        patrolCetificatePop.setPatrolSaveListener(object : PatrolCertificatePop.PatrolSaveListener {
            override fun save(
                id: String,
                status: String,
                task: List<PatrolOrderDetailBean.StandardList>
            ) {
                viewModel.checkTaskRegister(id, status, task)
            }
        })
        orderReceivingPop.setOrderReceivingListener(object :
            OrderReceivingPop.OrderReceivingListener {
            override fun confirm(member: MembersBean?, note: String, serviceTime: String) {
                viewModel.receivingOrder(member, note, orderId, serviceTime)
            }
        })
        transferFormPop.setOrderTransferListener(object :
            TransferFormPop.OrderTransferListener {
            override fun confirm(member: MembersBean, note: String) {
                viewModel.transferOrder(member, note, orderId)
            }
        })

        viewModel.uc.doTransferFormClick.observe(this, {
            XPopup.Builder(context)
                .asCustom(transferFormPop)
                .show()
        })
        viewModel.uc.doOrderReceivingClick.observe(this, {
            XPopup.Builder(context)
                .asCustom(orderReceivingPop)
                .show()
        })
        viewModel.uc.loadDeviceEvent.observe(this, { data ->
            mAdapter.setNewInstance(data as MutableList<WorkOrderDetailBean.TaskList>?)
        })
        viewModel.uc.loadPatrolDeviceEvent.observe(this, { data ->
            mPatrolAdapter.setNewInstance(data as MutableList<PatrolOrderDetailBean.TaskList>?)
        })
        viewModel.uc.loadImageEvent.observe(this, { data ->
            mPhotoAdapter.setNewInstance(data as MutableList<String>?)
        })
        viewModel.uc.uploadCertificate.observe(this, {
            if (mAdapter.getCheckIds().isEmpty()) {
                ToastHelper.showNormalToast("请选择登记设备!")
                return@observe
            }
            startContainerActivity(
                AppConstants.Router.Work.F_WORK_UPLOAD_CERTIFICATE,
                Bundle().apply {
                    putString(
                        AppConstants.BundleKey.WORK_ORDER_TASK_IDS,
                        Gson().toJson(mAdapter.getCheckIds())
                    )
                })
        })

        viewModel.uc.uploadAlarmCertificate.observe(this, {
            startContainerActivity(
                AppConstants.Router.Work.F_WORK_UPLOAD_CERTIFICATE,
                Bundle().apply {
                    putString(
                        AppConstants.BundleKey.WORK_ORDER_ALARM_ID,
                        orderId
                    )
                })
        })
        LiveBusCenter.observeWorkOrderDetailRefreshEvent(this, {
            refresh()
        })
        LiveBusCenter.observeMembersSelectEvent(this, {
            if (it.members.isEmpty()) return@observeMembersSelectEvent
            if (orderReceivingPop.isShow) {
                orderReceivingPop.setMember(it.members[0])
            }
            if (transferFormPop.isShow) {
                transferFormPop.setMember(it.members[0])
            }
        })
    }

    private fun initAdapter() {
        mAdapter = MyDispatchDeviceAdapter()
        mPatrolAdapter = MyPatrolDispatchDeviceAdapter()
        mPhotoAdapter = OrderDetailPhotoAdapter()
        binding.ryCommon.apply {
            adapter = mAdapter
        }
        binding.ryPatrolCommon.apply {
            adapter = mPatrolAdapter
        }
        binding.ryPhoto.apply {
            adapter = mPhotoAdapter
        }
        binding.tvAllCheck.setOnClickListener {

            if (isAllCheck()) {
                mAdapter.clearCheck()
                binding.tvAllCheck.isSelected = false
            } else {
                mAdapter.allCheck()
                binding.tvAllCheck.isSelected = true
            }
        }
        mPatrolAdapter.setOnItemClickListener { adapter, view, position ->
            val data = mPatrolAdapter.data.get(position)
            patrolCetificatePop.setTask(data)

            XPopup.Builder(context)
                .asCustom(patrolCetificatePop)
                .show()
        }
        mAdapter.setCheckMode(status == MyOrderDetailViewModel.FILTER_TYPE_PENDING)
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

    private fun isAllCheck(): Boolean {
        val selectPos = mAdapter.getSelectPos()
        return selectPos.size == mAdapter.data.size
    }
}