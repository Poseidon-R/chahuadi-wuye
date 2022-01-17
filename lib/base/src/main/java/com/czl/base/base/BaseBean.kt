package com.czl.base.base

import java.io.Serializable


/**
 * @author Alwyn
 * @Date 2019/9/10
 * @Description
 */
class BaseBean<T> : Serializable {
    var data: T? = null
    var errorCode: Int = 0
    var errorMsg: String? = null
    var msg: String? = null
    var code: Int? = null


    companion object {
        private const val serialVersionUID = 5223230387175917834L
    }

    override fun toString(): String {
        return "BaseBean(data=$data, errorCode=$errorCode, errorMsg=$errorMsg), msg=$msg), code=$code)"
    }

}
