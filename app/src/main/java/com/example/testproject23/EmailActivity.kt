package com.example.testproject23

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

class EmailActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var shPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_email)
        shPref = getPreferences(MODE_PRIVATE)
        editor = shPref.edit()
        init()
    }
    fun init(){
        var btnEmail = findViewById<Button>(R.id.btnEmail)
        var editText = findViewById<EditText>(R.id.editText)
        btnEmail.setOnClickListener()
        {
            if (isEmail(editText.text.toString())){
                sendEmail(editText.text.toString())
                val intent = Intent(this, EmailCodeActivity::class.java)
                //передача данных из одной активности в другую
                intent.putExtra("Email", editText.text.toString())
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Неверный email",Toast.LENGTH_LONG).show()
            }
        }
        editText.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                btnEmail.callOnClick()
                true
            } else {
                false
            }
        }
    }
    fun isEmail(charSequence: CharSequence): Boolean{
        return !TextUtils.isEmpty(charSequence) && android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()
    }
}