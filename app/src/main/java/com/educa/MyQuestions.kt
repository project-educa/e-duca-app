package com.educa

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educa.api.model.*
import com.educa.api.service.ApiClient
import com.educa.api.service.SessionManager
import com.educa.ui.adapters.TopicListAdapter
import com.educa.ui.recyclerview.RecyclerViewInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyQuestions : AppCompatActivity(), RecyclerViewInterface {
    lateinit var myTopics: RecyclerView
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    lateinit var topicAdapter: TopicListAdapter
    lateinit var myTopicsList: MutableList<TopicResponse>
    lateinit var emptyView: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_questions)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        myTopics = findViewById<RecyclerView>(R.id.rv_topics)!!

        loadMyTopicsList()
        Log.i(
            "TÓPICO: CHAMOU A FUNÇÃO",
            "loadMyTopicsList"
        )

        val seeAll = findViewById<FragmentContainerView>(R.id.fg_btn_seeAlltopics)
        seeAll.setOnClickListener{
            val allTopicsPage = Intent(applicationContext, AllQuestions::class.java)
            allTopicsPage.putExtra("btn_text", "Ver meus tópicos")
            startActivity(allTopicsPage)
        }
    }

    fun updateListTopic(topic: Topic?, id: Int) {
        myTopicsList.map{
            if (it.idTopico == id) {
                it.titulo = topic!!.titulo
                it.descricao = topic.descricao
            }
        }

        updateLayout()
    }

    fun deleteTopic(id: Int) {
        myTopicsList = myTopicsList.filter { it.idTopico != id } as MutableList<TopicResponse>
        updateLayout()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateLayout() {
        topicAdapter = TopicListAdapter(this, myTopicsList, this)

        val layoutManager = LinearLayoutManager(this)

        myTopics.layoutManager = layoutManager

        myTopics.adapter = topicAdapter

        topicAdapter.notifyDataSetChanged()

        updateEmptyViewVisibility()

    }

    fun loadMyTopicsList() {
        myTopicsList = mutableListOf()
        topicAdapter = TopicListAdapter(this, myTopicsList, this)

        val layoutManager = LinearLayoutManager(this)

        myTopics.layoutManager = layoutManager

        myTopics.adapter = topicAdapter

        apiClient.getMainApiService(this)
            .getMyTopics()
            .enqueue(object : Callback<TopicResponseArray> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TopicResponseArray>,
                    response: Response<TopicResponseArray>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.topic?.let { myTopicsList.addAll(it) }
                        topicAdapter.notifyDataSetChanged()

                        Log.i(
                            "TÓPICO: ENTROU NO ISSUCCESSFUL",
                            "Call: ${call} Response: ${response.body()} ${response} ${myTopicsList}"
                        )

                    } else {
                        Log.i(
                            "TÓPICO: ENTROU NO IF DO ISSUCCESSFUL MAS CAIU NO ELSE",
                            "Call: ${call}, Response: ${response.code()} ${response.body()})"
                        )
                    }
                    updateEmptyViewVisibility()

                }

                override fun onFailure(call: Call<TopicResponseArray>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(
                        this@MyQuestions, t.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.e(
                        "ERRO AO PUXAR TÓPICO",
                        "Call: ${call}  ${t.message} ${t.printStackTrace()}"
                    )
                    updateEmptyViewVisibility()

                }
            })
    }

    override fun onItemClick(position: Int) {
        val accessTopic = Intent(this.applicationContext, AccessThread::class.java)
        accessTopic.putExtra("topicList", TopicResponseArray(myTopicsList))
        accessTopic.putExtra("position", position.toString())
        accessTopic.putExtra("page", "myQuestions")

        startActivity(accessTopic)
    }

    private fun updateEmptyViewVisibility() {
        emptyView = findViewById(R.id.emptyView)

        if (myTopicsList.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }
    }

}