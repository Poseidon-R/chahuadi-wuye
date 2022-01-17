package com.czl.module_login.bean

data class LoginParamsBean(
    val username: String,
    val password: String?,
    val code: String?,
    val pushBind: PushBind
) {
    data class PushBind(
        val deviceBrand: String,
        val deviceBrandToken: String,
        val deviceOs: Int,
        val token: String
    )
}