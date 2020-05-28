package com.example.expviewer

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClientInstance {
    private val BASE_URL = "http://192.168.0.53:8000/"
    private var getRequest: GetRequest? = null

    init {
        val rertofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        getRequest = rertofit.create(GetRequest::class.java)
    }

    fun getAllExp(): Observable<Response<List<ExpData>>> = getRequest!!.getAllExp()

    companion object {
        private var instance: RetrofitClientInstance? = null
        fun getInstance(): RetrofitClientInstance {
            if (instance == null) {
                instance = RetrofitClientInstance()
            }
            return instance as RetrofitClientInstance
        }
    }
}