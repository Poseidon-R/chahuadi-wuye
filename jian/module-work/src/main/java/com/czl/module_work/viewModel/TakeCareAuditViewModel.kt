package com.czl.module_work.viewModel

import android.os.Bundle
import androidx.databinding.ObservableField
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.WorkOrderBean
import com.czl.base.event.SingleLiveEvent


/**
 * 创建日期：2021/12/28  10:27
 * 类说明:
 * @author：86152
 */
class TakeCareAuditViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var page = 1

    val pageSize = 5

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val clearOrderEvent: SingleLiveEvent<Void> = SingleLiveEvent()

        val changTab: SingleLiveEvent<Void> = SingleLiveEvent()

        val loadRoomEvent: SingleLiveEvent<List<WorkOrderBean.Data>> = SingleLiveEvent()
    }

    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
//        getMonthCardList()
    })

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        getUserRooms()
    })

    val btnToAuditClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK_TAKE_CARE_BATCH_DISPATCH, Bundle().apply {
            putInt(
                AppConstants.BundleKey.WORK_BATCH_ORDER_TYPE,
                TakeCareBatchDispatchViewModel.ORDER_TYPE_TAKE_CARE
            )
            putInt(
                AppConstants.BundleKey.WORK_BATCH_TYPE,
                TakeCareBatchDispatchViewModel.ORDER_BATCH_TYPE_AUDIT
            )
        })
    })

    val filterType = ObservableField(FILTER_TYPE_WAITING)

    val btnWaitingClick: BindingCommand<Void> = BindingCommand(BindingAction {
        page = 1
        uc.clearOrderEvent.call()
        filterType.set(MyOrderViewModel.FILTER_TYPE_WAITING)
        uc.changTab.call()
        getOrders()
    })
    val btnCompleteClick: BindingCommand<Void> = BindingCommand(BindingAction {
        page = 1
        uc.clearOrderEvent.call()
        filterType.set(MyOrderViewModel.FILTER_TYPE_COMPLETE)
        uc.changTab.call()
        getOrders()
    })

    fun getOrders() {
        if (filterType.get() == MyOrderViewModel.FILTER_TYPE_WAITING) {
            getWaitingOrders()
        } else {
            getCompleteOrders()
        }
    }

    fun getCompleteOrders() {
        val data =WorkOrderBean.Data(
            "2022-12-25T00:00:00.000+08:00",
            1,
            "1",
            true,
            "测试",
            "2022-12-25T00:00:00.000+08:00",
            1,
            "测试",
            "11111",
            2,
            2,
            2,
            "测试",
            ""
        )
        uc.loadRoomEvent.postValue(listOf(data))
    }

    fun getWaitingOrders() {
        val data =WorkOrderBean.Data(
            "2022-12-25T00:00:00.000+08:00",
            1,
            "1",
            true,
            "测试",
            "2022-12-25T00:00:00.000+08:00",
            1,
            "测试",
            "11111",
            1,
            2,
            3,
            "测试",
            ""
        )
        uc.loadRoomEvent.postValue(listOf(data))
    }

    override fun setToolbarRightClick() {

    }


    fun getUserRooms() {
//        val list = arrayListOf("", "", "", "")
//        val t = BaseBean<List<String>>()
//        t.data = list
//        uc.loadRoomEvent.postValue(t.data)
//        model.saveRoomIdAndCode(GsonUtils.toJson(list))
//        model.getUserRooms(model.getLoginPhone().toString(), model.getAreaId())
//            .compose(RxThreadHelper.rxSchedulerHelper(this))
//            .subscribe(
//                object : ApiSubscriberHelper<BaseBean<List<RoomBean>>>() {
//                    override fun onResult(t: BaseBean<List<RoomBean>>) {
//                        if (t.code == 200) {
//                            uc.loadRoomEvent.postValue(t.data)
//                            t.data?.let {
//                                var rooms = Stream.of(it).map { e -> e.houseId + e.houseCode }
//                                    .collect(Collectors.toList())
//                                model.saveRoomIdAndCode(GsonUtils.toJson(rooms))
//                            }
//
//                        } else {
//                            uc.loadRoomEvent.postValue(null)
//                            showErrorToast(t.msg)
//                        }
//                    }
//
//                    override fun onFailed(msg: String?) {
//                        uc.loadRoomEvent.postValue(null)
//                        showErrorToast(msg)
//                    }
//
//                }
//            )
    }

    companion object {
        const val FILTER_TYPE_WAITING = 1001;
        const val FILTER_TYPE_COMPLETE = 1003;
    }
}