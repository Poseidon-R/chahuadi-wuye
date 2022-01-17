package com.czl.module_pay.viewmodel

import androidx.databinding.ObservableInt
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.PayInfoBean
import com.czl.base.data.bean.PayRecordListBean
import com.czl.base.data.bean.PayResultBean
import com.czl.base.event.LiveBusCenter
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class PayResultViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    var i = 0
    val resultType = ObservableInt(-1)
    private var endTime: Date = TimeUtils.getNowDate()
    private var startTime: Date = Date(endTime.time - 604800000L)//第前七天日期

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val onSetTitleEvent: SingleLiveEvent<String> = SingleLiveEvent()
    }

    fun setPayResult(result: Int) {
        if (result == AppConstants.Constants.PAY_SUCCESS_TYPE) {
            uc.onSetTitleEvent.postValue("支付成功")
            resultType.set(AppConstants.Constants.PAY_SUCCESS_TYPE)
            LiveBusCenter.postPayResultEvent(AppConstants.Constants.PAY_SUCCESS_TYPE)
        } else {
            uc.onSetTitleEvent.postValue("支付失败")
            resultType.set(AppConstants.Constants.PAY_FAIL_TYPE)
            LiveBusCenter.postPayResultEvent(AppConstants.Constants.PAY_FAIL_TYPE)
        }
    }

    fun queryPayResult(orderNo: String?) {
        model.queryPayResult(orderNo!!, model.getAreaId())
            .repeatWhen()
            { objectObservable ->
                objectObservable.flatMap {
                    // 加入判断条件：当轮询次数 = 5次后，就停止轮询
                    if (i > 3) {
                        Observable.error<Any>(Throwable("轮询结束"))
                    } else Observable.just(1).delay(2000, TimeUnit.MILLISECONDS)
                }
            }
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading("正在查询结果") }
            .subscribe(object : ApiSubscriberHelper<BaseBean<PayResultBean>>() {
                override fun onResult(t: BaseBean<PayResultBean>) {
                    i++
                    if (t.code == 200) {
                        if (t.data?.status == 1) //0 处理中，1支付成功，-1支付失败
                        {
                            i = AppConstants.Constants.PAY_SUCCESS_TYPE
                        }
                    } else {
                        i = AppConstants.Constants.PAY_FAIL_TYPE
                        showErrorToast(t.data?.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    setPayResult(i)
                    dismissLoading()
                }

            })
    }

    fun getPayRecordList() {
        model.getPayRecordList(
            mapOf(
                "beginTime" to date2String(startTime, "yyyy-MM-dd'T'00:00:00.SSSZ"),
                "endTime" to date2String(endTime, "yyyy-MM-dd'T'23:59:59.SSSZ"),
                "pageNum" to "1",
                "pageSize" to "10",
                "areaId" to model.getAreaId(),
            )
        )
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading("正在查询结果") }
            .subscribe(object : ApiSubscriberHelper<BaseBean<PayRecordListBean>>() {
                override fun onResult(t: BaseBean<PayRecordListBean>) {
                    dismissLoading()
                    if (t.code == 200) {
                        if (ObjectUtils.isNotEmpty(t.data)) {
                            if (t.data!!.records.isNotEmpty()) {
                                var record = t.data!!.records[0]
                                queryPayResult(record.orderNo)
                            } else {
                                setPayResult(AppConstants.Constants.PAY_FAIL_TYPE)
                            }
                        } else {
                            setPayResult(AppConstants.Constants.PAY_FAIL_TYPE)
                        }
                    } else {
                        showErrorToast(t.msg)
                        setPayResult(AppConstants.Constants.PAY_FAIL_TYPE)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                    dismissLoading()
                    setPayResult(AppConstants.Constants.PAY_FAIL_TYPE)
                }

            })
    }
}