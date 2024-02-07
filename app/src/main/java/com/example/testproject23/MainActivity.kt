package com.example.testproject23

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    val splash: Int = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed(Runnable{
            startActivity(Intent(this@MainActivity,EmailActivity::class.java))
            finish()
        },splash.toLong())
    }
}