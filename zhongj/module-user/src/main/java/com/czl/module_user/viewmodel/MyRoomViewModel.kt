package com.czl.module_user.viewmodel

import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.blankj.utilcode.util.GsonUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.RoomBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

class MyRoomViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadRoomEvent: SingleLiveEvent<List<RoomBean>> = SingleLiveEvent()
    }

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        getUserRooms()
    })


    fun getUserRooms() {
        model.getUserRooms(model.getLoginPhone().toString(), model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(
                object : ApiSubscriberHelper<BaseBean<List<RoomBean>>>() {
                    override fun onResult(t: BaseBean<List<RoomBean>>) {
                        if (t.code == 200) {
                            uc.loadRoomEvent.postValue(t.data)
                            t.data?.let {
                                var rooms = Stream.of(it).map { e -> e.houseId + e.houseCode }
                                    .collect(Collectors.toList())
                                model.saveRoomIdAndCode(GsonUtils.toJson(rooms))
                            }

                        } else {
                            uc.loadRoomEvent.postValue(null)
                            showErrorToast(t.msg)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        uc.loadRoomEvent.postValue(null)
                        showErrorToast(msg)
                    }

                }
            )
    }
}