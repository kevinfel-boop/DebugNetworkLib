package com.kevinfelicite.debugnetwork.cronet

import android.content.Context
import org.chromium.net.CronetEngine

object CronetProvider {

    enum class Provider { PLAY_SERVICES, EMBEDDED, NONE }

    private var cachedEngine: CronetEngine? = null
    private var cachedProvider: Provider = Provider.NONE

    fun getEngine(context: Context): CronetEngine? {
        if (cachedEngine != null) return cachedEngine

        // Tentative 1 : Google Play Services
        tryPlayServices(context)?.let {
            cachedEngine = it
            cachedProvider = Provider.PLAY_SERVICES
            return it
        }

        // Tentative 2 : Embedded Cronet
        tryEmbedded(context)?.let {
            cachedEngine = it
            cachedProvider = Provider.EMBEDDED
            return it
        }

        cachedProvider = Provider.NONE
        return null
    }

    fun getProvider(): Provider = cachedProvider

    private fun tryPlayServices(context: Context): CronetEngine? {
        return try {
            val providerClass = Class.forName(
                "com.google.android.gms.net.CronetProviderInstaller"
            )
            // Vérifie que Play Services Cronet est disponible
            val builderClass = Class.forName(
                "com.google.android.gms.net.GmsCronetEngine\$Builder"
            )
            val constructor = builderClass.getConstructor(Context::class.java)
            val builder = constructor.newInstance(context)
            val buildMethod = builderClass.getMethod("build")
            buildMethod.invoke(builder) as? CronetEngine
        } catch (e: Exception) {
            null
        }
    }

    private fun tryEmbedded(context: Context): CronetEngine? {
        return try {
            CronetEngine.Builder(context).build()
        } catch (e: Exception) {
            null
        }
    }

    fun reset() {
        cachedEngine?.shutdown()
        cachedEngine = null
        cachedProvider = Provider.NONE
    }
}