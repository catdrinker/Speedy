package com.drinker.converter

import com.drinker.speedy.TypeToken2
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test

class MoshiConverterFactoryTestKt {


    private lateinit var moshiConverterFactory: MoshiConverterFactory
    private lateinit var user: User<Home>


    @Before
    @Throws(Exception::class)
    fun setUp() {
        val home = Home(1, "hahaah")
        user = User(home, "1321", "12321321")
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        moshiConverterFactory = MoshiConverterFactory.create(moshi)
    }

    @Test
    fun reqBodyConverterTest() {
        val value =
            "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\",\"value\":\"12321321\"}"
        val token2 = object : TypeToken2<User<Home>>() {
        }
        val reqBodyConverter = moshiConverterFactory.reqBodyConverter(token2)
        val reqBody = reqBodyConverter.transform(user)

        assert(reqBody != null)

        reqBody!!.let {
            assert(it.contentLength() == value.length.toLong())
            assert(MediaType.get("application/json; charset=UTF-8") == it.contentType())
        }
    }

    @Test
    fun respBodyConverterTest() {
        val value =
            "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\"}"
        val token2 = object : TypeToken2<User<Home>>() {
        }
        val respBodyConverter = moshiConverterFactory.respBodyConverter(token2)

        val responseBody = ResponseBody.create(MultipartBody.FORM, value)
        val homeUser =
            respBodyConverter.transform(responseBody)

        assert(homeUser != null)
        homeUser!!
        assert(homeUser.data.value == 1)
        assert(homeUser.data.h == "hahaah")
        assert(homeUser.name == "1321")
        assert(homeUser.value == null)
    }

    @Test
    fun respBodyConverterNotNullTest() {
        val value =
            "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\",\"value\":\"12321321\"}"
        val token2 = object : TypeToken2<User<Home>>() {
        }
        val respBodyConverter = moshiConverterFactory.respBodyConverter(token2)

        val responseBody = ResponseBody.create(MultipartBody.FORM, value)
        val homeUser =
            respBodyConverter.transform(responseBody)

        assert(homeUser != null)
        homeUser!!
        assert(homeUser.data.value == 1)
        assert(homeUser.data.h == "hahaah")
        assert(homeUser.name == "1321")
        assert(homeUser.value == "12321321")
    }


    data class Home(val value: Int = 0, val h: String)

    data class User<T>(val data: T, val name: String, val value: String?)

}