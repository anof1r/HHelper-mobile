package com.harman.hhelper.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.harman.hhelper.R

class LoginActivity : AppCompatActivity() {

    lateinit var etLoginEmail : TextInputEditText
    lateinit var etLoginPassword : TextInputEditText
    lateinit var tvRegisterHere : TextView
    lateinit var btnLogin : Button
    lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_activity)


        etLoginEmail = findViewById(R.id.etLoginEmail)
        etLoginPassword = findViewById(R.id.etLoginPass)
        tvRegisterHere = findViewById(R.id.tvRegisterHere)
        btnLogin = findViewById(R.id.btnLogin)

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            loginUser()
        }
        tvRegisterHere.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
    private fun loginUser(){
        val email : String = etLoginEmail.text.toString()
        val password :  String = etLoginPassword.text.toString()

        if (TextUtils.isEmpty(email)){
            etLoginEmail.error = "Email cannot be empty"
            etLoginEmail.requestFocus()
        } else if (TextUtils.isEmpty(password)){
            etLoginPassword.error = "Password cannot be empty"
            etLoginPassword.requestFocus()
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"Welcome !", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }else{
                    Toast.makeText(this,task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}