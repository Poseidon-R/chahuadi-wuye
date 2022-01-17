package com.czl.module_service.viewmodel

import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository

class ServiceViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val btnWorkClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK)
    })
    val btnReportClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Report.F_REPORT_SUBMIT)
    })
    val btnDeviceTakeCareClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK_DEVICE_TAKE_CARE)
    })
    val btnDevicePatrolClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK_DEVICE_PATROL)
//        startContainerActivity(AppConstants.Router.Work.F_WORK_DEVICE_INSPECTION)
    })
    val btnWorkOrderCenterClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK_MY_ORDER)
    })
    val btnElectronClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Report.F_REPORT_SUBMIT)
    })
    val btnAppointmentClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK_DEVICE_USER_APPOINTMENT)
    })
}