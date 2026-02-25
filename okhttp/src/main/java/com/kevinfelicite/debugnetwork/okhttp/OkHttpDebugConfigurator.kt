package com.kevinfelicite.debugnetwork.okhttp

import com.kevinfelicite.debugnetwork.core.DebugNetworkConfig
import okhttp3.OkHttpClient
import java.net.InetSocketAddress
import java.net.Proxy
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object OkHttpDebugConfigurator {

    fun configure(
        builder: OkHttpClient.Builder,
        config: DebugNetworkConfig = DebugNetworkConfig.get()
    ): OkHttpClient.Builder {
        if (!config.enabled) return builder

        val trustManager = trustAllTrustManager()
        val sslContext = SSLContext.getInstance("SSL").apply {
            init(null, arrayOf<TrustManager>(trustManager), java.security.SecureRandom())
        }

        return builder
            .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(config.proxyHost, config.proxyPort)))
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(DebugHeadersInterceptor(config))
    }

    private fun trustAllTrustManager(): X509TrustManager = object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }
}

// Extension Kotlin pour usage fluent
fun OkHttpClient.Builder.applyDebugConfig(
    config: DebugNetworkConfig = DebugNetworkConfig.get()
): OkHttpClient.Builder = OkHttpDebugConfigurator.configure(this, config)