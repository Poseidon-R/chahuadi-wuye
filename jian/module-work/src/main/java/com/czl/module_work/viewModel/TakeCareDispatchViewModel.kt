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
import com.czl.base.data.bean.RoomBean
import com.czl.base.data.bean.TakeCareBean
import com.czl.base.data.bean.TakeCareDispatchBean
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper


/**
 * 创建日期：2021/12/27  14:04
 * 类说明:
 * @author：86152
 */
class TakeCareDispatchViewModel(application: MyApplication, model: DataRepository) :
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

        val loadRoomEvent: SingleLiveEvent<List<TakeCareDispatchBean.Data>> = SingleLiveEvent()

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
        model.takeCareOrderDispatchFinish(pageNum = page, pageSize = pageSize, filterParams)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<TakeCareDispatchBean>>() {
                override fun onResult(t: BaseBean<TakeCareDispatchBean>) {
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
        model.takeCareOrderDispatchReady(pageNum = page, pageSize = pageSize, filterParams)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<TakeCareDispatchBean>>() {
                override fun onResult(t: BaseBean<TakeCareDispatchBean>) {
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


    fun getUserRooms() {
//        val list = arrayListOf("", "", "", "")
//        val t = BaseBean<List<WorkOrderBean.Data>>()
//        t.data = list
//        uc.loadRoomEvent.postValue(t.data)
//        model.saveRoomIdAndCode(GsonUtils.toJson(list))
//        model.getUserRooms(model.getLoginPhone().toString(), model.getAreaId())
//            .compose(RxThreadHelper.rxSchedulerHelper(this))
//            .subscribe(
//                object : ApiSubscriberHelper<BaseBean<List<RoomBean>>>() {
//                    override fun onResult(t: BaseBean<List<RoomBean>>) {
//                        if (t.code == 200) {
//                            uc.loadRoomEvent.postValue(t.data)
//                            t.data?.let {
//                                var rooms = Stream.of(it).map { e -> e.houseId + e.houseCode }
//                                    .collect(Collectors.toList())
//                                model.saveRoomIdAndCode(GsonUtils.toJson(rooms))
//                            }
//
//                        } else {
//                            uc.loadRoomEvent.postValue(null)
//                            showErrorToast(t.msg)
//                        }
//                    }
//
//                    override fun onFailed(msg: String?) {
//                        uc.loadRoomEvent.postValue(null)
//                        showErrorToast(msg)
//                    }
//
//                }
//            )
    }

    companion object {
        const val FILTER_TYPE_WAITING = 1001;
        const val FILTER_TYPE_COMPLETE = 1003;
    }
}