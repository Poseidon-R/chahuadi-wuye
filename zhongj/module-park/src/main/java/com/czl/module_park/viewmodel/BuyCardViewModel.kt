package com.czl.module_park.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.TimeUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.CarItem
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.DateHelper
import com.czl.base.util.RxThreadHelper
import java.util.*

/**
 *  Author:xch
 *  Date:2021/9/12
 *  Do:
 */
class BuyCardViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val phone = ObservableField(model.getLoginPhone())
    private var quantity = ObservableField("1")
    var vehicleNo = ObservableField("")
    val totalAmount = ObservableFloat(0f)
    val canPay = ObservableBoolean(false)
    val areaName = ObservableField(model.getAreaName())
    var price = 0f
    private var startTime: Date = TimeUtils.getNowDate()
    val startDate = ObservableField(date2String(startTime))

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val onPayClickEvent: SingleLiveEvent<String> = SingleLiveEvent()
        val choiceStartDateCommand: SingleLiveEvent<Void> = SingleLiveEvent()
        val choiceCarCommand: SingleLiveEvent<Void> = SingleLiveEvent()
        val loadMyCarEvent: SingleLiveEvent<List<CarItem>> = SingleLiveEvent()
    }

    val onChoiceStartDateClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.choiceStartDateCommand.call()
    })

    val onChoiceCarClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.choiceCarCommand.call()
    })

    val onPayClick: BindingCommand<Void> = BindingCommand(BindingAction {
        if (vehicleNo.get().isNullOrBlank()) {
            showErrorToast("请选择车牌号")
            return@BindingAction
        }
        uc.onPayClickEvent.postValue(
            GsonUtils.toJson(
                mapOf(
                    "startDate" to date2String(
                        startTime,
                        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
                    ),
                    "discountAmount" to "0",
                    "orderAmount" to totalAmount.get().toString(),
                    "payAmount" to totalAmount.get().toString(),
                    "orderType" to "2",//1：临停，2：月卡
                    "vehicleNo" to vehicleNo.get().toString(),
                    "applyMonth" to quantity.get().toString(),
                    "areaId" to model.getAreaId()
                )
            )
        )
    })

    fun setStartDate(date: Date) {
        startTime = date
        startDate.set(date2String(date))
    }

    fun decreaseNum(quantity: Int) {
        this.quantity.set(quantity.toString())
        totalAmount.set(quantity * price)
    }

    fun getCarList() {
        model.getMyCarList(true, model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<CarItem>>>() {
                override fun onResult(t: BaseBean<List<CarItem>>) {
                    if (t.code == 200) {
                        uc.loadMyCarEvent.postValue(t.data)
                    } else {
                        uc.loadMyCarEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                    uc.loadMyCarEvent.postValue(null)
                }

            })
    }

    fun getMonthPrice(vehicleNo: String) {
        this.vehicleNo.set(vehicleNo)
        model.getMonthPrice(vehicleNo,model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe {
                canPay.set(false)
                showLoading()
            }
            .subscribe(object : ApiSubscriberHelper<BaseBean<Float>>() {
                override fun onResult(t: BaseBean<Float>) {
                    dismissLoading()
                    if (t.code == 200) {
                        t.data?.let {
                            price = it
                            decreaseNum(quantity.get()!!.toInt())
                            canPay.set(true)
                        }

                    } else {
                        showErrorToast(t.msg)
                        canPay.set(false)
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    showErrorToast(msg)
                }

            })
    }

}