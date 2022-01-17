package com.czl.base.data.net

import com.czl.base.data.DataRepository
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

class TokenInterceptor(private val headers : Map<String, String>?): Interceptor, KoinComponent {
    private val dataRepository by inject<DataRepository>()
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request()
            .newBuilder()
        val token: String? = dataRepository.getLoginToken()
        if(!token.isNullOrBlank()){
            builder.addHeader("Authorization", token).build()
        }
        if (headers != null && headers.isNotEmpty()) {
            val keys = headers.keys
            for (headerKey in keys) {
                builder.addHeader(headerKey, headers[headerKey]).build()
            }
        }
        //请求信息
        return chain.proceed(builder.build())
    }
}