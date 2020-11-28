package com.vicente.tutorialfirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(92900)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }
}