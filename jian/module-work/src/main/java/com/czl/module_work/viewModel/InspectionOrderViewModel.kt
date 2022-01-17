package com.czl.module_work.viewModel

import androidx.databinding.ObservableField
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.event.SingleLiveEvent

class InspectionOrderViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {


    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadRoomEvent: SingleLiveEvent<List<WorkOrderBean.Data>> = SingleLiveEvent()

        val showFilter: SingleLiveEvent<String> = SingleLiveEvent()
    }

    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
//        getMonthCardList()
    })

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        getUserRooms()
    })


    val filterType = ObservableField(FILTER_TYPE_WAITING)

    val btnWaitingClick: BindingCommand<Void> = BindingCommand(BindingAction {
        filterType.set(FILTER_TYPE_WAITING)
    })
    val btnPendingClick: BindingCommand<Void> = BindingCommand(BindingAction {
        filterType.set(FILTER_TYPE_PENDING)
    })
    val btnCompleteClick: BindingCommand<Void> = BindingCommand(BindingAction {
        filterType.set(FILTER_TYPE_COMPLETE)
    })


    override fun setToolbarRightClick() {
        uc.showFilter.call()
    }

    fun getUserRooms() {
//        val list = arrayListOf("2", "2", "2", "2")
//        val t = BaseBean<List<String>>()
//        t.data = list
//        uc.loadRoomEvent.postValue(t.data)
    }

    companion object {
        const val FILTER_TYPE_WAITING = 1001;
        const val FILTER_TYPE_PENDING = 1002;
        const val FILTER_TYPE_COMPLETE = 1003;
    }
}