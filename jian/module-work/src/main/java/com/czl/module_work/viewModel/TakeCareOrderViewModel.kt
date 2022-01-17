package com.czl.module_work.viewModel

import androidx.databinding.ObservableField
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
import com.czl.base.data.bean.TakeCarePageParams
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper


/**
 * 创建日期：2021/12/27  14:04
 * 类说明:
 * @author：86152
 */
class TakeCareOrderViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var page = 1

    val pageSize = 10

    /**
     * 是否巡检订单
     */
    var isPatrol = false;

    val uc = UiChangeEvent()

    val filterParams = TakeCarePageParams.Data()

    class UiChangeEvent {
        val loadOrderEvent: SingleLiveEvent<List<TakeCareBean.Data>> = SingleLiveEvent()

        val loadNewOrderEvent: SingleLiveEvent<List<TakeCareBean.Data>> = SingleLiveEvent()

        val clearOrderEvent: SingleLiveEvent<Void> = SingleLiveEvent()

        val showFilter: SingleLiveEvent<String> = SingleLiveEvent()
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
        getOrders()
    })
    val btnPendingClick: BindingCommand<Void> = BindingCommand(BindingAction {
        page = 1
        uc.clearOrderEvent.call()
        filterType.set(MyOrderViewModel.FILTER_TYPE_PENDING)
        getOrders()
    })
    val btnCompleteClick: BindingCommand<Void> = BindingCommand(BindingAction {
        page = 1
        uc.clearOrderEvent.call()
        filterType.set(MyOrderViewModel.FILTER_TYPE_COMPLETE)
        getOrders()
    })


    fun setFilterParams(
        startTime: String,
        endTime: String,
        name: String,
        orderNo: String,
        assetsNo: String
    ) {
        filterParams.startTime = startTime
        filterParams.endTime = endTime
        filterParams.handleUserName = name
        filterParams.orderNo = orderNo
        filterParams.deviceNo = assetsNo
        page = 1
        uc.clearOrderEvent.call()
        getOrders()
    }

    fun getOrders() {
        if (filterType.get() == MyOrderViewModel.FILTER_TYPE_WAITING) {
            getWaitingOrders()
        } else if (filterType.get() == MyOrderViewModel.FILTER_TYPE_PENDING) {
            getPendingOrders()
        } else {
            getCompleteOrders()
        }
    }

    override fun setToolbarRightClick() {
        uc.showFilter.call()
    }

    fun getWaitingOrders() {
        model.takeCareOrderReady(pageNum = page, pageSize = pageSize, filterParams)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<TakeCareBean>>() {
                override fun onResult(t: BaseBean<TakeCareBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        uc.loadOrderEvent.postValue(t.data!!.list)
                    } else {
                        uc.loadOrderEvent.postValue(null)
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadOrderEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }


    fun getPendingOrders() {
        model.takeCareOrderExec(pageNum = page, pageSize = pageSize, filterParams)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<TakeCareBean>>() {
                override fun onResult(t: BaseBean<TakeCareBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        uc.loadOrderEvent.postValue(t.data!!.list)
                    } else {
                        uc.loadOrderEvent.postValue(null)
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadOrderEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }

    fun getCompleteOrders() {
        model.takeCareOrderFinish(pageNum = page, pageSize = pageSize, filterParams)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<TakeCareBean>>() {
                override fun onResult(t: BaseBean<TakeCareBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        uc.loadOrderEvent.postValue(t.data!!.list)
                    } else {
                        uc.loadOrderEvent.postValue(null)
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadOrderEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }

    companion object {
        const val FILTER_TYPE_WAITING = 1001;
        const val FILTER_TYPE_PENDING = 1002;
        const val FILTER_TYPE_COMPLETE = 1003;
    }
}