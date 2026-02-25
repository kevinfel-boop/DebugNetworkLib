package com.kevinfelicite.debugnetwork.cronet

import android.content.Context
import com.kevinfelicite.debugnetwork.core.DebugNetworkConfig
import org.chromium.net.CronetEngine

object CronetDebugConfigurator {

    fun buildEngine(
        context: Context,
        config: DebugNetworkConfig = DebugNetworkConfig.get()
    ): CronetEngine? {
        if (!config.enabled) return CronetProvider.getEngine(context)

        val engine = CronetProvider.getEngine(context) ?: return null

        // Cronet ne supporte pas de proxy direct via API publique.
        // On configure les variables d'environnement JVM pour forcer le proxy système.
        System.setProperty("http.proxyHost", config.proxyHost)
        System.setProperty("http.proxyPort", config.proxyPort.toString())
        System.setProperty("https.proxyHost", config.proxyHost)
        System.setProperty("https.proxyPort", config.proxyPort.toString())

        return engine
    }

    fun buildEngineWithQuicDisabled(
        context: Context,
        config: DebugNetworkConfig = DebugNetworkConfig.get()
    ): CronetEngine? {
        if (!config.enabled) return CronetProvider.getEngine(context)

        System.setProperty("http.proxyHost", config.proxyHost)
        System.setProperty("http.proxyPort", config.proxyPort.toString())
        System.setProperty("https.proxyHost", config.proxyHost)
        System.setProperty("https.proxyPort", config.proxyPort.toString())

        return try {
            CronetEngine.Builder(context)
                .enableQuic(false)
                .enableHttp2(true)
                .build()
        } catch (e: Exception) {
            CronetProvider.getEngine(context)
        }
    }

    fun getProviderInfo(): String {
        return when (CronetProvider.getProvider()) {
            CronetProvider.Provider.PLAY_SERVICES -> "Google Play Services Cronet"
            CronetProvider.Provider.EMBEDDED -> "Embedded Cronet"
            CronetProvider.Provider.NONE -> "Cronet non disponible"
        }
    }
}