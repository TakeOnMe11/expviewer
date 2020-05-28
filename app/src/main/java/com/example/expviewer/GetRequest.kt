package com.example.expviewer

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET


interface GetRequest {
    @GET("experiments_results")
    fun getAllExp(): Observable<Response<List<ExpData>>>
}