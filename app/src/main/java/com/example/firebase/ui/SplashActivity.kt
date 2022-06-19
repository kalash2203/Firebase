package com.example.firebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.firebase.R
import com.example.firebase.firestore.FireStoreClass

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(

            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val id = FireStoreClass().getCurrentUserId()


        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.

        Handler().postDelayed({
            //if user id is not then go to main activity
            if (id != "") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else
            //if user id empty it means user is not existing and needs to register therefore go to register Activity.
            {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000) // 3000 is the delayed time in milliseconds.
    }

}