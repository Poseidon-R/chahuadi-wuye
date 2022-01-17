package com.czl.module_main.viewmodel

import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.CarItem
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

class HomeViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadCompleteEvent: SingleLiveEvent<List<CarItem>> = SingleLiveEvent()
        val deleteResultEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    fun getMyQueryHistory() {
        model.getMyQueryHistory()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<CarItem>>>() {
                override fun onResult(t: BaseBean<List<CarItem>>) {
                    if (t.code == 200) {
                        uc.loadCompleteEvent.postValue(t.data)
                    } else {
                        uc.loadCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                }

            })
    }

    fun deleteQueryCar(vehicleNo: String) {
        model.deleteQueryCar(vehicleNo,model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading("删除中...") }
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                override fun onResult(t: BaseBean<Any?>) {
                    dismissLoading()
                    if (t.code == 200) {
                        uc.deleteResultEvent.call()
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    showErrorToast(msg)
                }
            })
    }

}