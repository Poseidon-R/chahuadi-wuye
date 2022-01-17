package com.czl.module_park.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import com.blankj.utilcode.util.GsonUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.MonthCardListBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper


class RenewCardViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    val restDay = ObservableField("")
    val startTime = ObservableField("")
    val endTime = ObservableField("")
    val price = ObservableFloat(0f)
    val totalAmount = ObservableFloat(0f)
    val canPay = ObservableBoolean(false)
    var vehicleNo: String = ""
    private var quantity = 0
    val uc = UiChangeEvent()

    class UiChangeEvent {
        val onPayClickEvent: SingleLiveEvent<String> = SingleLiveEvent()
    }

    val onPayClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.onPayClickEvent.postValue(
            GsonUtils.toJson(
                mapOf(
                    "discountAmount" to "0",
                    "orderAmount" to totalAmount.get().toString(),
                    "payAmount" to totalAmount.get().toString(),
                    "orderType" to "2",//1：临停，2：月卡
                    "vehicleNo" to vehicleNo,
                    "applyMonth" to quantity.toString(),
                    "areaId" to model.getAreaId()
                )
            )
        )
    })

    fun decreaseNum(quantity: Int) {
        this.quantity = quantity
        canPay.set(quantity != 0)
        totalAmount.set((quantity * price.get()))
    }

    fun setMonthCard(card: MonthCardListBean.Data) {
        card?.let {
            restDay.set(it.restDay)
            startTime.set(date2String(it.startDate))
            endTime.set(date2String(it.endDate))
        }
    }

    fun getMonthPrice(vehicleNo: String) {
        this.vehicleNo = vehicleNo
        model.getMonthPrice(vehicleNo, model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading() }
            .subscribe(object : ApiSubscriberHelper<BaseBean<Float>>() {
                override fun onResult(t: BaseBean<Float>) {
                    dismissLoading()
                    if (t.code == 200) {
                        t.data?.let {
                            price.set(it)
                        }
                    } else {
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    showErrorToast(msg)
                }

            })
    }
}