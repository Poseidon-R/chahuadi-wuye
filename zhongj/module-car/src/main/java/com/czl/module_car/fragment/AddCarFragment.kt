package com.czl.module_car.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.util.DialogHelper
import com.czl.module_car.BR
import com.czl.module_car.R
import com.czl.module_car.databinding.ActivityAddCarBinding
import com.czl.module_car.viewmodel.AddCarViewModel
import java.util.*

@Route(path = AppConstants.Router.Car.F_ADDCAR)
class AddCarFragment : BaseFragment<ActivityAddCarBinding, AddCarViewModel>() {

    override fun initContentView(): Int {
        return R.layout.activity_add_car
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("新增车牌号")

        viewModel.getUserRooms()

        binding.carBoardView.setUpdateCarNumListener {
            viewModel.setCarNumber(it)
        }
    }

    override fun initViewObservable() {
        viewModel.uc.choiceRoomEvent.observe(this, {
            if (it.isNullOrEmpty()) {
                showErrorToast("暂无可选房间号")
                return@observe
            }
            DialogHelper.showBottomListDialog(
                requireContext(),
                Stream.of(it).map { e -> e.houseCode }.withoutNulls()
                    .collect(Collectors.toList()) as ArrayList<String>
            ) { pos, text ->
                viewModel.roomId = it[pos].houseId
                viewModel.roomName.set(text)
            }
        })
    }

    override fun onBackPressedSupport(): Boolean {
        return binding.carBoardView.keyboardDismiss()
    }


}