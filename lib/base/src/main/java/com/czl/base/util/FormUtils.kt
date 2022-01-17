package com.czl.base.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object FormUtils {

    fun isPassword(password: String?): Boolean {
        val regex = "^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{8,12}\$"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(password)
        return m.matches()
    }

    fun formatMoney(money: Float): String {
        val decimalFormat = DecimalFormat("0.00") //构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(money) //format 返回的是字符串
    }


    @Throws(ParseException::class)
    fun dealDateFormat(oldDateStr: String?): String? {
        //此格式只有  jdk 1.7才支持  yyyy-MM-dd'T'HH:mm:ss.SSSXXX
        val df: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX") //yyyy-MM-dd'T'HH:mm:ss.SSSZ
        val date: Date = df.parse(oldDateStr)
        val df1 = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK)
        val date1: Date = df1.parse(date.toString())
        val df2: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //  Date date3 =  df2.parse(date1.toString());
        return df2.format(date1)
    }


}