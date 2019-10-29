package com.drinker.speedy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        val speedy = Speedy.Builder()
            .baseUrl(BaseHttpUrl.get("https://www.baidu.com"))
            .callFactory(client)
            .build()

        val service = speedy.getService(IService::class.java)

        val btn = findViewById<Button>(R.id.btn)

        btn.setOnClickListener {
            val loginCall = service.getLogin("loginwithme", "ph124356")
            loginCall.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("MainActivity", "fail call" + call.isCanceled + call + e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.e(
                        "MainActivity",
                        "call" + call.isCanceled + call.cancel() + response.isSuccessful
                    )

                    Log.i("MainActivity", "response $response")
                }
            })
        }


    }
}
