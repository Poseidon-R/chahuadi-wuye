package com.czl.module_work.viewModel

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.ImgRspBean
import com.czl.base.data.bean.MembersBean
import com.czl.base.data.bean.PatrolOrderDetailBean
import com.czl.base.data.bean.WorkOrderDetailBean
import com.czl.base.event.LiveBusCenter
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper
import com.czl.base.util.ToastHelper


/**
 * 创建日期：2021/12/28  17:39
 * 类说明:
 * @author：86152
 */
class UploadCertificateViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val isAgree = ObservableField(false)

    val isReject = ObservableField(false)

    val remarkText = ObservableField<String>()

    val uc = UiChangeEvent()

    var img = ""

    val ids = arrayListOf<String>()

    var id = ""


    class UiChangeEvent {
        val choiceImgEvent: SingleLiveEvent<Void> = SingleLiveEvent()

        val addImageEvent: SingleLiveEvent<String> = SingleLiveEvent()
    }

    val btnSubmitClick: BindingCommand<Void> = BindingCommand(BindingAction {
        if(TextUtils.isEmpty(id)){
            submit()
        }else{
            submitAlarm()
        }
    })

    val btnAgreeClick: BindingCommand<Void> = BindingCommand(BindingAction {
        isAgree.set(true)
        isReject.set(false)
    })

    val btnRejectClick: BindingCommand<Void> = BindingCommand(BindingAction {
        isAgree.set(false)
        isReject.set(true)
    })

    fun setIds(ids: List<String>) {
        this.ids.addAll(ids)
    }

    fun setOrderId(id: String) {
        this.id = id
    }

    fun submit() {
        if (isAgree.get() == false && isReject.get() == false) {
            ToastHelper.showNormalToast("请选择状态!")
            return
        }
        if (TextUtils.isEmpty(img)) {
            ToastHelper.showNormalToast("请上传照片!")
            return
        }
        model.inspectTaskRegister(img, if (isAgree.get() == true) "2" else "3", ids)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                override fun onResult(t: BaseBean<Any>) {
                    if (t.code == 200) {
                        ToastHelper.showNormalToast("提交成功")
                        LiveBusCenter.postWorkOrderDetailRefreshEvent("")
                        finish()
                    } else {
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                }
            })
    }

    fun submitAlarm() {
        model.alarmTaskRegister(img, remarkText.get().toString(), id)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                override fun onResult(t: BaseBean<Any>) {
                    if (t.code == 200) {
                        ToastHelper.showNormalToast("提交成功")
                        LiveBusCenter.postWorkOrderDetailRefreshEvent("")
                        finish()
                    } else {
                        showErrorToast(t.msg)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                }
            })
    }

    fun uploadImg(imgSrc: String) {
        model.apply {
            uploadImg(imgSrc)
                .compose(RxThreadHelper.rxSchedulerHelper(this@UploadCertificateViewModel))
                .doOnSubscribe { showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<ImgRspBean>>() {
                    override fun onResult(t: BaseBean<ImgRspBean>) {
                        dismissLoading()
                        if (t.code == 200) {
                            img = t.data?.url ?: ""
                            uc.addImageEvent.postValue(t.data?.url ?: "")
                        } else {
                            showErrorToast(t.msg)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        dismissLoading()
                        showErrorToast(msg)
                    }
                })
        }
    }
}