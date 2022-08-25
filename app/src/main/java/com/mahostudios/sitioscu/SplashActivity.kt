package com.mahostudios.sitioscu

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.constraintlayout.widget.ConstraintLayout

class SplashActivity : AppCompatActivity() {

    private lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        preferences = getSharedPreferences("selected_start", MODE_PRIVATE)
        var sel : Boolean = preferences.getBoolean("selected_start", false)
    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M){
        val anim = findViewById<ConstraintLayout>(R.id.root).background as AnimationDrawable
        anim.setEnterFadeDuration(0)
        anim.setExitFadeDuration(500)
        anim.start()
        Handler().postDelayed({
            if(!sel){
                val intent = Intent(this@SplashActivity, SelectionActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                if (preferences.getBoolean("min", false)){
                    val intent = Intent(this@SplashActivity, MainMinActivity::class.java)
                }else{
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }, 2000)
    }else{
        val intent = Intent(this@SplashActivity, MainMinActivity::class.java)
        startActivity(intent)
        finish()
        }
    }
}