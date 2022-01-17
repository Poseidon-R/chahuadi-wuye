package com.czl.module_car.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.czl.base.base.AppManager
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.RoomBean
import com.czl.base.event.LiveBusCenter
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

/**
 *  Author:xch
 *  Date:2021/9/10
 *  Do:
 */
class AddCarViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    val uc = UiChangeEvent()
    private var carNumber: String = ""
    private var roomList: List<RoomBean>? = null

    //是否只有一个房间号可选
    val onlyOne = ObservableBoolean(true)
    val roomName = ObservableField("")
    var roomId = ""

    class UiChangeEvent {
        val choiceRoomEvent: SingleLiveEvent<List<RoomBean>> = SingleLiveEvent()
    }

    val onAddClick: BindingCommand<Any> = BindingCommand(BindingAction {
        addPersonCar()
    })

    val onChoiceRoomClick: BindingCommand<Void> = BindingCommand(BindingAction {
        if (onlyOne.get()) return@BindingAction
        uc.choiceRoomEvent.postValue(roomList)
    })

    fun setCarNumber(number: String) {
        carNumber = number
    }

    fun getUserRooms() {
        model.getUserRooms(model.getLoginPhone().toString(), model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this@AddCarViewModel))
            .subscribe(
                object : ApiSubscriberHelper<BaseBean<List<RoomBean>>>() {
                    override fun onResult(t: BaseBean<List<RoomBean>>) {
                        if (t.code == 200) {
                            roomList = t.data
                            t.data?.let {
                                when (it.size) {
                                    1 -> {
                                        onlyOne.set(true)
                                        roomId = it[0].houseId
                                        roomName.set(it[0].houseCode)
                                    }
                                    else -> {
                                        onlyOne.set(false)
                                    }
                                }
                            }

                        } else {
                            showErrorToast(t.msg)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                    }

                }
            )
    }

    private fun addPersonCar() {
        if (carNumber.isNullOrEmpty()) {
            showNormalToast("请输入车牌号")
            return
        }
        if (roomId.isNullOrEmpty()) {
            showNormalToast("请选择房间")
            return
        }
        model.addPersonCar(
            mapOf(
                "houseId" to roomId,
                "vehicleNo" to carNumber,
                "areaId" to model.getAreaId()
            )
        )
            .compose(RxThreadHelper.rxSchedulerHelper(this@AddCarViewModel))
            .subscribe(
                object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        showNormalToast(t.msg)
                        if (t.code == 200) {
                            LiveBusCenter.postAddCarCompleteEvent("")
                            AppManager.instance.finishActivity()
                        }
                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                    }

                }
            )
    }
}