package com.czl.base.binding.viewadapter.view

import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.SizeUtils
import com.czl.base.binding.command.BindingCommand
import com.czl.base.extension.clickWithTrigger
import com.czl.base.extension.longClick
import com.czl.base.widget.PieChartView

object ViewAdapter {

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     * isThrottleFirst 是否开启防止过快点击
     */
    @JvmStatic
    @BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
    fun onClickCommand(view: View?, clickCommand: BindingCommand<*>?, isThrottleFirst: Boolean) {
        if (isThrottleFirst) {
            view?.setOnClickListener { clickCommand?.execute() }
        } else {
            view?.clickWithTrigger { clickCommand?.execute() }
        }
    }

    /**
     * 列表Item点击事件并携带item的数据
     *
     * @param view 点击事件相应的view
     * @param clickCommand 发起点击事件者
     * @param item 业务每个Item数据
     */
    @JvmStatic
    @BindingAdapter(value = ["onRvItemCommand", "rvItemBean"])
    fun onClickCommand(view: View?, clickCommand: BindingCommand<Any>?, item: Any) {
        view?.clickWithTrigger { clickCommand?.execute(item) }
    }

    /**
     * view的onLongClick事件绑定
     */
    @JvmStatic
    @BindingAdapter(value = ["onLongClickCommand"], requireAll = false)
    fun onLongClickCommand(view: View?, clickCommand: BindingCommand<*>?) {
        view?.longClick { clickCommand?.execute() }
    }

    /**
     * 回调控件本身
     *
     * @param currentView
     * @param bindingCommand
     */
    @JvmStatic
    @BindingAdapter(value = ["currentView"], requireAll = false)
    fun replyCurrentView(currentView: View?, bindingCommand: BindingCommand<View>?) {
        bindingCommand?.execute(currentView)
    }

    /**
     * view是否需要获取焦点
     */
    @JvmStatic
    @BindingAdapter("requestFocus")
    fun requestFocusCommand(view: View, needRequestFocus: Boolean) {
        if (needRequestFocus) {
            view.isFocusableInTouchMode = true
            view.requestFocus()
        } else {
            view.clearFocus()
        }
    }

    /**
     * view的焦点发生变化的事件绑定
     */
    @JvmStatic
    @BindingAdapter("onFocusChangeCommand")
    fun onFocusChangeCommand(view: View, onFocusChangeCommand: BindingCommand<Boolean?>?) {
        view.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            onFocusChangeCommand?.execute(hasFocus)
        }
    }

    /**
     * view的显示隐藏
     */
    @JvmStatic
    @BindingAdapter(value = ["isVisible"], requireAll = false)
    fun isVisible(view: View, visibility: Boolean) {
        if (visibility) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["isEnabled"], requireAll = false)
    fun isEnabled(view: View, enable: Boolean) {
        view.isEnabled = enable
    }

    @JvmStatic
    @BindingAdapter(value = ["isSelected"], requireAll = false)
    fun isSelected(view: View, isSelected: Boolean) {
        view.isSelected = isSelected
    }

    @JvmStatic
    @BindingAdapter("setChartViewData")
    fun setChartViewData(
        view: PieChartView,
        data: List<PieChartView.ItemType>?
    ) {
        if (data == null || data.isEmpty()) return
        view.setInnerRadius(0.7f)
        view.setItemTextSize(SizeUtils.sp2px(11f))
        data.forEach {
            view.addItemType(
                it
            )
        }
    }
}