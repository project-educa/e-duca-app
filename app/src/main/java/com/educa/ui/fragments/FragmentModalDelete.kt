package com.educa.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.educa.MyQuestions
import com.educa.R
import com.educa.api.model.Topic
import com.educa.api.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentModalDelete(val currentTopic: Int) : DialogFragment() {

    lateinit var apiClient: ApiClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modal_delete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        super.onViewCreated(view, savedInstanceState)

        val btnClosePopUp: Button = view.findViewById(R.id.btn_back)
        val btn_deleteTopic: Button = view.findViewById(R.id.btn_deleteTopic)

        btn_deleteTopic.setOnClickListener {
            deleteCurrentTopic(currentTopic)
        }

        btnClosePopUp.setOnClickListener {
            dismiss()
        }

    }

    fun deleteCurrentTopic(currentTopic: Int) {
        apiClient.getMainApiService(
            requireActivity().applicationContext
        ).deleteTopic(currentTopic)
            .enqueue(object : Callback<Topic> {
                override fun onResponse(
                    call: Call<Topic>,
                    response: Response<Topic>
                ) {
                    if (response.code() == 204) {
                        val deletedTopic = response.body()
                        Log.w("TÓPICO DELETADO", "${deletedTopic}")

                        val myQuestions = activity as MyQuestions
                        myQuestions.deleteTopic(currentTopic)
                        dismiss()
                    } else {
                        Log.e(
                            "ERRO AO DELETAR TÓPICO",
                            "Call: ${call} Response: ${response} Tópico DELETADO: ${currentTopic}"
                        )
                    }
                }

                override fun onFailure(call: Call<Topic>, t: Throwable) {
                    t.printStackTrace()
                    val myQuestions = activity as MyQuestions
                    myQuestions.deleteTopic(currentTopic)
                    dismiss()
                    Log.e(
                        "ERROR NO SERVIDOR AO DELETAR TÓPICO",
                        "Call: ${call}"
                    )
                }

            })
    }

}