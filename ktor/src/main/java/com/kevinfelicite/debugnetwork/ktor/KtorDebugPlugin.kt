package com.kevinfelicite.debugnetwork.ktor

import com.kevinfelicite.debugnetwork.core.DebugNetworkConfig
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import java.net.InetSocketAddress
import java.net.Proxy
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

fun HttpClientConfig<CIOEngineConfig>.applyDebugConfig(
    config: DebugNetworkConfig = DebugNetworkConfig.get()
) {
    if (!config.enabled) return

    engine {
        proxy = Proxy(
            Proxy.Type.HTTP,
            InetSocketAddress(config.proxyHost, config.proxyPort)
        )
        https {
            trustManager = object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }
        }
    }

    install(DefaultRequest) {
        header("X-Debug-App", config.appTag)
    }
}