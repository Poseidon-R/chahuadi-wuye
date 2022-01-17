package com.czl.base.util

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.afollestad.date.DatePicker
import com.afollestad.date.year
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.datetime.DateTimeCallback
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.TimeUtils
import com.czl.base.R
import com.czl.base.base.AppManager
import com.czl.base.base.BaseActivity
import com.czl.base.view.BottomListPopView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.lxj.xpopup.interfaces.OnSelectListener
import java.util.*

/**
 * @author Alwyn
 * @Date 2020/12/1
 * @Description
 */
object DialogHelper {
    fun showBaseDialog(
        context: Context,
        title: String,
        content: String,
        func: () -> Unit
    ): BasePopupView {
        return XPopup.Builder(context).asConfirm(title, content, OnConfirmListener(func)).show()
    }

    fun showVersionDialog(
        context: Context,
        title: String,
        content: String,
        func: () -> Unit
    ): BasePopupView {
        return XPopup.Builder(context)
            .asConfirm(title, content, "取消", "下载", OnConfirmListener(func), null, false).show()
    }

    fun showNoChoiceCarDialog(
        context: Context,
        func: () -> Unit
    ): BasePopupView {
        return XPopup.Builder(context)
            .isDestroyOnDismiss(true)
            .asConfirm(
                "暂无车辆信息", "快去添加我的爱车吧~",
                "", "立即添加",
                func, null, true
            ).show()
    }

    fun showBottomListDialog(
        context: Context,
        title: String,
        list: Array<String>,
        func: (position: Int, text: String) -> Unit
    ): BasePopupView {
        return XPopup.Builder(context).asBottomList(title, list, OnSelectListener(func)).show()
    }

    fun showBottomListDialog(
        context: Context,
        list: ArrayList<String>?,
        func: (position: Int, text: String) -> Unit
    ): BasePopupView {
        return XPopup.Builder(context)
            .asCustom(
                BottomListPopView(
                    context,
                    list,
                    func
                )
            )
            .show()
    }


    fun showLoadingDialog(context: Context, title: String? = "加载中"): BasePopupView {
        return XPopup.Builder(context).asLoading(title, R.layout.common_loading_dialog).show()
    }

    fun showNoCancelDialog(
        context: Context,
        title: String,
        content: String,
        isDismissOnTouchOutside: Boolean? = true,
        func: () -> Unit,
    ): BasePopupView {
        return XPopup.Builder(context)
            .dismissOnTouchOutside(isDismissOnTouchOutside)
            .dismissOnBackPressed(isDismissOnTouchOutside)
            .asConfirm(title, content, "取消", "确定", OnConfirmListener(func), null, true).show()
    }

    fun showNomarlDialog(
        context: Context,
        title: String,
        content: String,
        onconfirm: () -> Unit,
        oncancle: () -> Unit
    ): BasePopupView {
        return XPopup.Builder(context)
            .asConfirm(title, content, "取消", "确定", onconfirm, oncancle, false).show()
    }

    fun showDateDialog(
        activity: BaseActivity<*, *>,
        dateStr: String? = null,
        dateTimeCallback: DateTimeCallback
    ) {
        MaterialDialog(activity)
            .show {
                noAutoDismiss()
                getActionButton(WhichButton.POSITIVE).updateTextColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.md_theme_red
                    )
                )
                getActionButton(WhichButton.NEGATIVE).updateTextColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.md_grey
                    )
                )
                neutralButton(text = "回到今天", click = object : DialogCallback {
                    override fun invoke(dialog: MaterialDialog) {
                        dialog.findViewById<DatePicker>(R.id.datetimeDatePicker)
                            .setDate(Calendar.getInstance().apply { time = Date() })
                    }
                })
                negativeButton { dismiss() }
                datePicker(
                    currentDate = if (dateStr.isNullOrEmpty()) null else Calendar.getInstance()
                        .apply {
                            time = TimeUtils.string2Date(dateStr, "yyyy-MM-dd")
                        }, dateCallback = dateTimeCallback
                )
                lifecycleOwner(activity)
            }
    }

    fun showDatePickDialog(
        context: Context,
        textContentTitle: String,
        startDefaultYear: Calendar = Calendar.getInstance().apply { this[1990, 0] = 1 },
        startDate: Calendar? = Calendar.getInstance().apply { this[1901, 0] = 1 },
        endDate: Calendar? = Calendar.getInstance().apply { this.year = this.year + 1 },
        func: (date: Date, v: View?) -> Unit
    ): TimePickerView {
        val textColor: Int = context.resources.getColor(R.color.color_00A468)
        return TimePickerBuilder(context, func).apply {
            setType(
                booleanArrayOf(true, true, true, false, false, false)
            )
            setCancelText("取消")
            setSubmitText("确认")
            setContentTextSize(18)
            setTitleText(textContentTitle)
            setCancelColor(textColor)
            setSubmitColor(textColor)
            setTitleSize(20)
            setDate(startDefaultYear)
            setRangDate(startDate, endDate)
        }.build()
    }
}