package com.czl.module_park.viewmodel

import androidx.databinding.ObservableField
import com.blankj.utilcode.util.TimeUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.PayRecordListBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper
import java.util.*

/**
 *  Author:xch
 *  Date:2021/9/10
 *  Do:
 */
class PayRecordsViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentPage = 0
    var status = "" //  >0 支付成功，0 待支付，-1 支付失败，-2 退款中，-3 已退款
    val uc = UiChangeEvent()

    private var endTime: Date = TimeUtils.getNowDate()
    private var startTime: Date = Date(endTime.time - 604800000L)//第前七天日期

    val startDate = ObservableField(date2String(startTime, "yyyy-MM-dd"))
    val endDate = ObservableField(date2String(endTime, "yyyy-MM-dd"))
    val payStatus = ObservableField("全部")

    class UiChangeEvent {
        val loadCompleteEvent: SingleLiveEvent<PayRecordListBean> = SingleLiveEvent()
        val choiceStartDateCommand: SingleLiveEvent<Void> = SingleLiveEvent()
        val choiceEndDateCommand: SingleLiveEvent<Void> = SingleLiveEvent()
        val choiceStatusCommand: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val onChoiceStartDateClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.choiceStartDateCommand.call()
    })

    val onChoiceEndDateClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.choiceEndDateCommand.call()
    })

    val onChoiceStatusClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.choiceStatusCommand.call()
    })

    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
        getPayRecordList()
    })

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = 0
        getPayRecordList()
    })

    fun setStartDate(startDate: Date) {
        this.startTime = startDate
        this.startDate.set(date2String(startDate, "yyyy-MM-dd"))
    }

    fun setEndDate(endDate: Date) {
        this.endTime = endDate
        this.endDate.set(date2String(endDate, "yyyy-MM-dd"))
    }

    private fun getPayRecordList() {
        model.getPayRecordList(
            mapOf(
                "beginTime" to date2String(startTime, "yyyy-MM-dd'T'00:00:00.SSSZ"),
                "endTime" to date2String(endTime, "yyyy-MM-dd'T'23:59:59.SSSZ"),
                "pageNum" to (currentPage + 1).toString(),
                "pageSize" to "10",
                "areaId" to model.getAreaId(),
                "status" to status,
            )
        )
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<PayRecordListBean>>() {
                override fun onResult(t: BaseBean<PayRecordListBean>) {
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