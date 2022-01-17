package com.czl.module_work.viewModel

import android.os.Bundle
import androidx.databinding.ObservableField
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.PatrolBean
import com.czl.base.data.bean.TakeCareBean
import com.czl.base.data.bean.TakeCareDispatchBean
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper


/**
 * 创建日期：2021/12/28  10:27
 * 类说明:
 * @author：86152
 */
class PatrolAuditViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var page = 1

    val pageSize = 5

    /**
     * 是否巡检订单
     */
    var isPatrol = false;

    val uc = UiChangeEvent()

    val filterParams = ""

    class UiChangeEvent {
        val clearOrderEvent: SingleLiveEvent<Void> = SingleLiveEvent()

        val changTab: SingleLiveEvent<Void> = SingleLiveEvent()

        val loadRoomEvent: SingleLiveEvent<List<PatrolBean.Data>> = SingleLiveEvent()

        val batchAuditEvent: SingleLiveEvent<Void> = SingleLiveEvent()

    }


    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
        loadNext()
    })

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        refresh()
    })

    fun refresh() {
        page = 1
        uc.clearOrderEvent.call()
        getOrders()
    }

    fun loadNext() {
        page++
        getOrders()
    }

    val btnToAuditClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.batchAuditEvent.call()
    })

    val filterType = ObservableField(FILTER_TYPE_WAITING)

    val btnWaitingClick: BindingCommand<Void> = BindingCommand(BindingAction {
        page = 1
        uc.clearOrderEvent.call()
        filterType.set(MyOrderViewModel.FILTER_TYPE_WAITING)
        uc.changTab.call()
        getOrders()
    })
    val btnCompleteClick: BindingCommand<Void> = BindingCommand(BindingAction {
        page = 1
        uc.clearOrderEvent.call()
        filterType.set(MyOrderViewModel.FILTER_TYPE_COMPLETE)
        uc.changTab.call()
        getOrders()
    })

    fun getOrders() {
        if (filterType.get() == MyOrderViewModel.FILTER_TYPE_WAITING) {
            getWaitingOrders()
        } else {
            getCompleteOrders()
        }
    }

    fun getCompleteOrders() {
        model.checkTaskAuditConfirm(pageNum = page, pageSize = pageSize)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<PatrolBean>>() {
                override fun onResult(t: BaseBean<PatrolBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        uc.loadRoomEvent.postValue(t.data!!.list)
                    } else {
                        uc.loadRoomEvent.postValue(null)
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadRoomEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }

    fun getWaitingOrders() {
        model.checkTaskAuditUnConfirm(pageNum = page, pageSize = pageSize)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<PatrolBean>>() {
                override fun onResult(t: BaseBean<PatrolBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        uc.loadRoomEvent.postValue(t.data!!.list)
                    } else {
                        uc.loadRoomEvent.postValue(null)
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadRoomEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }

    override fun setToolbarRightClick() {

    }


    companion object {
        const val FILTER_TYPE_WAITING = 1001;
        const val FILTER_TYPE_COMPLETE = 1003;
    }
}