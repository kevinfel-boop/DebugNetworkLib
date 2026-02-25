package com.kevinfelicite.debugnetwork.core

data class DebugNetworkConfig(
    val proxyHost: String = "10.0.2.2",
    val proxyPort: Int = 8080,
    val appTag: String,
    val enabled: Boolean = true
) {
    companion object {

        @Volatile
        private var instance: DebugNetworkConfig? = null

        fun init(appTag: String, block: DebugNetworkConfig.() -> Unit = {}): DebugNetworkConfig {
            return DebugNetworkConfig(appTag = appTag).apply(block).also {
                instance = it
            }
        }

        fun get(): DebugNetworkConfig {
            return instance ?: error(
                "DebugNetworkConfig not initialized. Call DebugNetworkConfig.init() first."
            )
        }

        fun isInitialized(): Boolean = instance != null
    }
}