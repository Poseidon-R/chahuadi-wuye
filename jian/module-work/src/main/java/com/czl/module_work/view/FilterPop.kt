package com.czl.module_work.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bigkoo.pickerview.view.TimePickerView
import com.czl.base.util.DialogHelper
import com.czl.module_work.R
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.core.DrawerPopupView
import java.util.*

import com.lxj.xpopup.XPopup

import android.widget.Toast
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.TimeUtils
import com.czl.base.callback.TimePickerListener
import com.czl.base.data.bean.TakeCarePageParams
import com.czl.base.widget.TimePickerPopup


/**
 * 创建日期：2021/12/28  14:08
 * 类说明:
 * @author：86152
 */
class FilterPop : DialogFragment() {

    private var listener: OnTimePickResultListener? = null
    private lateinit var cancelView: TextView

    private lateinit var queryView: TextView

    private lateinit var startTimeView: TextView

    private lateinit var endTimeView: TextView

    private lateinit var nameView: EditText

    private lateinit var orderView: EditText

    private lateinit var assetsView: EditText

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.work_dialog_take_care_filter, container, false)
        initPopupContent()
        return rootView
    }

    fun setFilter(filterParams: TakeCarePageParams.Data) {
        if (TextUtils.isEmpty(filterParams.startTime) &&
            TextUtils.isEmpty(filterParams.endTime) &&
            TextUtils.isEmpty(filterParams.handleUserName) &&
            TextUtils.isEmpty(filterParams.orderNo) &&
            TextUtils.isEmpty(filterParams.deviceNo)
        ) {
            return
        }
        startTimeView.text = filterParams.startTime
        endTimeView.text = filterParams.endTime
        nameView.setText(filterParams.handleUserName)
        orderView.setText(filterParams.orderNo)
        assetsView.setText(filterParams.deviceNo)
    }

    fun setOnResultListener(listener: OnTimePickResultListener) {
        this.listener = listener
    }

    fun initPopupContent() {
        if (context == null) return
        cancelView = rootView.findViewById(R.id.tv_cancel)
        queryView = rootView.findViewById(R.id.tv_query)
        startTimeView = rootView.findViewById(R.id.tv_start_time)
        endTimeView = rootView.findViewById(R.id.tv_end_time)
        nameView = rootView.findViewById(R.id.et_name)
        orderView = rootView.findViewById(R.id.et_order_no)
        assetsView = rootView.findViewById(R.id.et_assets_no)

        cancelView.setOnClickListener { dismiss() }
        startTimeView.setOnClickListener {
            val popup: TimePickerPopup =
                TimePickerPopup(requireContext()) //.setDefaultDate(date)  //设置默认选中日期
                    //                        .setYearRange(1990, 1999) //设置年份范围
                    //                        .setDateRange(date, date2) //设置日期范围
                    .setTimePickerListener(object : TimePickerListener {
                        override fun onTimeChanged(date: Date?) {
                            //时间改变
                        }

                        override fun onTimeConfirm(date: Date, view: View?) {
                            //点击确认时间
                            startTimeView.text = TimeUtils.date2String(date, "yyyy-MM-dd")
                        }
                    })

            XPopup.Builder(requireContext())
                .asCustom(popup)
                .show()
        }
        endTimeView.setOnClickListener {
            val popup: TimePickerPopup =
                TimePickerPopup(requireContext())
                    .setShowLabel(true)
                    .setTimePickerListener(object : TimePickerListener {
                        override fun onTimeChanged(date: Date?) {
                            //时间改变
                        }

                        override fun onTimeConfirm(date: Date, view: View?) {
                            //点击确认时间
                            endTimeView.text = TimeUtils.date2String(date, "yyyy-MM-dd")
                        }
                    })

            XPopup.Builder(requireContext())
                .asCustom(popup)
                .show()
        }
        queryView.setOnClickListener {
            val startTime = startTimeView.text.toString().trim()
            val endTime = endTimeView.text.toString().trim()
            val name = nameView.text.toString().trim()
            val orderNo = orderView.text.toString().trim()
            val assetsNo = assetsView.text.toString().trim()
            dismiss()
            listener?.query(startTime, endTime, name, orderNo, assetsNo)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setGravity(Gravity.RIGHT);
        val lp = dialog?.window?.attributes;
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        //这里就是直接去掉边距的代码。
        dialog?.window?.decorView?.setPadding(0, 0, 0, 0);
        lp?.width = SizeUtils.dp2px(257f);
        lp?.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog?.window?.attributes = lp;
    }

    interface OnTimePickResultListener {

        fun query(
            startTime: String,
            endTime: String,
            name: String,
            orderNo: String,
            assetsNo: String
        )

    }
}