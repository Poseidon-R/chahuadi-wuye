package com.czl.module_work.viewModel

import android.os.Bundle
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
import com.czl.base.data.bean.*
import com.czl.base.event.LiveBusCenter
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.FormUtils
import com.czl.base.util.RxThreadHelper
import com.google.gson.Gson


/**
 * 创建日期：2021/12/27  17:50
 * 类说明:
 * @author：86152
 */
class MyOrderDetailViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val filterType = ObservableField(FILTER_TYPE_WAITING)

    val filterOrderType = ObservableField(FILTER_ORDER_TYPE_TAKE_CARE)

    val timeTag = ObservableField<String>()

    val btnText = ObservableField<String>()

    val planNameText = ObservableField<String>()

    val orderNoText = ObservableField<String>()

    val alarmDeviceName = ObservableField<String>()

    val alarmType = ObservableField<String>()

    val alarmLevel = ObservableField<String>()

    val alarmTime = ObservableField<String>()

    val alarmLocation = ObservableField<String>()

    val alarmDesc = ObservableField<String>()

    val alarmHandleTime = ObservableField<String>()

    val alarmHandleName = ObservableField<String>()

    val planTime = ObservableField<String>()

    val execName = ObservableField<String>()

    val orderTime = ObservableField<String>()

    val orderStatus = ObservableField<String>()

    val finishTimeText = ObservableField<String>()

    val uploadText = ObservableField<String>("")

    val uc = UiChangeEvent()

    var orderId = ""

    class UiChangeEvent {
        val doTransferFormClick: SingleLiveEvent<String> = SingleLiveEvent()

        val loadDeviceEvent: SingleLiveEvent<List<WorkOrderDetailBean.TaskList>> = SingleLiveEvent()

        val loadPatrolDeviceEvent: SingleLiveEvent<List<PatrolOrderDetailBean.TaskList>> =
            SingleLiveEvent()

        val loadImageEvent: SingleLiveEvent<List<String>> =
            SingleLiveEvent()

        val doOrderReceivingClick: SingleLiveEvent<String> = SingleLiveEvent()

        val uploadCertificate: SingleLiveEvent<String> = SingleLiveEvent()

        val uploadAlarmCertificate: SingleLiveEvent<String> = SingleLiveEvent()

    }

    val btnTransferFormClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.doTransferFormClick.call()
    })

    val btnOrderReceivingClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.doOrderReceivingClick.call()
    })

    val btnUploadCertificateClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.uploadCertificate.call()
    })
    val btnUploadAlarmCertificateClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.uploadAlarmCertificate.call()
    })
    val btnFinishClick: BindingCommand<Void> = BindingCommand(BindingAction {
        if (filterOrderType.get() == FILTER_ORDER_TYPE_CHECK) {
            finishPatrolOrder(orderId)
        }
        if (filterOrderType.get() == FILTER_ORDER_TYPE_TAKE_CARE) {
            finishOrder(orderId)
        }
        if (filterOrderType.get() == FILTER_ORDER_TYPE_WARNING) {
            finishAlarmOrder(orderId)
        }
    })

    fun getTakeOrderFull(orderId: String) {
        this.orderId = orderId
        model.getTakeCareFullOrder(orderId)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<WorkOrderDetailBean>>() {
                override fun onResult(t: BaseBean<WorkOrderDetailBean>) {
                    planNameText.set(t.data?.workOrder?.orderName)
                    orderNoText.set(t.data?.workOrder?.orderNo)
                    uploadText.set("维保登记（${t.data?.taskDetailList?.size ?: 0}）")
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
                    val statusText = when (t.data?.workOrder?.orderState) {
//                        "0" -> "待接单"
                        1 -> "待接单"
                        2 -> "处理中"
                        3 -> "已完成"
//                        "4" -> "已超期"
//                        "5" -> "已完成"
                        else -> "-----"
                    }
                    uc.loadDeviceEvent.postValue(t.data?.taskDetailList)
                    orderStatus.set(statusText)
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    fun receivingOrder(
        member: MembersBean?,
        note: String,
        orderId: String,
        serviceTime: String
    ) {
        model.workOrderAccept(
            model.getLocalUserInfo()?.userId ?: "",
            model.getLocalUserInfo()?.nickName ?: "",
            member,
            note,
            orderId,
            serviceTime
        )
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                override fun onResult(t: BaseBean<Any>) {
                    if (t.code == 200) {
                        ToastUtils.showShort("接单成功!")
                        LiveBusCenter.postWorkOrderRefreshEvent("")
                    } else {
                        ToastUtils.showLong(t.errorMsg)
                    }
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    fun checkTaskRegister(
        id: String,
        status: String,
        standardList: List<PatrolOrderDetailBean.StandardList>
    ) {
        model.checkTaskRegister(
            id, status, standardList
        )
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                override fun onResult(t: BaseBean<Any>) {
                    if (t.code == 200) {
                        ToastUtils.showShort("保存成功!")
                        LiveBusCenter.postWorkOrderDetailRefreshEvent("")
                    } else {
                        ToastUtils.showLong(t.errorMsg)
                    }
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    fun finishOrder(
        orderId: String
    ) {
        model.inspectTaskFinishOrder(
            orderId
        )
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                override fun onResult(t: BaseBean<Any>) {
                    if (t.code == 200) {
                        ToastUtils.showShort("完成!")
                        LiveBusCenter.postWorkOrderRefreshEvent("")
                        finish()
                    } else {
                        ToastUtils.showLong(t.errorMsg)
                    }
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    fun finishPatrolOrder(
        orderId: String
    ) {
        model.checkTaskFinishOrder(
            orderId
        )
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                override fun onResult(t: BaseBean<Any>) {
                    if (t.code == 200) {
                        ToastUtils.showShort("完成!")
                        LiveBusCenter.postWorkOrderRefreshEvent("")
                        finish()
                    } else {
                        ToastUtils.showLong(t.errorMsg)
                    }
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    fun finishAlarmOrder(
        orderId: String
    ) {
        model.alarmTaskFinishOrder(
            orderId
        )
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                override fun onResult(t: BaseBean<Any>) {
                    if (t.code == 200) {
                        ToastUtils.showShort("完成!")
                        LiveBusCenter.postWorkOrderRefreshEvent("")
                        finish()
                    } else {
                        ToastUtils.showLong(t.errorMsg)
                    }
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    fun transferOrder(
        member: MembersBean,
        note: String,
        orderId: String,
    ) {
        model.workOrderTransfer(
            member.userId,
            member.nickName,
            note,
            orderId,
        )
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                override fun onResult(t: BaseBean<Any>) {
                    if (t.code == 200) {
                        ToastUtils.showShort("转单成功!")
                        LiveBusCenter.postWorkOrderRefreshEvent("")
                    } else {
                        ToastUtils.showLong(t.errorMsg)
                    }
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    fun getAlarmFull(orderId: String) {
        this.orderId = orderId
        model.getAlarmFullOrder(orderId)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<AlarmOrderDetailBean>>() {
                override fun onResult(t: BaseBean<AlarmOrderDetailBean>) {
                    planNameText.set(t.data?.workOrder?.orderName)
                    orderNoText.set(t.data?.workOrder?.orderNo)
                    alarmDeviceName.set(t.data?.workOrder?.orderName)
                    alarmLevel.set(t.data?.alarmRecord?.alarmLevel)
                    alarmType.set(t.data?.alarmRecord?.alarmTypeName)
                    alarmDesc.set(t.data?.alarmRecord?.alarmDesc)
                    alarmLocation.set(t.data?.pointInfo?.location)
                    val time =
                        if (TextUtils.isEmpty(t.data?.alarmRecord?.alarmTime)) "" else FormUtils.dealDateFormat(
                            (t.data?.alarmRecord?.alarmTime)
                        )
                    val handleTime =
                        if (TextUtils.isEmpty(t.data?.workOrder?.serviceTime)) "" else FormUtils.dealDateFormat(
                            (t.data?.workOrder?.serviceTime)
                        )
                    alarmTime.set(time)
                    alarmHandleTime.set(handleTime)
                    alarmHandleName.set(t.data?.workOrder?.handleUserName)
                    val statusText = when (t.data?.workOrder?.orderState) {
                        1 -> "待接单"
                        2 -> "处理中"
                        3 -> "已完成"
                        else -> "-----"
                    }
                    if (TextUtils.isEmpty(t.data?.alarmRecord?.handleImage)) {
                        uc.loadImageEvent.postValue(null)
                    } else {
                        uc.loadImageEvent.postValue(listOf(t.data?.alarmRecord?.handleImage ?: ""))
                    }
                    orderStatus.set(statusText)
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    fun getPatrolFull(orderId: String) {
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
                    val statusText = when (t.data?.workOrder?.orderState) {
                        1 -> "待接单"
                        2 -> "处理中"
                        3 -> "已完成"
                        else -> "-----"
                    }
                    uc.loadPatrolDeviceEvent.postValue(t.data?.taskDetailList)
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

    companion object {
        const val FILTER_TYPE_WAITING = 1001;
        const val FILTER_TYPE_PENDING = 1002;
        const val FILTER_TYPE_COMPLETE = 1003;

        /**
         * 保养工单
         */
        const val FILTER_ORDER_TYPE_TAKE_CARE = 101;

        /**
         * 巡检工单
         */
        const val FILTER_ORDER_TYPE_CHECK = 102;

        /**
         * 报警工单
         */
        const val FILTER_ORDER_TYPE_WARNING = 103;

        /**
         * 报警工单
         */
        const val FILTER_ORDER_TYPE_OTHER = 104;
    }
}