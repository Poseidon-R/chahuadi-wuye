package com.czl.module_car.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.CarItem
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.DialogHelper
import com.czl.module_car.BR
import com.czl.module_car.R
import com.czl.module_car.adapter.MyCarAdapter
import com.czl.module_car.databinding.ActivityMyCarBinding
import com.czl.module_car.viewmodel.MyCarViewModel
import com.lxj.xpopup.util.XPopupUtils

@Route(path = AppConstants.Router.Car.F_MYCAR)
class MyCarFragment : BaseFragment<ActivityMyCarBinding, MyCarViewModel>() {

    private lateinit var mAdapter: MyCarAdapter

    override fun initContentView(): Int {
        return R.layout.activity_my_car
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("我的车辆")
        viewModel.toolbarRightText.set("编辑")
        initAdapter()
        loadData()
    }

    override fun initViewObservable() {
        viewModel.uc.loadCompleteEvent.observe(this, { data ->
            binding.smartCommon.finishRefresh(true)
            mAdapter.setNewInstance(data as MutableList<CarItem>?)
        })
        viewModel.uc.touchEditEvent.observe(this, { isEdit ->
            mAdapter.checkEditStatus(isEdit)
        })
        viewModel.uc.deleteCarEvent.observe(this, {
            DialogHelper.showBaseDialog(requireContext(), "提示", "确定删除吗？") {
                viewModel.deleteCarList()
            }
        })
        LiveBusCenter.observeAddCarCompleteEvent(this) {
            viewModel.setEdit(false)
            loadData()
        }
    }

    private fun loadData() {
        binding.smartCommon.autoRefresh()
    }

    private fun initAdapter() {
        mAdapter = MyCarAdapter(this)
        binding.ryCommon.apply {
            adapter = mAdapter
        }
    }
}