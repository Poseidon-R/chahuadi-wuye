package com.czl.module_park.fragment

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.MonthCardListBean
import com.czl.base.util.DialogHelper
import com.czl.module_park.BR
import com.czl.module_park.R
import com.czl.module_park.databinding.FragmentRenewCardBinding
import com.czl.module_park.view.quantitizer.AnimationStyle
import com.czl.module_park.view.quantitizer.QuantitizerListener
import com.czl.module_park.viewmodel.RenewCardViewModel
import android.content.Intent
import android.net.Uri
import com.czl.base.event.LiveBusCenter
import java.net.URLEncoder


@Route(path = AppConstants.Router.Park.F_RENEW_CARD)
class RenewCardFragment : BaseFragment<FragmentRenewCardBinding, RenewCardViewModel>() {

    var monthCard: MonthCardListBean.Data? = null

    override fun initContentView(): Int {
        return R.layout.fragment_renew_card
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        viewModel.tvTitle.set("月卡续费")
        monthCard =
            arguments?.getSerializable(AppConstants.BundleKey.MONTH_CARD_DATA) as MonthCardListBean.Data?
        monthCard?.let {
            viewModel.setMonthCard(it)
            viewModel.getMonthPrice(it.vehicleNo)
        }
        binding.hQ.apply {
            value = 0
            minValue = 0
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
    }

    override fun initViewObservable() {
        viewModel.uc.onPayClickEvent.observe(this, {
            startContainerActivity(AppConstants.Router.Pay.F_PAY_DETAIL, Bundle().apply {
                putString(AppConstants.BundleKey.PAY_ORDER_KEY, it)
            })
        })

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

    override fun onResume() {
        super.onResume()
        println("-------onResume")
    }

}