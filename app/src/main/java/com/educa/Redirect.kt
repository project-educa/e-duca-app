package com.educa

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Redirect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redirect)

        val backToSignUp = findViewById<Button>(R.id.btn_back_sign)
        val redirectToBrowser = findViewById<Button>(R.id.btn_access_navigation)

        backToSignUp.setOnClickListener {
            val signUpPage = Intent(applicationContext, SignUp::class.java)
            startActivity(signUpPage)
        }

        redirectToBrowser.setOnClickListener {
            val educaBrowser = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://educa.hopto.org/cadastro/professor/etapa1")
            )
            startActivity(educaBrowser)
        }
    }
}