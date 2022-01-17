package com.czl.module_work.viewModel

import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository

class DevicePatrolViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val btnMenu1Click: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK_DEVICE_PATROL_ORDER)
    })


    val btnMenu2Click: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK_PATROL_DISPATCH)
    })

    val btnMenu3Click: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK_PATROL_AUDIT)
    })


}