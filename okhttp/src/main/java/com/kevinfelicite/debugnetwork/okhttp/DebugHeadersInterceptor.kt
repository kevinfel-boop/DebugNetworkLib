package com.kevinfelicite.debugnetwork.okhttp

import android.os.Build
import com.kevinfelicite.debugnetwork.core.DebugNetworkConfig
import okhttp3.Interceptor
import okhttp3.Response

class DebugHeadersInterceptor(
    private val config: DebugNetworkConfig
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("X-Debug-App", config.appTag)
            .header("X-Debug-Device", "${Build.MODEL} (API ${Build.VERSION.SDK_INT})")
            .header("X-Debug-Timestamp", System.currentTimeMillis().toString())
            .build()
        return chain.proceed(request)
    }
}