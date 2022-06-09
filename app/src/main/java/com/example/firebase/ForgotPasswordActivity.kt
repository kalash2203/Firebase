package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register.*

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth= FirebaseAuth.getInstance()
        send_email.setOnClickListener {
            forgotPassword()
        }
    }

//function of sending email to registered email address for resetting the password
    fun forgotPassword(){
        auth.sendPasswordResetEmail(forgot.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Show the toast message and finish the forgot password activity to go back to the login screen.
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                       "Email sent sucessfully",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()
                }
                }
            }
    }
