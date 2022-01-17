package com.czl.module_work.viewModel

import android.os.Bundle
import androidx.databinding.ObservableField
import com.alibaba.android.arouter.facade.annotation.Route
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.blankj.utilcode.util.GsonUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.*
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper


/**
 * 创建日期：2021/12/27  14:04
 * 类说明:
 * @author：86152
 */
class PatrolDispatchViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {


    var page = 1

    val pageSize = 10

    val uc = UiChangeEvent()

    /**
     * 是否巡检订单
     */
    var isPatrol = false;

    val filterParams = ""

    class UiChangeEvent {
        val clearOrderEvent: SingleLiveEvent<Void> = SingleLiveEvent()

        val changTab: SingleLiveEvent<Void> = SingleLiveEvent()

        val loadRoomEvent: SingleLiveEvent<List<PatrolBean.Data>> = SingleLiveEvent()

        val batchDispatchEvent: SingleLiveEvent<Void> = SingleLiveEvent()
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

    val btnToDispatchClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.batchDispatchEvent.call()
    })

    fun getOrders() {
        if (filterType.get() == MyOrderViewModel.FILTER_TYPE_WAITING) {
            getWaitingOrders()
        } else {
            getCompleteOrders()
        }
    }

    fun getCompleteOrders() {
        model.patrolOrderDispatchFinish(pageNum = page, pageSize = pageSize)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<PatrolBean>>() {
                override fun onResult(t: BaseBean<PatrolBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        uc.loadRoomEvent.postValue(t.data!!.list)
                    } else {
                        uc.loadRoomEvent.postValue(listOf())
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadRoomEvent.postValue(listOf())
                    showErrorToast(msg)
                }
            })
    }

    fun getWaitingOrders() {
        model.patrolOrderDispatchReady(pageNum = page, pageSize = pageSize)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<PatrolBean>>() {
                override fun onResult(t: BaseBean<PatrolBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        uc.loadRoomEvent.postValue(t.data!!.list)
                    } else {
                        uc.loadRoomEvent.postValue(listOf())
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadRoomEvent.postValue(listOf())
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