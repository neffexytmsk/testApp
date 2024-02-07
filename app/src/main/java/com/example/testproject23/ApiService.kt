package com.example.testproject23

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/SendCode")
    fun postEmail(@Header("User-email") email:String): Call<String>

    @GET("api/SignIn")
    fun postSignIn(@Header("User-email") email: String, @Header("User-code") code: String): Call<String>
}