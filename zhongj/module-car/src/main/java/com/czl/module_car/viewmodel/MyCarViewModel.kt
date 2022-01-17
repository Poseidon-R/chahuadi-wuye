package com.czl.module_car.viewmodel

import androidx.databinding.ObservableBoolean
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.CarItem
import com.czl.base.event.LiveBusCenter
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

class MyCarViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()
    val isEdit = ObservableBoolean(false)
    private var deleteList: MutableList<String> = ArrayList()

    class UiChangeEvent {
        val loadCompleteEvent: SingleLiveEvent<List<CarItem>> = SingleLiveEvent()
        val touchEditEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
        val deleteCarEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        getCarList()
    })

    val onActionClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Car.F_ADDCAR)
    })
    val onDeleteCarClick: BindingCommand<Void> = BindingCommand(BindingAction {
        if (deleteList.isEmpty()) {
            showNormalToast("请选择车辆")
            return@BindingAction
        }
        uc.deleteCarEvent.call()
    })

    fun setDeleteCarList(list: List<CarItem>) {
        deleteList.clear()
        deleteList = Stream.of(list).map { e -> e.vehicleNo }
            .collect(Collectors.toList()) as MutableList<String>
    }

    override fun setToolbarRightClick() {
        setEdit(!isEdit.get())
    }

    fun setEdit(edit: Boolean) {
        isEdit.set(edit)
        toolbarRightText.set(if (edit) "完成" else "编辑")
        uc.touchEditEvent.postValue(edit)
    }

    private fun getCarList() {
        model.getMyCarList(false, model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this@MyCarViewModel))
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<CarItem>>>() {
                override fun onResult(t: BaseBean<List<CarItem>>) {
                    if (t.code == 200) {
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

    fun deleteCarList() {
        if (deleteList.isEmpty()) {
            showNormalToast("请选择车辆")
            return
        }
        model.deleteCarList(deleteList, model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this@MyCarViewModel))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                override fun onResult(t: BaseBean<Any?>) {
                    if (t.code == 200) {
                        showNormalToast("删除成功")
                        LiveBusCenter.postAddCarCompleteEvent("")
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


