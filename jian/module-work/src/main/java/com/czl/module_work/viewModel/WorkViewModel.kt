package com.czl.module_work.viewModel

import android.graphics.Color
import androidx.databinding.ObservableField
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.PercentBean
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.data.bean.WorkOrderTimeBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper
import com.czl.base.widget.PieChartView
import java.math.BigDecimal

class WorkViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val waitingOrderTotal = ObservableField("")

    val pendingOrderTotal = ObservableField("")

    val completeOrderTotal = ObservableField("")

    val orderTotal = ObservableField("")

    val data = ObservableField<List<PieChartView.ItemType>>()
    val uc = UiChangeEvent()
    val type = ObservableField<Int>(1)


    class UiChangeEvent {
        val loadRoomEvent: SingleLiveEvent<List<WorkOrderBean.Data>> = SingleLiveEvent()
        val loadChatEvent: SingleLiveEvent<List<PercentBean>> = SingleLiveEvent()
        val loadOrderTimeEvent: SingleLiveEvent<List<WorkOrderTimeBean>> = SingleLiveEvent()
    }

    val btn1Click = BindingCommand<Void>(BindingAction {
        type.set(1)
        workOrderPercent()
    })

    val btn2Click = BindingCommand<Void>(BindingAction {
        type.set(2)
        workOrderPercent()
    })

    val btn3Click = BindingCommand<Void>(BindingAction {
        type.set(3)
        workOrderPercent()
    })

    fun getUserRooms() {
        model.workOrderToday()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Map<String, Int>>>() {
                override fun onResult(t: BaseBean<Map<String, Int>>) {
                    val exec_num = t.data?.get("exec_num") ?: 0
                    val ready_num = t.data?.get("ready_num") ?: 0
                    val finish_num = t.data?.get("finish_num") ?: 0
                    val all_num = t.data?.get("all_num") ?: 0
                    waitingOrderTotal.set("待接单 ${ready_num}")
                    pendingOrderTotal.set("处理中 ${exec_num}")
                    completeOrderTotal.set("已完成 ${finish_num}")
                    val list = arrayListOf<PieChartView.ItemType>()
                    val execSize = exec_num.toFloat() / all_num.toFloat()
                    val readySize = ready_num.toFloat() / all_num.toFloat()
                    val execSizeBigDecimal = BigDecimal(execSize.toString());
                    val execSizeFormat =
                        execSizeBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toFloat() * 100;

                    val readySizeBigDecimal = BigDecimal(readySize.toString());
                    val readySizeFormat =
                        readySizeBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toFloat() * 100;

                    val finishSizeFormat = 100 - execSizeFormat - readySizeFormat
                    list.add(
                        PieChartView.ItemType(
                            "$exec_num（${execSizeFormat.toInt()}%）".trimIndent(),
                            exec_num, Color.parseColor("#3ECC98"),
                            false
                        )
                    )
                    list.add(
                        PieChartView.ItemType(
                            "$ready_num（${readySizeFormat.toInt()}%）".trimIndent(),
                            ready_num, Color.parseColor("#999999"),
                            false
                        )
                    )
                    list.add(
                        PieChartView.ItemType(
                            "$finish_num（${finishSizeFormat.toInt()}%）".trimIndent(),
                            finish_num, Color.parseColor("#00A468"),
                            true
                        )
                    )
                    orderTotal.set(all_num.toString())
                    data.set(list)
                }

                override fun onFailed(msg: String?) {
                }
            })
    }

    fun workOrderPercent() {
        model.apply {
            this.workOrderPercent(type = type.get() ?: 1)
                .compose(RxThreadHelper.rxSchedulerHelper(this@WorkViewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<List<PercentBean>>>() {
                    override fun onResult(t: BaseBean<List<PercentBean>>) {
                        if (t.code == 200) {
                            uc.loadChatEvent.postValue(t.data)
                        } else {
                            showErrorToast(t.msg)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                    }
                })
        }
    }

    fun workOrderTime() {
        model.apply {
            this.workOrderTime()
                .compose(RxThreadHelper.rxSchedulerHelper(this@WorkViewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<List<WorkOrderTimeBean>>>() {
                    override fun onResult(t: BaseBean<List<WorkOrderTimeBean>>) {
                        if (t.code == 200) {
                            uc.loadOrderTimeEvent.postValue(t.data)
                        } else {
                            showErrorToast(t.msg)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                    }
                })
        }
    }
}