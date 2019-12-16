package com.drinker.speedy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.IOException

class LiveDataAdapter<T>(private val isAsync: Boolean = true) :
    CallAdapter<T, LiveData<Result<T>>> {
    override fun adapt(call: Call<T>): LiveData<Result<T>> {
        val liveResult = if (isAsync) {
            EnqueueLiveResult<T>()
        } else {
            ExecuteLiveResult<T>()
        }
        return liveResult.service(call)
    }
}


enum class ResultStatus {
    SUCCESS,
    ERROR
}

data class Result<T>(var status: ResultStatus, val resp: T?, val exception: IOException? = null) {
    companion object {
        fun <T> response(resp: T): Result<T> {
            return Result(ResultStatus.SUCCESS, resp)
        }

        fun <T> failure(resp: T? = null, e: IOException?): Result<T> {
            return Result(ResultStatus.ERROR, resp, e)
        }
    }
}


interface LiveResult<T> {
    fun service(call: Call<T>): LiveData<Result<T>>
}


class EnqueueLiveResult<T> : LiveResult<T> {
    override fun service(call: Call<T>): LiveData<Result<T>> {
        val liveData = MutableLiveData<Result<T>>()
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: T) {
                liveData.postValue(Result.response(response))
            }

            override fun onFailure(call: Call<T>?, e: IOException?) {
                liveData.postValue(Result.failure(null, e))
            }

        })
        return liveData
    }
}

class ExecuteLiveResult<T> : LiveResult<T> {
    override fun service(call: Call<T>): LiveData<Result<T>> {
        val liveData = MutableLiveData<Result<T>>()
        try {
            val response = call.execute()
            liveData.value = Result.response(response.body)
        } catch (e: IOException) {

        }
        return liveData
    }
}