package com.example.testproject23

import android.content.ContentValues
import android.util.Log
import android.widget.EditText
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

public fun sendEmail(email: String) {

    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val httpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    val gsonBuilder = GsonBuilder()
        .setLenient()
        .create()
    val  retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
        .baseUrl("https://iis.ngknn.ru/NGKNN/МамшеваЮС/MedicMadlab/")
        .client(httpClient)
        .build()
    val requiresApi = retrofit.create(ApiService::class.java)
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = requiresApi.postEmail(email)
                .awaitResponse() //Запуск метода из ApiRequest с переданым параметром в Header (заголовок)
            if(response.isSuccessful){
                Log.d("Response", "Успешная отправка кода на почту")
            }
            else{
                Log.d("Response", "${response.code()}")
            }
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, e.toString()) // Вывод информации на консоль
        }
    }
}