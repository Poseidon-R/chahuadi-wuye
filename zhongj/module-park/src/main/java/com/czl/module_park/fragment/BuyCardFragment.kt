package com.czl.module_park.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.facade.annotation.Route
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.bigkoo.pickerview.view.TimePickerView
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.CarItem
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.DialogHelper
import com.czl.module_park.BR
import com.czl.module_park.R
import com.czl.module_park.databinding.FragmentBuyCardBinding
import com.czl.module_park.view.quantitizer.AnimationStyle
import com.czl.module_park.view.quantitizer.QuantitizerListener
import com.czl.module_park.viewmodel.BuyCardViewModel
import java.util.*

@Route(path = AppConstants.Router.Park.F_BUY_CARD)
class BuyCardFragment : BaseFragment<FragmentBuyCardBinding, BuyCardViewModel>() {

    private lateinit var pvStartTime: TimePickerView
    private var list: List<CarItem> = arrayListOf()

    override fun initContentView(): Int {
        return R.layout.fragment_buy_card
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    @SuppressLint("NewApi")
    override fun initData() {
        viewModel.tvTitle.set("购买月卡")
        pvStartTime =
            DialogHelper.showDatePickDialog(requireContext(), "开始日期",
                Calendar.getInstance(), Calendar.getInstance()) { date, _ ->
                viewModel.setStartDate(date)
            }

        binding.hQ.apply {
            value = 1
            minValue = 1
            buttonAnimationEnabled = false
            textAnimationStyle = AnimationStyle.FALL_IN
            setPlusIcon(R.mipmap.ic_plus)
            setMinusIcon(R.mipmap.ic_minus)
            setPlusIconBackgroundColor(R.color.trans)
            setMinusIconBackgroundColor(R.color.trans)
        }
        binding.hQ.setQuantitizerListener(object : QuantitizerListener {
            override fun onDecrease() {
                val quantity = binding.hQ.value
                viewModel.decreaseNum(quantity)
            }

            override fun onIncrease() {
                val quantity = binding.hQ.value
                viewModel.decreaseNum(quantity)
            }

        })
        viewModel.getCarList()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initViewObservable() {
        viewModel.uc.choiceCarCommand.observe(this, {
            if (list.isNullOrEmpty()) {
                DialogHelper.showNoChoiceCarDialog(requireContext()) {
                    startContainerActivity(AppConstants.Router.Car.F_ADDCAR)
                }
            } else {
                showChoiceCarPop()
            }
        })
        viewModel.uc.choiceStartDateCommand.observe(this, {
            pvStartTime.show()
        })

        viewModel.uc.loadMyCarEvent.observe(this, { it ->
            if (it?.isNotEmpty() == true) {
                list = it
                showChoiceCarPop()
            } else {
                DialogHelper.showNoChoiceCarDialog(requireContext()) {
                    startContainerActivity(AppConstants.Router.Car.F_ADDCAR)
                }
            }
        })

        viewModel.uc.onPayClickEvent.observe(this, {
            startContainerActivity(AppConstants.Router.Pay.F_PAY_DETAIL, Bundle().apply {
                putString(AppConstants.BundleKey.PAY_ORDER_KEY, it)
            })
        })

        LiveBusCenter.observeAddCarCompleteEvent(this) {
            viewModel.getCarList()
        }

        LiveBusCenter.observePayResultEvent(this, {
            when (it.payType) {
                AppConstants.Constants.PAY_SUCCESS_TYPE -> {
                    back()
                }
                AppConstants.Constants.PAY_FAIL_TYPE -> {
                    back()
                }
            }
        })

    }

    private fun showChoiceCarPop() {
        DialogHelper.showBottomListDialog(
            requireContext(),
            Stream.of(list).map { r -> r.vehicleNo }.withoutNulls()
                .collect(Collectors.toList()) as ArrayList<String>
        ) { _, text ->
            viewModel.getMonthPrice(text)
        }
    }

}