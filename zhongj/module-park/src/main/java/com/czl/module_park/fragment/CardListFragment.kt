package com.czl.module_park.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.czl.module_park.BR
import com.czl.module_park.R
import com.czl.module_park.adapter.CardAdapter
import com.czl.module_park.databinding.ActivityCardListBinding
import com.czl.module_park.viewmodel.CardListViewModel

/**
 * 月卡列表
 */
@Route(path = AppConstants.Router.Park.F_CAR_LIST)
class CardListFragment : BaseFragment<ActivityCardListBinding,CardListViewModel>() {

    private lateinit var mAdapter: CardAdapter

    override fun initContentView(): Int {
        return R.layout.activity_card_list
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("月卡列表")

        initAdapter()

        reload()
    }

    private fun initAdapter(){
        mAdapter = CardAdapter(this)
        binding.ryCommon.apply{
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
    }

    override fun initViewObservable() {
        viewModel.uc.loadCompleteEvent.observe(this, { data ->
            handleRecyclerviewData(
                data == null,
                data?.list as MutableList<*>?,
                mAdapter,
                binding.ryCommon,
                binding.smartCommon,
                viewModel.currentPage,
                data?.isLastPage
            )
        })
        LiveBusCenter.observePayResultEvent(this, {
            when (it.payType) {
                AppConstants.Constants.PAY_SUCCESS_TYPE -> {
                    reload()
                }
                AppConstants.Constants.PAY_FAIL_TYPE -> {
                    back()
                }
            }
        })
    }

    override fun reload() {
        super.reload()
        binding.smartCommon.autoRefresh()
    }
}