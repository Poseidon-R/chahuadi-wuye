package com.czl.module_work.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkFragmentDeviceInspectionBinding
import com.czl.module_work.viewModel.DeviceInspectionViewModel

@Route(path = AppConstants.Router.Work.F_WORK_DEVICE_INSPECTION)
class DeviceInspectionFragment :
    BaseFragment<WorkFragmentDeviceInspectionBinding, DeviceInspectionViewModel>() {
    override fun useBaseLayout(): Boolean = false
    override fun initContentView(): Int = R.layout.work_fragment_device_inspection

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("设备巡检")
    }
}