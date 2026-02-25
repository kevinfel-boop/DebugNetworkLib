package com.kevinfelicite.debugnetwork

import android.app.Application
import com.kevinfelicite.debugnetwork.core.DebugNetworkConfig

class DebugApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            DebugNetworkConfig.init(appTag = "DebugNetworkLib-Test-v1.0")
        }
    }
}