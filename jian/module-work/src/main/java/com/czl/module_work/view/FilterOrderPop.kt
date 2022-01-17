package com.czl.module_work.view

import android.content.Context
import android.view.View
import android.widget.TextView
import com.czl.module_work.R
import com.lxj.xpopup.core.BottomPopupView


/**
 * 创建日期：2021/12/28  16:23
 * 类说明:
 * @author：86152
 */
class FilterOrderPop(context: Context) : BottomPopupView(context) {

    private lateinit var order1: View
    private lateinit var order2: View
    private lateinit var order3: View
    private lateinit var order4: View

    private var type = 0

    private var mListener: (Int) -> Unit = {}

    override fun getImplLayoutId(): Int = R.layout.work_dialog_filter_order

    fun setFilterResultListener(listener: (Int) -> Unit) {
        this.mListener = listener
    }

    override fun initPopupContent() {
        super.initPopupContent()
        findViewById<View>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }
        order1 = findViewById<TextView>(R.id.tv_order_1)
        order2 = findViewById<TextView>(R.id.tv_order_2)
        order3 = findViewById<TextView>(R.id.tv_order_3)
        order4 = findViewById<TextView>(R.id.tv_order_4)
        order1.setOnClickListener { setSelected(1) }
        order2.setOnClickListener { setSelected(2) }
        order3.setOnClickListener { setSelected(3) }
        order4.setOnClickListener { setSelected(4) }
        findViewById<View>(R.id.tv_confirm).setOnClickListener {
            dismiss()
            mListener.invoke(type)
        }
    }

    fun setSelected(type: Int) {
        order1.isSelected = type == 1
        order2.isSelected = type == 2
        order3.isSelected = type == 3
        order4.isSelected = type == 4
        this.type = type
    }
}