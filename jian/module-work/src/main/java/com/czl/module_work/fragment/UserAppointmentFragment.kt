package com.czl.module_work.fragment

import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkFragmentDevicePartolBinding
import com.czl.module_work.databinding.WorkFragmentDeviceTakeCareBinding
import com.czl.module_work.databinding.WorkFragmentUserAppointmentBinding
import com.czl.module_work.viewModel.DevicePatrolViewModel
import com.czl.module_work.viewModel.DeviceTakeCareViewModel
import com.czl.module_work.viewModel.UserAppointmentViewModel


/**
 * 创建日期：2021/12/27  10:49
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_DEVICE_USER_APPOINTMENT)
class UserAppointmentFragment :
    BaseFragment<WorkFragmentUserAppointmentBinding, UserAppointmentViewModel>() {

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_user_appointment

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("访客预约")
    }
}