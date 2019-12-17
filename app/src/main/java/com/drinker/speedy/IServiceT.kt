package com.drinker.speedy

import androidx.lifecycle.LiveData
import com.drinker.annotation.*
import okhttp3.RequestBody
import okhttp3.ResponseBody

@Service
interface IServiceT {

    @Get("/home/login?name={source}&pwd={pwd}")
    fun getLogin(@Param("first") firstParam: String, @Param("second") secondParam: String): LiveData<Result<ResponseBody>>

    @Post("/user/sign_up")
    fun getSign(@Param("key") keyParam: String, @Param("value") valueParam: String): LiveData<Result<Home>>

    @Put("/user/sign")
    operator fun get(@Body body: RequestBody): LiveData<Result<Home>>

    @Delete("/user/index.jsp")
    fun deleteJsp(): LiveData<Result<Home>>

    @Delete("/user/deleteBody")
    fun deleteBody(@Body body: RequestBody): LiveData<Result<Home>>
}