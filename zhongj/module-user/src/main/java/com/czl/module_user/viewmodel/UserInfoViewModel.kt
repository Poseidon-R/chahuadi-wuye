package com.czl.module_user.viewmodel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RegexUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.CarItem
import com.czl.base.data.bean.ImgRspBean
import com.czl.base.data.bean.RoomBean
import com.czl.base.data.bean.UserInfo
import com.czl.base.event.LiveBusCenter
import com.czl.base.event.SingleLiveEvent
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper

class UserInfoViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    private lateinit var userInfo: UserInfo
    val uc = UiChangeEvent()

    var avatarUrl = ObservableField("")
    var nickName = ObservableField("")
    var name = ObservableField("")
    var gender = ObservableInt(0)
    var phone = ObservableField("")
    var idCard = ObservableField("")
    var birthday = ObservableField("")
    var email = ObservableField("")
    var carNames = ObservableField("我的车辆")
    var roomNum = ObservableField("我的房间")

    class UiChangeEvent {
        val choiceImgEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val choiceGenderEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val choiceBirthdayEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val loadCarsCompleteEvent: SingleLiveEvent<List<CarItem>> = SingleLiveEvent()
        val loadRoomsEvent: SingleLiveEvent<List<RoomBean>> = SingleLiveEvent()

    }

    val onNickNameChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        nickName.set(it)
    })
    val onNameChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        name.set(it)
    })
    val onIdCardChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        idCard.set(it)
    })
    val onEmailChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        email.set(it)
    })

    val onAvatarClick: BindingCommand<Any> = BindingCommand(BindingAction {
        uc.choiceImgEvent.call()
    })

    val onGenderClick: BindingCommand<Any> = BindingCommand(BindingAction {
        uc.choiceGenderEvent.call()
    })
    val onBirthdayClick: BindingCommand<Any> = BindingCommand(BindingAction {
        uc.choiceBirthdayEvent.call()
    })

    val onSubmitClickCommand: BindingCommand<Any> = BindingCommand(BindingAction {
        changeUserInfo()
    })

    fun getUserInfo() {
        model.getLocalUserInfo()?.let {
            avatarUrl.set(it.avatarUrl)
            nickName.set(it.nickName)
            name.set(it.name)
            gender.set(it.gender!!)
            phone.set(it.phone)
            idCard.set(it.idCard)
            birthday.set(it.birthday)
            email.set(it.email)
        }

    }

    fun setGender(position: Int) {
        gender.set(position)
    }

    fun setBirthday(value: String) {
        birthday.set(value)
    }


    fun uploadHeadImg(imgSrc: String) {
        model.apply {
            uploadHeadImg(imgSrc)
                .compose(RxThreadHelper.rxSchedulerHelper(this@UserInfoViewModel))
                .doOnSubscribe { showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<ImgRspBean>>() {
                    override fun onResult(t: BaseBean<ImgRspBean>) {
                        dismissLoading()
                        if (t.code == 200) {
                            avatarUrl.set(t.data?.url)
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

    private fun changeUserInfo() {
        if (name.get().toString().length < 2) {
            showErrorToast("姓名不能少于两个字符")
            return
        }
        if (!email.get().isNullOrBlank()) {
            if (!RegexUtils.isEmail(email.get())) {
                showErrorToast("邮箱格式错误")
                return
            }
        }
        if (!idCard.get().isNullOrBlank()) {
            if (!RegexUtils.isIDCard18Exact(idCard.get())) {
                showErrorToast("身份证号格式错误")
                return
            }
        }
        userInfo = UserInfo(
            0,
            avatarUrl.get(),
            nickName.get(),
            name.get(),
            gender.get(),
            phone.get(),
            idCard.get(),
            birthday.get(),
            email.get(),
            "",
        )
        model.apply {
            changeUserInfo(userInfo)
                .compose(RxThreadHelper.rxSchedulerHelper(this@UserInfoViewModel))
                .doOnSubscribe { showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        dismissLoading()
                        showNormalToast(t.msg)
                        if (t.code == 200) {
                            LiveBusCenter.postModifyUserInfoEvent("")
                        }
                    }

                    override fun onFailed(msg: String?) {
                        dismissLoading()
                        LogUtils.i(msg)
                        showNormalToast(msg)
                    }

                })
        }
    }

    fun getCarList() {
        model.getMyCarList(false, model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this@UserInfoViewModel))
            .doOnSubscribe { showLoading() }
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<CarItem>>>() {
                override fun onResult(t: BaseBean<List<CarItem>>) {
                    dismissLoading()
                    if (t.code == 200) {
                        uc.loadCarsCompleteEvent.postValue(t.data)
                    } else {
                        uc.loadCarsCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                    dismissLoading()
                    uc.loadCarsCompleteEvent.postValue(null)
                }

            })
    }

    fun getUserRooms() {
        model.getUserRooms(model.getLoginPhone().toString(), model.getAreaId())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(
                object : ApiSubscriberHelper<BaseBean<List<RoomBean>>>() {
                    override fun onResult(t: BaseBean<List<RoomBean>>) {
                        if (t.code == 200) {
                            uc.loadRoomsEvent.postValue(t.data)
                            t.data?.let {
                                var rooms = Stream.of(it).map { e -> e.houseId + e.houseCode }
                                    .collect(Collectors.toList())
                                model.saveRoomIdAndCode(GsonUtils.toJson(rooms))
                            }

                        } else {
                            uc.loadRoomsEvent.postValue(null)
                            showErrorToast(t.msg)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        uc.loadRoomsEvent.postValue(null)
                        showErrorToast(msg)
                    }

                }
            )
    }
}