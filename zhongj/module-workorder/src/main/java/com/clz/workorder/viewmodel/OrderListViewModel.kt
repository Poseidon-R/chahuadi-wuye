package com.clz.workorder.viewmodel

import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.MonthCardListBean
import com.czl.base.data.bean.RepairBean
import com.czl.base.data.bean.RepairListBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

/**
 *
 * @Description:
 * @Author: XCH
 * @CreateDate: 2021/12/14 10:09
 */
class OrderListViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentPage = 0
    var propertyState = "0" //0待接单,1处理中,2待支付/待审核,3已完成
    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadCompleteEvent: SingleLiveEvent<RepairListBean> = SingleLiveEvent()
    }

    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
        getMonthCardList()
    })
    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = 0
        getMonthCardList()
    })

    private fun getMonthCardList() {
        model.getRepairList(
            mapOf(
                "pageNum" to (currentPage + 1).toString(),
                "pageSize" to "10",
                "propertyState" to propertyState
            )
        )
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<RepairListBean>>() {
                override fun onResult(t: BaseBean<RepairListBean>) {
                    if (t.code == 200) {
                        currentPage++
                        uc.loadCompleteEvent.postValue(t.data)
                    } else {
                        uc.loadCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                    uc.loadCompleteEvent.postValue(null)
                }

            })
    }
}