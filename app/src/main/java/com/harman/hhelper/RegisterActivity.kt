package com.harman.hhelper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity: AppCompatActivity() {

    lateinit var etRegEmail : TextInputEditText
    lateinit var etRegPassword : TextInputEditText
    lateinit var etLoginHere : TextView
    lateinit var btnRegister : Button
    lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        etRegEmail = findViewById(R.id.etRegEmail)
        etRegPassword = findViewById(R.id.etRegPass)
        etLoginHere = findViewById(R.id.tvLoginHere)
        btnRegister = findViewById(R.id.btnRegister)

        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            createUser()
        }
        etLoginHere.setOnClickListener {
            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
        }
    }

    private fun createUser(){
       val email : String = etRegEmail.text.toString()
       val password :  String = etRegPassword.text.toString()

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty")
            etRegEmail.requestFocus()
        } else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Password cannot be empty")
            etRegPassword.requestFocus()
        } else{
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"Registration complete !", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                }else{
                    Toast.makeText(this,"Registration Error: " + task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


}




