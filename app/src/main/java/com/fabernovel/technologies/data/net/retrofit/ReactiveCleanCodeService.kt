package com.fabernovel.technologies.data.net.retrofit

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface RandomUserReactiveService {
    @GET("api")
    fun getUsers(): Single<Response<RestUsers>>
}

