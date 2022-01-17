package com.czl.module_park.viewmodel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ObjectUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.PayDetail
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

/**
 *  Author:xch
 *  Date:2021/9/10
 *  Do:
 */
class ParkDetailViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    private var chargingId = ""
    val createTime = ObservableField("")
    val chargeTotal = ObservableField("")
    val vehicleNo = ObservableField("")
    val stopTime = ObservableField("")
    val areaName = ObservableField(model.getAreaName())
    val showHasData = ObservableInt(-1)
    val uc = UiChangeEvent()

    class UiChangeEvent {
        val payEvent: SingleLiveEvent<String> = SingleLiveEvent()
    }

    val onBackClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uC.finishEvent.call()
    })

    val onPayClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.payEvent.postValue(
            GsonUtils.toJson(
                mapOf(
                    "discountAmount" to "0",
                    "orderAmount" to chargeTotal.get(),
                    "payAmount" to chargeTotal.get(),
                    "orderType" to "1",//1：临停，2：月卡
                    "vehicleNo" to vehicleNo.get().toString(),
                    "chargingId" to chargingId,
                    "areaId" to model.getAreaId()
                )
            )
        )
    })

    fun parkCharging(vehicleNo: String) {
        this.vehicleNo.set(vehicleNo)
        model.parkCharging(vehicleNo)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading() }
            .subscribe(object : ApiSubscriberHelper<BaseBean<PayDetail>>(loadService) {
                override fun onResult(t: BaseBean<PayDetail>) {
                    dismissLoading()
                    if (t.code == 200) {
                        if (ObjectUtils.isEmpty(t.data)) {
                            showHasData.set(0)
                        } else {
                            showHasData.set(1)
                            t.data?.let {
                                createTime.set(
                                    date2String(
                                        it.createTime,
                                        "yyyy-MM-dd HH:mm:ss"
                                    )
                                )
                                stopTime.set(it.stopTime)
                                chargeTotal.set(it.chargeTotal)
                                chargingId = it.chargingId
                            }
                        }
                    } else {
                        showHasData.set(0)
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    showHasData.set(0)
                }

            })
    }
}