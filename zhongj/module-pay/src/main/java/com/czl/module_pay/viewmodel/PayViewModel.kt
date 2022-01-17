package com.czl.module_pay.viewmodel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import com.blankj.utilcode.util.GsonUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.PayInfoBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory


class PayViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()
    val payAmount = ObservableFloat(0f)
    var payType = ObservableInt(0)
    var orderNo = ""
    lateinit var paramsMap: Map<String, String>

    class UiChangeEvent {
        val openAliPayEvent: SingleLiveEvent<PayInfoBean.Data> = SingleLiveEvent()
        val openWxPayEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val btnPayClick: BindingCommand<Void> = BindingCommand(BindingAction {
        placeAnOrder()
    })
    val onWxClick: BindingCommand<Void> = BindingCommand(BindingAction {
        payType.set(0)
    })
    val onAliClick: BindingCommand<Void> = BindingCommand(BindingAction {
        payType.set(1)
    })

    fun setPayParams(paramsMap: Map<String, String>) {
        this.paramsMap = paramsMap
        payAmount.set(paramsMap.getValue("payAmount").toFloat())
    }

    private fun placeAnOrder() {

        when (payType.get()) {
            0 -> {
                uc.openWxPayEvent.call()
            }
            1 -> {
                //"paymentType" to "1",//1:支付宝 2:微信小程序
                model.placeAnOrder(
                    paramsMap + mapOf("paymentType" to "1", "sceneType" to "AND_SDK")
                ).compose(RxThreadHelper.rxSchedulerHelper(this))
                    .doOnSubscribe { showLoading() }
                    .subscribe(object : ApiSubscriberHelper<BaseBean<PayInfoBean>>() {
                        override fun onResult(t: BaseBean<PayInfoBean>) {
                            dismissLoading()
                            if (t.code == 200) {
                                if (t.data?.code == 200) {
                                    orderNo = t.data?.data?.reqsn.toString()
                                    uc.openAliPayEvent.postValue(t.data?.data)
                                } else {
                                    showErrorToast(t.data?.msg)
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
    }


}