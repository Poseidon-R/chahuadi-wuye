package com.czl.module_work.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.MembersBean
import com.czl.base.data.bean.TakeCareBean
import com.czl.base.event.LiveBusCenter
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MembersSelectAdapter
import com.czl.module_work.adapter.MyDispatchDeviceAdapter
import com.czl.module_work.adapter.MyPatrolDispatchDeviceAdapter
import com.czl.module_work.databinding.WorkFragmentPatrolDispatchDetailBinding
import com.czl.module_work.databinding.WorkFragmentTakeCareDispatchDetailBinding
import com.czl.module_work.view.OrderReceivingPop
import com.czl.module_work.view.TransferFormPop
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.czl.module_work.viewModel.PatrolDispatchDetailViewModel
import com.czl.module_work.viewModel.TakeCareDispatchDetailViewModel
import com.lxj.xpopup.XPopup


/**
 * 创建日期：2021/12/27  17:49
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_PATROL_DISPATCH_DETAIL)
class PatrolDispatchDetailFragment :
    BaseFragment<WorkFragmentPatrolDispatchDetailBinding, PatrolDispatchDetailViewModel>() {

    private lateinit var mAdapter: MyPatrolDispatchDeviceAdapter

    private lateinit var membersAdapter: MembersSelectAdapter

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_patrol_dispatch_detail

    override fun initVariableId(): Int = BR.viewModel

    private var orderId = ""

    override fun initData() {
        viewModel.tvTitle.set("派单详情")
        initAdapter()
        viewModel.getTakeOrderFull(orderId)
    }

    override fun initParam() {
        orderId = arguments?.getString(AppConstants.BundleKey.WORK_ORDER_ID) ?: ""
    }

    private fun initAdapter() {
        mAdapter = MyPatrolDispatchDeviceAdapter()
        membersAdapter = MembersSelectAdapter()
        binding.ryCommon.apply {
            adapter = mAdapter
        }
        binding.membersList.apply {
            adapter = membersAdapter
        }
    }

    override fun initViewObservable() {
        viewModel.uc.loadDeviceEvent.observe(this, { data ->
            mAdapter.addData(data)
        })
        viewModel.uC.finishEvent.observe(this, {
            back()
        })
        LiveBusCenter.observeMembersSelectEvent(this, {
            if (it.members.isEmpty()) return@observeMembersSelectEvent
            viewModel.setMembers(it.members[0])
            membersAdapter.setNewInstance(it.members as MutableList<MembersBean>)
        })
    }
}