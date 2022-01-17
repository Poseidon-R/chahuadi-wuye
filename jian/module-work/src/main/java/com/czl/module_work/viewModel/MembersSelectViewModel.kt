package com.czl.module_work.viewModel

import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.MembersBean
import com.czl.base.data.bean.TakeCareDispatchBean
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper


/**
 * 创建日期：2021/12/28  15:27
 * 类说明:
 * @author：86152
 */
class MembersSelectViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadRoomEvent: SingleLiveEvent<List<MembersBean>> = SingleLiveEvent()
    }

    fun getUserRooms() {
        model.listUser()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<MembersBean>>>() {
                override fun onResult(t: BaseBean<List<MembersBean>>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        uc.loadRoomEvent.postValue(t.data)
                    } else {
                        uc.loadRoomEvent.postValue(null)
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadRoomEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }

}