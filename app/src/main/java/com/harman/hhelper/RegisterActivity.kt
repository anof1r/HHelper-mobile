package com.harman.hhelper

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity: AppCompatActivity() {

    lateinit var etRegEmail : TextInputEditText
    lateinit var etRegPassword : TextInputEditText
    lateinit var etLoginHere : TextView
    lateinit var btnRegister : Button
    lateinit var auth : FirebaseAuth
    lateinit var fStore : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        etRegEmail = findViewById(R.id.etRegEmail)
        etRegPassword = findViewById(R.id.etRegPass)
        etLoginHere = findViewById(R.id.tvLoginHere)
        btnRegister = findViewById(R.id.btnRegister)

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

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
            etRegEmail.error = "Email cannot be empty"
            etRegEmail.requestFocus()
        } else if (TextUtils.isEmpty(password)){
            etRegPassword.error = "Password cannot be empty"
            etRegPassword.requestFocus()
        } else{
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val user = auth.currentUser!!
                    val doc = fStore.collection("Users").document(user.uid)
                    val userInfo = HashMap<String,Any>()
                    userInfo["USerEmail"] = email
                    userInfo["isAdmin"] = 0
                    doc.set(userInfo)
                    Toast.makeText(this,"Registration complete!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                }else{
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


}




