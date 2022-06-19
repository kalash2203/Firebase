package com.example.firebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.firebase.R
import com.example.firebase.firestore.FireStoreClass
import com.example.firebase.model.User
import com.example.firebase.utils.Constants.LOGIN_USER_DETAILS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

// ...
// Initialize Firebase Auth
        auth = Firebase.auth

        login.setOnClickListener {
            if (validation()) {
                login()
            }
        }
       forgot_password.setOnClickListener {
           val intent = Intent(this, ForgotPasswordActivity::class.java)
           startActivity(intent)
       }
    }

    //validation of email and password editfields
    fun validation() : Boolean{
        if (login_email.text.toString().trim(){it <= ' '}.isEmpty()){
            Toast.makeText(this, "Email Field is Empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (login_password.text.toString().trim(){it <= ' '}.isEmpty()){
            Toast.makeText(this, "Password Field is Empty", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun login() {
        auth.signInWithEmailAndPassword(login_email.text.toString().trim(), login_password.text.toString().trim())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    //after success login ,getUserDetails will be called and
                    FireStoreClass().getUserDetails(this)



                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun loggedInSuccess(user: User)
    {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(LOGIN_USER_DETAILS,user)
        startActivity(intent)
        finish()

    }
}