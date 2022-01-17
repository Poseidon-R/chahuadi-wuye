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
class TransferFormPop(context: Context) : BottomPopupView(context) {

    private lateinit var selectMemberView: TextView

    private lateinit var remarkView: EditText

    private var mListener: TransferFormPop.OrderTransferListener? = null

    private var mMember: MembersBean? = null

    override fun getImplLayoutId(): Int = R.layout.work_dialog_transfer_form

    fun setMember(member: MembersBean) {
        this.mMember = member
        selectMemberView.text = "协助人:" + member.nickName
    }

    fun setOrderTransferListener(listener: TransferFormPop.OrderTransferListener) {
        this.mListener = listener
    }

    override fun initPopupContent() {
        super.initPopupContent()
        selectMemberView = findViewById(R.id.tv_help_member)
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
        findViewById<View>(R.id.tv_confirm).setOnClickListener {
            if (mMember == null) {
                ToastUtils.showShort("清选择转单人!")
                return@setOnClickListener
            }
            val note = remarkView.text.toString()
            mListener?.confirm(mMember!!, note)
            dismiss()
        }

    }


    interface OrderTransferListener {

        fun confirm(member: MembersBean, note: String)

    }
}