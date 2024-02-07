package com.example.testproject23

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
import retrofit2.http.Header

class EmailCodeActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private  lateinit var shPref: GlobalSharedPreferences
    private lateinit var _email:String
    private var codeResult: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_code)
        shPref = GlobalSharedPreferences(this)
        //получение значения почты
        _email = intent.getStringExtra("Email").toString()
        //для обработки нажатия enter в editText
        var editlast4 = findViewById<EditText>(R.id.edt4)
        editlast4.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkCode()
                true
            } else {
                false
            }
        }
        CountDownTimer()
    }
    fun CountDownTimer(){
        var txtViewTimer = findViewById<TextView>(R.id.textViewTimer)
        object: CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txtViewTimer.setText("Оправить код повторно можно\nбудет через " + millisUntilFinished/1000+ " секунд")

            }
            override fun onFinish() {
                txtViewTimer.setText("Отправить код повторно")
                txtViewTimer.setOnClickListener(){
                    onTick(60000)
                }
            }
        }.start()
    }

    private fun checkCode() {
        val edit1 = findViewById<EditText>(R.id.edt1)
        val edit2 = findViewById<EditText>(R.id.edt2)
        val edit3 = findViewById<EditText>(R.id.edt3)
        val edit4 = findViewById<EditText>(R.id.edt4)
        var code =
            edit1.text.toString() + edit2.text.toString() + edit3.text.toString() + edit4.text.toString()
        if (code.length == 4) {
            signInFromCode(code)
            startActivity(Intent(this@EmailCodeActivity, SplashActivity::class.java))
            finish()

        }
    }
    private fun signInFromCode(code: String) {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gsonBuilder = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .baseUrl("https://iis.ngknn.ru/NGKNN/МамшеваЮС/MedicMadlab/")
            .client(httpClient)
            .build()

        val requestApi = retrofit.create(ApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            codeResult = try {
//                val response = requestApi.postSignIn(_email, code).execute()
                val response = async { requestApi.postSignIn(_email, code) }.await().awaitResponse()
                if (response.isSuccessful){
                    var data:String = response.body()!!
                    shPref.token = data
                    Log.d("ResponseSendCode", data)
                }
                response.code()
            } catch (e: Exception) {
                Log.d("ResponseSendCodeErr", e.toString())
            }
        }
    }
}