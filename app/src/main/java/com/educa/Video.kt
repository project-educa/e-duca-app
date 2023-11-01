package com.educa

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView

class Video : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_aula)

        val videoTitle: String? = intent.getStringExtra("title")
        val contentId: String? = intent.getStringExtra("contentId")
        if (contentId != null) {
            Log.e("CONTENT ID NO VIDEO", contentId)
        }
        val teacherName: String? = intent.getStringExtra("postedBy")
        val urlVideo: String? = intent.getStringExtra("video")
        val abilityCode: String? = intent.getStringExtra("ability")

        val title = findViewById<TextView>(R.id.txt_title_class)
        title.text = videoTitle

        val name = findViewById<TextView>(R.id.txt_teacher_name)
        name.text = teacherName

        val ability = findViewById<TextView>(R.id.txt_category)
        ability.text = abilityCode

        val webView: WebView = findViewById(R.id.web_view)
        webView.settings.javaScriptEnabled = true

        if (urlVideo != null) {
            webView.loadUrl(urlVideo)
        }

        val backToContent = findViewById<Button>(R.id.btn_back)

        backToContent.setOnClickListener {
            val contentPage = Intent(applicationContext, Content::class.java)
            startActivity(contentPage)
        }
    }
}