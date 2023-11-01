package com.educa.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.educa.Content
import com.educa.Login
import com.educa.MyQuestions
import com.educa.R
import com.educa.api.model.User
import com.educa.api.service.ApiClient
import com.educa.api.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentMenu : Fragment() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    private lateinit var btnOpenDrawer: ImageButton
    private lateinit var btnCloseDrawer: ImageButton
    private lateinit var drawerLayout: View
    private lateinit var backgroundOverlay: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @Nullable
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homePage = view.findViewById<TextView>(R.id.tv_home_page)
        val doubtForum = view.findViewById<TextView>(R.id.tv_doubt_forum)
        val exit = view.findViewById<TextView>(R.id.tv_exit)

        homePage.setOnClickListener {
            val content = Intent(activity, Content::class.java)
            startActivity(content)
        }

        doubtForum.setOnClickListener {
            val myQuestions = Intent(activity, MyQuestions::class.java)
            myQuestions.putExtra("btn_text", "Ver todos os tópicos")
            startActivity(myQuestions)
        }

        exit.setOnClickListener{
            val login = Intent(activity, Login::class.java)
            startActivity(login)
        }

        btnOpenDrawer = view.findViewById(R.id.btnOpenDrawer)
        btnCloseDrawer = view.findViewById(R.id.btnCloseDrawer)
        drawerLayout = view.findViewById(R.id.drawerLayout)
        backgroundOverlay = view.findViewById(R.id.backgroundOverlay)

        btnOpenDrawer.setOnClickListener {
            openMenu()
        }
        btnCloseDrawer.setOnClickListener {
            closeMenu()
        }
        backgroundOverlay.setOnClickListener {
            closeMenu()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        getCurrentUser()
        val view: View =  inflater.inflate(R.layout.fragment_menu, container, false)

        return view
    }

    private fun openMenu() {
        // Mostrar a camada de fundo
        backgroundOverlay.visibility = View.VISIBLE

        if (drawerLayout.visibility == View.VISIBLE) {
            drawerLayout.visibility = View.GONE

        } else {
            drawerLayout.visibility = View.VISIBLE
        }
    }

    private fun closeMenu() {
        // Esconder a camada de fundo
        backgroundOverlay.visibility = View.GONE

        if (drawerLayout.visibility == View.VISIBLE) {
            drawerLayout.visibility = View.GONE

        } else {
            drawerLayout.visibility = View.VISIBLE
        }
    }

    fun getCurrentUser() {
        apiClient.getAuthApiService(requireContext()).getUser().enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if (response.isSuccessful) {
                    Log.i(
                        "USER: ENTROU NO ISSUCCESSFUL",
                        "Call: ${call} Response: ${response.body()?.nome}"
                    )
                    val name = view?.findViewById<TextView>(R.id.student_name)
                    name?.text = "${response.body()?.nome} ${response.body()?.sobrenome}"
                } else {
                    Log.i(
                        "USER: ENTROU NO IF DO ISSUCCESSFUL MAS CAIU NO ELSE",
                        "Call: ${call}, Response: ${response.code()} ${response.body()})"
                    )
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    context, t.message,
                    Toast.LENGTH_SHORT
                ).show()

                Log.e(
                    "ERRO AO PUXAR USER",
                    "Call: ${call} ${t.message} ${t.printStackTrace()}"
                )
            }
        })
    }
}

