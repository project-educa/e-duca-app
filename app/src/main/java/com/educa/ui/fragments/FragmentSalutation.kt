package com.educa.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.educa.R
import com.educa.api.model.User
import com.educa.api.service.ApiClient
import com.educa.api.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentSalutation : Fragment() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())

        getCurrentUser()
        val view: View = inflater.inflate(R.layout.fragment_salutation, container, false)

        return view
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
                    val name = view?.findViewById<TextView>(R.id.name_student)
                    name?.text =  "Olá, ${response.body()?.nome} !"
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