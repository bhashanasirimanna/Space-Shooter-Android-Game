package com.example.spaceshooter2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var isMute = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.play).setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    GameActivity::class.java
                )
            )
        }
        val highScoreTxt = findViewById<TextView>(R.id.highScoreTxt)
        val prefs = getSharedPreferences("game", MODE_PRIVATE)
        highScoreTxt.text = "HighScore : " + prefs.getInt("highscore", 0)
        isMute = prefs.getBoolean("isMute", false)
        val volumeCtrl = findViewById<ImageView>(R.id.volumeCtrl)
        if (isMute) volumeCtrl.setImageResource(R.drawable.baseline_volume_off_24) else volumeCtrl.setImageResource(
            R.drawable.baseline_volume_up_24
        )
        volumeCtrl.setOnClickListener {
            isMute = !isMute
            if (isMute) volumeCtrl.setImageResource(R.drawable.baseline_volume_off_24) else volumeCtrl.setImageResource(
                R.drawable.baseline_volume_up_24
            )
            val editor = prefs.edit()
            editor.putBoolean("isMute", isMute)
            editor.apply()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}