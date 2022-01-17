package com.czl.base.binding.viewadapter.textview

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.KeyboardUtils
import com.czl.base.binding.command.BindingCommand
import com.lihang.ShadowLayout

object ViewAdapter {
    /**
     * TextView字体是否加粗
     */
    @JvmStatic
    @BindingAdapter(value = ["isBold"], requireAll = false)
    fun isBold(textView: TextView, isBold: Boolean) {
        val paint = textView.paint;
        paint.isFakeBoldText = isBold
    }

    /**
     * TextView字体是否加粗
     */
    @JvmStatic
    @BindingAdapter(value = ["setTextColor"], requireAll = false)
    fun setTextColor(textView: TextView, color: String) {
        textView.setTextColor(Color.parseColor(color))
    }

    /**
     * TextView字体是否加粗
     */
    @JvmStatic
    @BindingAdapter(value = ["setTextSize"], requireAll = false)
    fun setTextSize(textView: TextView, size: Float) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }
}

