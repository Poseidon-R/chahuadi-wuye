package com.czl.module_work.viewModel

import androidx.databinding.ObservableField
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper


/**
 * 创建日期：2021/12/28  14:47
 * 类说明:
 * @author：86152
 */
class MyOrderViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()

    var page = 1

    val pageSize = 5


    class UiChangeEvent {
        val loadOrderEvent: SingleLiveEvent<List<WorkOrderBean.Data>> = SingleLiveEvent()

        val clearOrderEvent: SingleLiveEvent<Void> = SingleLiveEvent()

        val showFilter: SingleLiveEvent<String> = SingleLiveEvent()
    }

    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
        page++
        getOrders()
    })

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        page = 1
        uc.clearOrderEvent.call()
        getOrders()
    })

    val btnOrderFilterClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.showFilter.call()
    })

    val btnOrderAnalyseClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK)
    })

    val filterType = ObservableField(FILTER_TYPE_WAITING)

    val filterOrderType = ObservableField(MyOrderDetailViewModel.FILTER_ORDER_TYPE_TAKE_CARE)

    val btnWaitingClick: BindingCommand<Void> = BindingCommand(BindingAction {
        page = 1
        uc.clearOrderEvent.call()
        filterType.set(FILTER_TYPE_WAITING)
        getOrders()
    })
    val btnPendingClick: BindingCommand<Void> = BindingCommand(BindingAction {
        page = 1
        uc.clearOrderEvent.call()
        filterType.set(FILTER_TYPE_PENDING)
        getOrders()
    })
    val btnCompleteClick: BindingCommand<Void> = BindingCommand(BindingAction {
        page = 1
        uc.clearOrderEvent.call()
        filterType.set(FILTER_TYPE_COMPLETE)
        getOrders()
    })


    fun setFilter(type: Int) {
        filterOrderType.set(type)
        page = 1
        uc.clearOrderEvent.call()
        getOrders()
    }

    override fun setToolbarRightClick() {
        uc.showFilter.call()
    }

    fun getOrders() {
        if (filterType.get() == FILTER_TYPE_WAITING) {
            getWaitingOrders()
        } else if (filterType.get() == FILTER_TYPE_PENDING) {
            getPendingOrders()
        } else {
            getCompleteOrders()
        }
    }

    fun getWaitingOrders() {
        model.workOrderReady(pageNum = page, pageSize = pageSize, filterOrderType.get().toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<WorkOrderBean>>() {
                override fun onResult(t: BaseBean<WorkOrderBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        t.data?.list?.forEach {
                            it.orderState == 1
                        }
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
        model.workOrderExec(pageNum = page, pageSize = pageSize, filterOrderType.get().toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<WorkOrderBean>>() {
                override fun onResult(t: BaseBean<WorkOrderBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        t.data?.list?.forEach {
                            it.orderState == 2
                        }
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
        model.workOrderFinish(pageNum = page, pageSize = pageSize, filterOrderType.get().toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<WorkOrderBean>>() {
                override fun onResult(t: BaseBean<WorkOrderBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        t.data?.list?.forEach {
                            it.orderState == 3
                        }
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

        /**
         * 保养工单
         */
        const val FILTER_ORDER_TYPE_TAKE_CARE = 1;

        /**
         * 巡检工单
         */
        const val FILTER_ORDER_TYPE_CHECK = 2;

        /**
         * 维修工单
         */
        const val FILTER_ORDER_TYPE_REPAIR = 3;

        /**
         * 报警工单
         */
        const val FILTER_ORDER_TYPE_WARNING = 4;

    }
}