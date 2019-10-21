package com.drinker.speedy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val speedy = Speedy.Builder()
            .baseUrl(BaseUrl.get("http://www.google.com"))
            .callFactory(client)
            .build()

        val service = speedy.getService(IService::class.java)

        val loginCall = service.getLogin("loginwithme", "ph124356")

        loginCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("MainActivity", "call" + call.isCanceled + call.cancel())
            }

            override fun onResponse(call: Call, response: Response) {
                val inputStream = response.body()?.byteStream()
                inputStream?.let {
                    val byteArray = ByteArray(1024)

                }
            }
        })

    }
}
