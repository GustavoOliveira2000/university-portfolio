package com.example.dailymoodapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        val appNameText = findViewById<TextView>(R.id.appNameText)
        val sloganText = findViewById<TextView>(R.id.sloganText)

        // Animasyonları yükle
        val splashAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)

        // Animasyonları başlat
        appNameText.startAnimation(splashAnimation)
        sloganText.startAnimation(splashAnimation)

        // 2 saniye sonra ana aktiviteye geç
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 2000)
    }
} 