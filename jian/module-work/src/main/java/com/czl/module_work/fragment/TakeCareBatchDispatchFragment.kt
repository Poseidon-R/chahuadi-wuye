package com.czl.module_work.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.GsonUtils
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.*
import com.czl.base.event.LiveBusCenter
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.BatchDispatchOrderAdapter
import com.czl.module_work.adapter.MembersSelectAdapter
import com.czl.module_work.adapter.MyDispatchDeviceAdapter
import com.czl.module_work.adapter.TakeCareDispatchOrderAdapter
import com.czl.module_work.databinding.WorkFragmentTakeCareBatchDispatchBinding
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.czl.module_work.viewModel.TakeCareBatchDispatchViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * 创建日期：2022/1/5  18:37
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_TAKE_CARE_BATCH_DISPATCH)
class TakeCareBatchDispatchFragment :
    BaseFragment<WorkFragmentTakeCareBatchDispatchBinding, TakeCareBatchDispatchViewModel>() {

    private lateinit var mAdapter: BatchDispatchOrderAdapter

    private lateinit var membersAdapter: MembersSelectAdapter

    var orderType = TakeCareBatchDispatchViewModel.ORDER_TYPE_TAKE_CARE

    var type = TakeCareBatchDispatchViewModel.ORDER_BATCH_TYPE_DISPATCH

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_take_care_batch_dispatch

    override fun initVariableId(): Int = BR.viewModel

//    var orders = mutableListOf<TakeCareDispatchBean.Data>()

    var orderNos = mutableListOf<String>()

    var ids = mutableListOf<String>()

    override fun initData() {
        initAdapter()
        viewModel.batchType.set(type)
        viewModel.orderType.set(orderType)
        viewModel.isTakeCare = orderType == TakeCareBatchDispatchViewModel.ORDER_TYPE_TAKE_CARE
        viewModel.setOrders(ids)
        if (type == TakeCareBatchDispatchViewModel.ORDER_BATCH_TYPE_AUDIT) {
            viewModel.tvTitle.set("批量审核")
            viewModel.btnText.set("批量审核")
        } else {
            viewModel.tvTitle.set("批量派单")
            viewModel.btnText.set("批量派单")
        }
    }

    private fun initAdapter() {
        mAdapter = BatchDispatchOrderAdapter()
        membersAdapter = MembersSelectAdapter()
        binding.recyclerView.apply {
            adapter = mAdapter
        }
        binding.membersList.apply {
            adapter = membersAdapter
        }
        mAdapter.setNewInstance(orderNos)
    }

    override fun initParam() {
        type = arguments?.getInt(
            AppConstants.BundleKey.WORK_BATCH_TYPE,
            MyOrderDetailViewModel.FILTER_TYPE_WAITING
        ) ?: MyOrderDetailViewModel.FILTER_TYPE_WAITING
        orderType = arguments?.getInt(
            AppConstants.BundleKey.WORK_BATCH_ORDER_TYPE,
            MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE
        ) ?: MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE
        val orders = arguments?.getString(
            AppConstants.BundleKey.WORK_BATCH_ORDER_IDS
        ) ?: ""
        if (orderType == MyOrderDetailViewModel.FILTER_ORDER_TYPE_CHECK) {
            val type = object : TypeToken<List<PatrolBean.Data>>() {}.getType();
            val ordersBean: List<PatrolBean.Data> = GsonUtils.fromJson(orders, type)
            this.orderNos.addAll(ordersBean.map {
                it.orderNo
            })
            this.ids.addAll(ordersBean.map {
                it.orderId
            })
        } else {
            val type = object : TypeToken<List<TakeCareDispatchBean.Data>>() {}.getType();
            val ordersBean: List<TakeCareDispatchBean.Data> = GsonUtils.fromJson(orders, type)
            this.orderNos.addAll(ordersBean.map {
                it.orderNo
            })
            this.ids.addAll(ordersBean.map {
                it.orderId
            })
        }
    }


    override fun initViewObservable() {
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