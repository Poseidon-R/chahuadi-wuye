package com.czl.module_work.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.MembersBean
import com.czl.base.event.LiveBusCenter
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MembersSelectAdapter
import com.czl.module_work.adapter.MyOrderAdapter
import com.czl.module_work.databinding.WorkFragmentMembersSelectBinding
import com.czl.module_work.viewModel.MembersSelectViewModel
import com.jeremyliao.liveeventbus.LiveEventBus


/**
 * 创建日期：2021/12/28  15:27
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_MEMBERS_SELECT)
class MembersSelectFragment :
    BaseFragment<WorkFragmentMembersSelectBinding, MembersSelectViewModel>() {

    private lateinit var mAdapter: MembersSelectAdapter

    override fun useBaseLayout(): Boolean = false;

    override fun reload() {
        super.reload()
    }

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("人员选择")
        initAdapter()
        viewModel.getUserRooms()
    }

    override fun initViewObservable() {
        viewModel.uc.loadRoomEvent.observe(this, { data ->
            mAdapter.setNewInstance(data as MutableList<MembersBean>?)
        })
    }

    private fun initAdapter() {
        mAdapter = MembersSelectAdapter()
        binding.ryCommon.apply {
            adapter = mAdapter
        }
        binding.tvSubmit.setOnClickListener {
            val members = mAdapter.getSelectMembers()
            if (!members.isEmpty()) {
                LiveBusCenter.postMembersSelectEvent(members)
            }
            back()
        }
    }

    override fun initContentView(): Int = R.layout.work_fragment_members_select
}