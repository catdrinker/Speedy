package com.drinker.speedy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer
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
            .callAdapter(LiveDataAdapter<Any>())
            .build()

        val service = speedy.getService(IService::class.java)

        val btn = findViewById<Button>(R.id.btn)

        btn.setOnClickListener {
            val loginCall = service.getLogin("loginwithme", "ph124356")
            loginCall.observe(this, Observer {
                if (it.status == ResultStatus.ERROR) {
                    Log.w("MainActivity", "status error ${it.exception}")
                } else if (it.status == ResultStatus.SUCCESS) {
                    Log.i("MainActivity", "status success ${it.resp}")
                }
            })
        }
    }
}
