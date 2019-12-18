package com.drinker.speedy

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.drinker.adapter.LiveDataAdapter
import com.drinker.adapter.ResultStatus
import okhttp3.OkHttpClient
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
            .callAdapter(LiveDataAdapter.create(true))
            .build()

        val service = speedy.getService(IService::class.java)

        val btn = findViewById<Button>(R.id.btn)

        btn.setOnClickListener {
            val loginCall = service.getLogin("loginwithme", "ph124356")
            loginCall.observe(this, Observer {
                if (it.status == ResultStatus.FAILURE) {
                    Log.w("MainActivity", "status error ${it.exception}")
                } else if (it.status == ResultStatus.SUCCESS) {
                    Log.i("MainActivity", "status success ${it.response}")
                }
            })
        }
    }
}
