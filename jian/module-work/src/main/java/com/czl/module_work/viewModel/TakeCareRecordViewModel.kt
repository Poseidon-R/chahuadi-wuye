package com.czl.module_work.viewModel

import androidx.databinding.ObservableField
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.blankj.utilcode.util.GsonUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.*
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper


/**
 * 创建日期：2021/12/27  14:04
 * 类说明:
 * @author：86152
 */
class TakeCareRecordViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var page = 1

    val pageSize = 10

    var filterParams = ObservableField<String>()

    val cancelClick = BindingCommand<Void>(BindingAction {
        filterParams.set("")
        refresh()
    })
    val search = BindingCommand<String> { t ->
        filterParams.set(t ?: "")
        refresh()
    }

    val uc = UiChangeEvent()

    class UiChangeEvent {

        val loadOrderEvent: SingleLiveEvent<List<TakeCareRecordBean.Data>> = SingleLiveEvent()

        val clearOrderEvent: SingleLiveEvent<Void> = SingleLiveEvent()

    }

    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
        loadNext()
    })

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        refresh()
    })

    override fun setToolbarRightClick() {

    }

    fun refresh() {
        page = 1
        uc.clearOrderEvent.call()
        getRecords()
    }

    fun loadNext() {
        page++
        getRecords()
    }

    fun getRecords() {
        model.takeCareOrderRecord(pageNum = page, pageSize = pageSize, filterParams.get()?:"")
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<TakeCareRecordBean>>() {
                override fun onResult(t: BaseBean<TakeCareRecordBean>) {
                    if (t.data == null) return
                    if (t.code == 200) {
                        uc.loadOrderEvent.postValue(t.data!!.list)
                    } else {
                        uc.loadOrderEvent.postValue(null)
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadOrderEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }
}