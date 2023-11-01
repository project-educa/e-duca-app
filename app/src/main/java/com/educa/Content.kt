package com.educa

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educa.api.model.ContentResponseArray
import com.educa.api.model.ContentResponse
import com.educa.api.service.ApiClient
import com.educa.api.service.SessionManager
import com.educa.ui.adapters.ContentListAdapter
import com.educa.ui.recyclerview.RecyclerViewInterface
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Content : AppCompatActivity(), RecyclerViewInterface {
    lateinit var contents: RecyclerView
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    lateinit var contentAdapter: ContentListAdapter
    lateinit var contentList: MutableList<ContentResponse>
    private lateinit var searchView: SearchView
    private lateinit var filteredContentList: MutableList<ContentResponse>
    private lateinit var noContentText: RelativeLayout
    private lateinit var loadingView: View
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var loadingJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        contents = findViewById<RecyclerView>(R.id.rv_cards)!!
        noContentText = findViewById(R.id.noContentText)
        loadingView = findViewById(R.id.loadingView)

        filteredContentList = mutableListOf() // Inicialize a lista aqui
        contentList = mutableListOf()

        contentAdapter = ContentListAdapter(this@Content, filteredContentList, this@Content)

        loadContentList()
        Log.i(
            "CONTEÚDO: CHAMOU A FUNÇÃO",
            "loadContentList"
        )

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val normalizedQuery = normalizedText(newText)
                performSearch(normalizedQuery)
                return true
            }
        })
    }

    fun loadContentList() {
        loadingView.visibility = View.VISIBLE

        loadingJob = coroutineScope.launch {
            delay(100)


//            filteredContentList = mutableListOf() // Inicialize a lista aqui

            val layoutManager = LinearLayoutManager(this@Content)

            contents.layoutManager = layoutManager

            contents.adapter = contentAdapter

            Log.e(
                "CONTEÚDO: ENTROU NA FUNÇÃO LoadContentList e puxou o token",
                "FETCHED TOKEN: ${sessionManager.fetchAuthToken()}"
            )

            apiClient.getMainApiService(this@Content)
                .getAllContent()
                .enqueue(object : Callback<ContentResponseArray> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<ContentResponseArray>,
                        response: Response<ContentResponseArray>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.content?.let {
                                contentList.addAll(it)
                                filteredContentList.addAll(it)
                                contentAdapter.notifyDataSetChanged()

                                Log.i(
                                    "CONTEÚDO: ENTROU NO ISSUCCESSFUL",
                                    "Call: ${call} Response: ${response.body()}"
                                )
                            }
                        } else {
                            Log.i(
                                "CONTEÚDO: ENTROU NO IF DO ISSUCCESSFUL MAS CAIU NO ELSE",
                                "Call: ${call}, Response: ${response.code()} ${response.body()})"
                            )
                        }

                        loadingView.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<ContentResponseArray>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(
                            this@Content, t.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.e(
                            "ERRO AO PUXAR CONTEUDO",
                            "Call: ${call} ${t.message} ${t.printStackTrace()}"
                        )

                        loadingView.visibility = View.GONE

                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingJob?.cancel()
    }

    override fun onItemClick(position: Int) {
        val urlVideo = contentList[position].urlVideo
        val accessContent =
            if (urlVideo == null || urlVideo == "https://www.youtube.com/embed/") {
                Intent(this.applicationContext, Reading::class.java)
            } else {
                Intent(this.applicationContext, Video::class.java)
            }

        accessContent.putExtra("contentId", contentList[position].idConteudo.toString())
        accessContent.putExtra("title", contentList[position].titulo)
        accessContent.putExtra("text", contentList[position].texto)
        accessContent.putExtra("video", contentList[position].urlVideo)
        accessContent.putExtra("ability", contentList[position].habilidade.codigo)
        accessContent.putExtra("postedAt", contentList[position].dataCriacao)
        accessContent.putExtra("postedBy", contentList[position].usuario.nome)
        startActivity(accessContent)
    }

    private fun normalizedText(text: String): String {
        return text.replace(Regex("[^\\p{L}\\p{N}\\s\\p{Pd}]"), "")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun performSearch(query: String) {
        filteredContentList.clear()

        if (query.isEmpty()) {
            filteredContentList.addAll(contentList)
        } else {
            for (content in contentList) {
                val normalizedTitle = normalizedText(content.titulo)
                if (normalizedTitle.contains(query, ignoreCase = true)) {
                    filteredContentList.add(content)
                }
            }
        }

        contentAdapter.notifyDataSetChanged()

        if (query.isNotEmpty() && filteredContentList.isEmpty()) {
            noContentText.visibility = View.VISIBLE
        } else {
            noContentText.visibility = View.GONE
        }
    }


}
