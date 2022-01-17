package com.czl.module_user.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.RoomBean
import com.czl.module_user.adapter.MyRoomAdapter
import com.czl.module_user.databinding.ActivityMyRoomsBinding
import com.czl.module_user.viewmodel.MyRoomViewModel
import com.czl.module_user.R
import com.czl.module_user.BR

@Route(path = AppConstants.Router.Room.F_MYROOM)
class MyRoomFragment: BaseFragment<ActivityMyRoomsBinding, MyRoomViewModel>() {
    private lateinit var mAdapter: MyRoomAdapter
    override fun initContentView(): Int {
        return R.layout.activity_my_rooms
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("房间号")
        initAdapter()
        loadData()
    }

    override fun initViewObservable() {
        viewModel.uc.loadRoomEvent.observe(this,{
            data->binding.smartCommon.finishRefresh(true)
            mAdapter.setNewInstance(data as MutableList<RoomBean>?)
        })
    }
    private fun loadData(){
        binding.smartCommon.autoRefresh()
    }
    private fun initAdapter(){
        mAdapter= MyRoomAdapter(this)
        binding.ryCommon.apply {
            adapter=mAdapter
        }
    }
}