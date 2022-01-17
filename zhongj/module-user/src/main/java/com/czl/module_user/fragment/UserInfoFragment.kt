package com.czl.module_user.fragment

import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.TimeUtils
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.DialogHelper
import com.czl.base.util.GlideEngine
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.ActivityUserInfoBinding
import com.czl.module_user.viewmodel.UserInfoViewModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import java.util.*


@Route(path = AppConstants.Router.User.F_USER_INFO)
class UserInfoFragment : BaseFragment<ActivityUserInfoBinding, UserInfoViewModel>() {

    private lateinit var pvTime: TimePickerView

    override fun initContentView(): Int {
        return R.layout.activity_user_info
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("业主信息")
        binding.btnMyCar.setOnClickListener {
            startContainerActivity(AppConstants.Router.Car.F_MYCAR)
        }
        binding.btnMyRoom.setOnClickListener {
            startContainerActivity(AppConstants.Router.Room.F_MYROOM)
        }

        pvTime =
            DialogHelper.showDatePickDialog(requireContext(), "日期") { date, _ ->
                viewModel.setBirthday(TimeUtils.date2String(date, "yyyy-MM-dd"))
            }

        viewModel.getUserInfo()
        viewModel.getCarList()
        viewModel.getUserRooms()
    }


    override fun initViewObservable() {
        viewModel.uc.choiceGenderEvent.observe(this, {
            XPopup.Builder(context)
                .isDestroyOnDismiss(true)
                .asBottomList(
                    "请选择", arrayOf("男", "女")
                ) { position: Int, _: String? ->
                    viewModel.setGender(position)
                }.show()
        })

        viewModel.uc.choiceBirthdayEvent.observe(this, {
            if (viewModel.birthday.get().toString().matches(Regex("^\\d*-\\d*-\\d*\$"))){
                val date=viewModel.birthday.get().toString().split("-")
                pvTime.setDate(Calendar.getInstance().apply { this[date[0].toInt(),(date[1].toInt()-1)]=date[2].toInt() })
            }
            pvTime.show()
        })

        viewModel.uc.choiceImgEvent.observe(this, {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isCompress(true)
                .selectionMode(PictureConfig.SINGLE)
                .minimumCompressSize(100)
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: List<LocalMedia?>) {
                        result[0]?.let { it -> viewModel.uploadHeadImg(it.compressPath) }
                    }

                    override fun onCancel() {
                        // onCancel Callback
                    }
                })
        })

        viewModel.uc.loadCarsCompleteEvent.observe(this, {
            if (it.isNullOrEmpty()) return@observe
            var vehicleNos = Stream.of(it).map { e -> e.vehicleNo }.withoutNulls()
                .collect(Collectors.toList())
            vehicleNos?.let {
                val join = TextUtils.join("、", vehicleNos)
                viewModel.carNames.set(join)
            }
        })
        viewModel.uc.loadRoomsEvent.observe(this,{
            if (it.isNullOrEmpty())return@observe
            var roomNum=Stream.of(it).map { r->r.houseCode }.withoutNulls().collect(Collectors.toList())
            roomNum?.let {
                val rs = TextUtils.join("、", roomNum)
                viewModel.roomNum.set(rs)
            }
        })

        LiveBusCenter.observeAddCarCompleteEvent(this) {
            viewModel.getCarList()
        }
    }
}