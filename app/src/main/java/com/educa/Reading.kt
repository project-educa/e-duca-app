package com.educa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class Reading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)

        val contentId: String? = intent.getStringExtra("contentId")
        if (contentId != null) {
            Log.e("CONTENT ID NO READING", contentId)
        }
        val contentTitle: String? = intent.getStringExtra("title")
        val contentText: String? = intent.getStringExtra("text")

        val title = findViewById<TextView>(R.id.txt_title)
        title.text = contentTitle

        val text = findViewById<TextView>(R.id.txt_content)
        text.text = contentText

        val backToContent = findViewById<Button>(R.id.btn_back)

        backToContent.setOnClickListener {
            val contentPage = Intent(applicationContext, Content::class.java)
            startActivity(contentPage)
        }
    }
}