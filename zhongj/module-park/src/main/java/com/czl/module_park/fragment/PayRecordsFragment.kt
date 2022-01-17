package com.czl.module_park.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.bigkoo.pickerview.view.TimePickerView
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.DialogHelper
import com.czl.module_park.BR
import com.czl.module_park.R
import com.czl.module_park.adapter.PayRecordAdapter
import com.czl.module_park.databinding.ActivityPayRecordsBinding
import com.czl.module_park.viewmodel.PayRecordsViewModel
import java.util.*

/**
 * 缴费记录
 */
@Route(path = AppConstants.Router.Park.F_PAY_RECORDS)
class PayRecordsFragment : BaseFragment<ActivityPayRecordsBinding, PayRecordsViewModel>() {

    private lateinit var mAdapter: PayRecordAdapter
    private lateinit var pvStartTime: TimePickerView
    private lateinit var pvEndTime: TimePickerView

    override fun initContentView(): Int {
        return R.layout.activity_pay_records
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("缴费记录")

        pvStartTime = DialogHelper.showDatePickDialog(
            requireContext(), "开始日期", Calendar.getInstance().apply { this[2020, 0] = 1 },
            Calendar.getInstance().apply { this[2000, 0] = 1 },
            Calendar.getInstance()
        ) { date, _ ->
            viewModel.setStartDate(date)
            binding.smartCommon.autoRefresh()
        }

        pvEndTime =
            DialogHelper.showDatePickDialog(
                requireContext(), "结束日期", Calendar.getInstance().apply { this[2020, 1] = 1 },
                Calendar.getInstance().apply { this[2000, 0] = 1 },
                Calendar.getInstance()
            ) { date, _ ->
                viewModel.setEndDate(date)
                binding.smartCommon.autoRefresh()
            }

        initAdapter()
        binding.smartCommon.autoRefresh()
    }

    private fun initAdapter() {
        mAdapter = PayRecordAdapter(this)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
    }

    override fun initViewObservable() {
        viewModel.uc.loadCompleteEvent.observe(this, { data ->
//            if (!data?.datas.isNullOrEmpty()){
//                viewModel.model.saveCacheListData(data!!.datas)
//            }
            handleRecyclerviewData(
                data == null,
                data?.records as MutableList<*>?,
                mAdapter,
                binding.ryCommon,
                binding.smartCommon,
                viewModel.currentPage,
                data?.current == data?.pages
            )
        })
        viewModel.uc.choiceStartDateCommand.observe(this, {
            pvStartTime.show()
        })
        viewModel.uc.choiceEndDateCommand.observe(this, {
            pvEndTime.show()
        })

        viewModel.uc.choiceStatusCommand.observe(this, {
            DialogHelper.showBottomListDialog(
                requireContext(),
                arrayListOf("全部", "待支付", "已完成", "已取消")
            ) { position, text ->
                viewModel.payStatus.set(text)
                when (position) {
                    0 -> viewModel.status = ""
                    1 -> viewModel.status = "0"
                    2 -> viewModel.status = "1"
                    3 -> viewModel.status = "-1"
                }
                binding.smartCommon.autoRefresh()
            }
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