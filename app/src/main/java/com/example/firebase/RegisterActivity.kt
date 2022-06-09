package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    val TAG="RegisterActivity"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


// Initialize Firebase Auth
        auth = Firebase.auth

//        //initialize other way
//        auth = FirebaseAuth.getInstance()


        register1.setOnClickListener {
            if(validation())
                register()
        }
        gotologin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    //validation of email and password editfields
    fun validation() : Boolean{
        if (email.text.toString().trim(){it <= ' '}.isEmpty()){
            Toast.makeText(this, "Email Field is Empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.text.toString().trim(){it <= ' '}.isEmpty()){
            Toast.makeText(this, "Password Field is Empty", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    fun register()
    {
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()


                    auth.signOut()

                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    finish()


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
}