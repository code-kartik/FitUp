package com.example.fitnessapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val userEmail:TextInputLayout = findViewById(R.id.emailInput)
        val userPassword:TextInputLayout = findViewById(R.id.passwdInput)
        val login: Button = findViewById(R.id.loginBtn)

        login.setOnClickListener {
            if(userEmail.toString() == "admin" && userPassword.toString() == "admin"){
                val intent = Intent(this@MainActivity,DashboardActivity::class.java)
                startActivity(intent)
            }
            else{ Toast.makeText(this@MainActivity, "Incorrect Credentials!", Toast.LENGTH_SHORT).show() }
        }
    }
}