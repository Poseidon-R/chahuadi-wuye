package com.czl.module_work.viewModel

import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository

class InspectionOrderDetailViewMode(application: MyApplication, model: DataRepository):BaseViewModel<DataRepository>(application, model) {

    val btnConfirmClick: BindingCommand<Void> = BindingCommand(BindingAction {
        finish()
    })
}