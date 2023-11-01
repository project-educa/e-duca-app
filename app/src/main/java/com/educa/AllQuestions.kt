package com.educa

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educa.api.model.TopicResponse
import com.educa.api.model.TopicResponseArray
import com.educa.api.service.ApiClient
import com.educa.api.service.SessionManager
import com.educa.ui.adapters.TopicListAdapter
import com.educa.ui.recyclerview.RecyclerViewInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllQuestions : AppCompatActivity(), RecyclerViewInterface {
    lateinit var allTopics: RecyclerView
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    lateinit var topicAdapter: TopicListAdapter
    lateinit var allTopicsList: MutableList<TopicResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_questions)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        allTopics = findViewById<RecyclerView>(R.id.rv_allTopics)!!

        val btnMyTopics = findViewById<FragmentContainerView>(R.id.fg_btn_seeMyTopics)!!

        btnMyTopics.setOnClickListener{
            val myTopicsPage = Intent(applicationContext, MyQuestions::class.java)
            myTopicsPage.putExtra("btn_text", "Ver todos os tópicos")
            startActivity(myTopicsPage)
        }

        loadAllTopicsList()
    }

    fun loadAllTopicsList() {
        allTopicsList = mutableListOf()
        topicAdapter = TopicListAdapter(this, allTopicsList, this)

        val layoutManager = LinearLayoutManager(this)

        allTopics.layoutManager = layoutManager

        allTopics.adapter = topicAdapter

        apiClient.getMainApiService(this).getAllTopics().enqueue(object :
            Callback<TopicResponseArray> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<TopicResponseArray>,
                response: Response<TopicResponseArray>
            ) {
                if (response.isSuccessful) {
                    response.body()?.topic?.let { allTopicsList.addAll(it) }
                    topicAdapter.notifyDataSetChanged()

                    Log.i(
                        "PUXOU TODOS OS TOPICOS COM SUCESSO",
                        "Call: ${call} Response: ${response.body()} ${response} ${allTopicsList}"
                    )
                }
            }

            override fun onFailure(call: Call<TopicResponseArray>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@AllQuestions, t.message,
                    Toast.LENGTH_SHORT
                ).show()

                Log.e(
                    "ERRO AO PUXAR TODOS OS TOPICOS",
                    "Call: ${call}  ${t.message} ${t.printStackTrace()}"
                )
            }
        })

    }

    override fun onItemClick(position: Int) {
        val accessTopic = Intent(this.applicationContext, AccessThread::class.java)
        accessTopic.putExtra("topicList", TopicResponseArray(allTopicsList))
        accessTopic.putExtra("position", position.toString())
        accessTopic.putExtra("page", "allQuestions")

        startActivity(accessTopic)
    }
}