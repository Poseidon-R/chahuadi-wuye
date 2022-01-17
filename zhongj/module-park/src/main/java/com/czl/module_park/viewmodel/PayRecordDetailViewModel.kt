package com.czl.module_park.viewmodel

import androidx.databinding.ObservableField
import com.blankj.utilcode.util.TimeUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.RecordDetailBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper
import java.util.*

class PayRecordDetailViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val amount = ObservableField<String>()
    val bizType = ObservableField<String>()
    val carNo = ObservableField<String>()
    val orderNo = ObservableField<String>()
    val validPeriod = ObservableField<String>()
    val payResult = ObservableField<String>()
    val payTime = ObservableField(Date())
    val payType = ObservableField<String>()
    private var payInfo = ""

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val openAliPayEvent: SingleLiveEvent<String> = SingleLiveEvent()
        val openWxPayEvent: SingleLiveEvent<String> = SingleLiveEvent()
    }

    val onCopClick: BindingCommand<Void> = BindingCommand(BindingAction {
        if (TimeUtils.getNowDate().time - payTime.get()!!.time > 299000) {
            showErrorToast("订单已超时, 请重新下单")
            return@BindingAction
        }
        when (payType.get()) {
            "微信" -> {
                uc.openWxPayEvent.postValue(payInfo)
            }
            "支付宝" -> {
                uc.openAliPayEvent.postValue(payInfo)
            }
        }

    })

    fun getRecordDetail(orderno: String) {
        model.getRecordDetail(orderno)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading() }
            .subscribe(object : ApiSubscriberHelper<BaseBean<RecordDetailBean>>() {
                override fun onResult(t: BaseBean<RecordDetailBean>) {
                    dismissLoading()
                    if (t.code == 200) {
                        t.data?.let {
                            amount.set(it.amount)
                            bizType.set(it.bizType)
                            carNo.set(it.carNo)
                            orderNo.set(it.orderNo)
                            validPeriod.set(it.validPeriod)
                            payResult.set(it.payResult)
                            payTime.set(it.payTime)
                            payType.set(it.payType)
                            payInfo = it.payInfo
                        }
                    } else {
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    showErrorToast(msg)
                }

            })
    }
}