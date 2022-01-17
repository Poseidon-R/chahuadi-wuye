package com.czl.module_park.viewmodel

import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.MonthCardListBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

/**
 *  Author:xch
 *  Date:2021/9/10
 *  Do:
 */
class CardListViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentPage = 0
    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadCompleteEvent: SingleLiveEvent<MonthCardListBean> = SingleLiveEvent()
    }

    val onBuyCardClickCommand:BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Park.F_BUY_CARD)
    })

    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
        getMonthCardList()
    })
    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = 0
        getMonthCardList()
    })

    private fun getMonthCardList() {
        model.getMonthCardList(currentPage + 1,10,model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<MonthCardListBean>>(){
                override fun onResult(t: BaseBean<MonthCardListBean>) {
                   if (t.code == 200){
                       currentPage ++
                       uc.loadCompleteEvent.postValue(t.data)
                   }else{
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