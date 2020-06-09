package com.example.expviewer

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PostRequest {
    @POST("experiments_results/")
    fun postAllExp(@Body exp: ExpData): Observable<Response<ExpData>>
}