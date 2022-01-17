package com.czl.module_work.viewModel

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.PatrolOrderDetailBean
import com.czl.base.data.bean.WorkOrderDetailBean
import com.czl.base.event.LiveBusCenter
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.FormUtils
import com.czl.base.util.RxThreadHelper
import java.util.*


/**
 * 创建日期：2021/12/27  17:50
 * 类说明:
 * @author：86152
 */
class PatrolAuditDetailViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val isAgree = ObservableField(false)

    val isReject = ObservableField(false)

    val planNameText = ObservableField<String>()

    val orderNoText = ObservableField<String>()

    val planTime = ObservableField<String>()

    val execName = ObservableField<String>()

    val orderTime = ObservableField<String>()

    val orderStatus = ObservableField<String>()

    val finishTimeText = ObservableField<String>()

    val remarkText = ObservableField<String>()

    var orderId = ""

    val uc = UiChangeEvent()

    class UiChangeEvent {

        val loadDeviceEvent: SingleLiveEvent<List<PatrolOrderDetailBean.TaskList>> = SingleLiveEvent()

    }

    val btnAuditClick: BindingCommand<Void> = BindingCommand(BindingAction {
        audit()
    })

    val btnAgreeClick: BindingCommand<Void> = BindingCommand(BindingAction {
        isAgree.set(true)
        isReject.set(false)
    })

    val btnRejectClick: BindingCommand<Void> = BindingCommand(BindingAction {
        isAgree.set(false)
        isReject.set(true)
    })

    fun audit() {
        val remark = remarkText.get() ?: ""
        if (isAgree.get() == false && isReject.get() == false) {
            ToastUtils.showShort("请进行审核!")
            return
        }
        model.checkTaskCheck(remark, listOf(orderId), isAgree.get() ?: false)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                override fun onResult(t: BaseBean<Any>) {
                    ToastUtils.showShort("审核完成")
                    LiveBusCenter.postAuditRefreshEvent("")
                    uC.finishEvent.call();
                }

                override fun onFailed(msg: String?) {

                }
            })
    }


    fun getTakeOrderFull(orderId: String) {
        this.orderId = orderId
        model.getPatrolFullOrder(orderId)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<PatrolOrderDetailBean>>() {
                override fun onResult(t: BaseBean<PatrolOrderDetailBean>) {
                    planNameText.set(t.data?.workOrder?.orderName)
                    orderNoText.set(t.data?.workOrder?.orderNo)
                    val startTime =
                        if (TextUtils.isEmpty(t.data?.task?.startTime)) "" else FormUtils.dealDateFormat(
                            (t.data?.task?.startTime)
                        )
                    val endTime =
                        if (TextUtils.isEmpty(t.data?.task?.endTime)) "" else FormUtils.dealDateFormat(
                            (t.data?.task?.endTime)
                        )
                    val finishTime =
                        if (TextUtils.isEmpty(t.data?.task?.finishTime)) "" else FormUtils.dealDateFormat(
                            (t.data?.task?.finishTime)
                        )
                    planTime.set("$startTime-$endTime")
                    execName.set(t.data?.task?.execUserName)
                    if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
                        orderTime.set("0")
                    } else {
                        orderTime.set(calculateTime(startTime!!, finishTime!!))
                    }
                    finishTimeText.set(finishTime)
                    val statusText = when (t.data?.task?.taskState) {
                        "0" -> "待接单"
                        "1" -> "未开始"
                        "2" -> "处理中"
                        "3" -> "待确认"
                        "4" -> "已超期"
                        "5" -> "已完成"
                        else -> "-----"
                    }
                    uc.loadDeviceEvent.postValue(t.data?.taskDetailList)
                    orderStatus.set(statusText)
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    private fun calculateTime(start: String, end: String): String {
        val startTimeMillis = TimeUtils.string2Millis(start, "yyyy-MM-dd HH:mm:ss")
        val endTimeMillis = TimeUtils.string2Millis(end, "yyyy-MM-dd HH:mm:ss")
        return formatDuring(endTimeMillis - startTimeMillis)
    }

    fun formatDuring(mss: Long): String {
        val days = mss / (1000 * 60 * 60 * 24)
        val hours = mss % (1000 * 60 * 60 * 24) / (1000 * 60 * 60)
        val minutes = mss % (1000 * 60 * 60) / (1000 * 60)
        val seconds = mss % (1000 * 60) / 1000
        val week = days / 7
        val formatDay = days % 7
        return "$week 周 $formatDay 天 $hours 小时"
    }
}