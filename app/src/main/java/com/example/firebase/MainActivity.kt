package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var  signingButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signingButton=findViewById(R.id.signIn)

        signingButton.setOnClickListener{
signInWithGoogle()
        }
    }

    fun signInWithGoogle()
    {

    }
}