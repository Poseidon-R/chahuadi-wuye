package com.czl.base.util

import java.util.*

object DateHelper {

    fun computeEndDate(startDate: Date, num: Int): Date {
        val ca = Calendar.getInstance() // 得到一个Calendar的实例
        ca.time = startDate; // 设置时间为当前时间
        ca.add(Calendar.MONTH, +num) // 月份减1
        return ca.time // 结果
    }
}