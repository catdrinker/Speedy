package com.drinker.speedy

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.drinker.adapter.LiveDataAdapter
import com.drinker.adapter.ResultStatus
import com.drinker.converter.GsonConverterFactory
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
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
            .dispatcher(Dispatcher().apply {
                maxRequests = 20
            })
            .build()

        /*val retrofit = Retrofit.Builder()
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .client(client)
            .baseUrl("https://ditu.amap.com/service/pl/")
            .build()


        val create = retrofit.create(RetrofitService::class.java)*/

        val speedy = Speedy.Builder()
            .baseUrl(BaseHttpUrl.get("https://ditu.amap.com/service/pl/"))
            .callFactory(client)
            .converterFactroy(GsonConverterFactory.create())
            .callAdapter(LiveDataAdapter.create(true))
            .build()

        val service = speedy.getService(IService::class.java)

        val btn = findViewById<Button>(R.id.btn)

        btn.setOnClickListener {
            /*val loginCall = create.reqValue()

            loginCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.i("MainActivity", "resp ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("MainActivity", "resp $t")
                }

            })*/

            val loginCall = service.getLogin("", "")
            loginCall.observe(this, Observer {
                if (it.status == ResultStatus.FAILURE) {
                    Log.w("MainActivity", "status error ${it.exception}")
                } else if (it.status == ResultStatus.SUCCESS) {
                    Log.i("MainActivity", "status success ${it.response}")
                }
            })


            /* H.call("https://ditu.amap.com/service/pl/",client).observe(this, Observer {
                 if (it.status == ResultStatus.FAILURE) {
                     Log.w("MainActivity", "status error ${it.exception}")
                 } else if (it.status == ResultStatus.SUCCESS) {
                     Log.i("MainActivity", "status success ${it.response}")
                 }
             })*/
        }
    }
}
