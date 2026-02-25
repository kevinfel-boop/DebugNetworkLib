package com.kevinfelicite.debugnetwork

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kevinfelicite.debugnetwork.okhttp.applyDebugConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient.Builder()
        .applyDebugConfig()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Appel réseau de test
        val request = Request.Builder()
            .url("https://httpbin.org/get")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("DebugTest", "Erreur : ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("DebugTest", "Réponse : ${response.code}")
                Log.d("DebugTest", "Body : ${response.body?.string()?.take(200)}")
            }
        })
    }
}