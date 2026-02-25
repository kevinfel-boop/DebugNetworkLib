package com.kevinfelicite.debugnetwork.nativebridge

import com.kevinfelicite.debugnetwork.core.DebugNetworkConfig

object CurlDebugBridge {

    private var configured = false

    fun configure(config: DebugNetworkConfig = DebugNetworkConfig.get()) {
        if (!config.enabled || configured) return

        val success = nativeConfigureProxy(config.proxyHost, config.proxyPort)
        if (success) {
            configured = true
        }
    }

    fun reset() {
        nativeReset()
        configured = false
    }

    private external fun nativeConfigureProxy(host: String, port: Int): Boolean
    private external fun nativeReset()

    init {
        try {
            System.loadLibrary("debugcurl")
        } catch (e: UnsatisfiedLinkError) {
            // La lib native n'est pas présente — mode dégradé sans crash
        }
    }
}