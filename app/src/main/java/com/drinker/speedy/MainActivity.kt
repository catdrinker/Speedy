package com.drinker.speedy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import okhttp3.OkHttpClient
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
            loginCall.enqueue(object : com.drinker.speedy.Callback<Home> {
                override fun onResponse(call: com.drinker.speedy.Call<Home>?, response: Home?) {
                }

                override fun onFailure(call: com.drinker.speedy.Call<Home>?, e: IOException?) {
                }
            })
        }


    }
}
