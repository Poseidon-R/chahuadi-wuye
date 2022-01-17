package com.czl.module_work.view

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.base.callback.TimePickerListener
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.MembersBean
import com.czl.base.mvvm.ui.ContainerFmActivity
import com.czl.base.widget.TimePickerPopup
import com.czl.module_work.R
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import java.util.*


/**
 * 创建日期：2021/12/28  16:23
 * 类说明:
 * @author：86152
 */
class OrderReceivingPop(context: Context) : BottomPopupView(context) {

    private lateinit var selectMemberView: TextView

    private lateinit var selectServiceTimeView: TextView

    private lateinit var remarkView: EditText

    private var mListener: OrderReceivingListener? = null

    override fun getImplLayoutId(): Int = R.layout.work_dialog_order_receiving

    private var mMember: MembersBean? = null

    private var serviceTime: String = ""

    fun setMember(member: MembersBean) {
        this.mMember = member
        selectMemberView.text = "协助人:" + member.nickName
    }

    fun setOrderReceivingListener(listener: OrderReceivingListener) {
        this.mListener = listener
    }

    override fun initPopupContent() {
        super.initPopupContent()
        selectMemberView = findViewById(R.id.tv_help_member)
        selectServiceTimeView = findViewById(R.id.tv_service_time)
        remarkView = findViewById(R.id.et_remark)
        findViewById<View>(R.id.tv_cancel).setOnClickListener { dismiss() }
        findViewById<View>(R.id.rl_help_member).setOnClickListener {
            val intent = Intent(context, ContainerFmActivity::class.java)
            intent.putExtra(
                ContainerFmActivity.FRAGMENT,
                AppConstants.Router.Work.F_WORK_MEMBERS_SELECT
            )
            ActivityUtils.startActivity(intent)
        }
        findViewById<View>(R.id.rl_service_time).setOnClickListener {
            val popup: TimePickerPopup =
                TimePickerPopup(context)
                    .setTimePickerListener(object : TimePickerListener {
                        override fun onTimeChanged(date: Date?) {
                            //时间改变
                        }

                        override fun onTimeConfirm(date: Date, view: View?) {
                            //点击确认时间
                            serviceTime = TimeUtils.date2String(date, "yyyy-MM-dd")
                            selectServiceTimeView.text =
                                "服务时间:" + serviceTime
                        }
                    })

            XPopup.Builder(context)
                .asCustom(popup)
                .show()
        }
        findViewById<View>(R.id.tv_confirm).setOnClickListener {
            if (TextUtils.isEmpty(serviceTime)) {
                ToastUtils.showShort("选择服务时间!")
                return@setOnClickListener
            }
            val note = remarkView.text.toString()
            mListener?.confirm(mMember, note, serviceTime)
            dismiss()
        }

    }

    interface OrderReceivingListener {

        fun confirm(member: MembersBean?, note: String, serviceTime: String)

    }
}
