package com.czl.module_work.fragment

import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkFragmentDevicePartolBinding
import com.czl.module_work.databinding.WorkFragmentDeviceTakeCareBinding
import com.czl.module_work.viewModel.DevicePatrolViewModel
import com.czl.module_work.viewModel.DeviceTakeCareViewModel


/**
 * 创建日期：2021/12/27  10:49
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_DEVICE_PATROL)
class DevicePatrolFragment :
    BaseFragment<WorkFragmentDevicePartolBinding, DevicePatrolViewModel>() {

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_device_partol

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("设备巡检")
    }
}