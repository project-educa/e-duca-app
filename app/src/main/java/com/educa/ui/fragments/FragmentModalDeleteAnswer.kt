package com.educa.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.educa.AccessThread
import com.educa.MyQuestions
import com.educa.R
import com.educa.api.model.Answer
import com.educa.api.model.Topic
import com.educa.api.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentModalDeleteAnswer(val deletedAnswer: Int) : DialogFragment() {
    lateinit var apiClient: ApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modal_delete_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        super.onViewCreated(view, savedInstanceState)

        val btnClosePopUp: Button = view.findViewById(R.id.btn_back)
        val btnDeleteAnswer: Button = view.findViewById(R.id.btn_deleteAnswer)

        btnDeleteAnswer.setOnClickListener {
            deleteCurrentAnswer(deletedAnswer)
        }

        btnClosePopUp.setOnClickListener {
            dismiss()

        }
    }

    fun deleteCurrentAnswer(deletedAnswer: Int) {
        apiClient.getMainApiService(
            requireActivity().applicationContext
        ).deleteAnswer(deletedAnswer)
            .enqueue(object : Callback<Answer> {
                override fun onResponse(
                    call: Call<Answer>,
                    response: Response<Answer>
                ) {

                    if (response.code() == 204) {
                        val deletedAnswerResponse = response.body()
                        Log.w("ANSWER DELETED", "${deletedAnswerResponse}")

                        val accessThread = activity as AccessThread
                        accessThread.deleteAnswer(deletedAnswer)
                        dismiss()
                    } else {

                        Log.e(
                            "ERRO AO DELETAR ANSWER",
                            "Call: ${call} Response: ${response} ANSWER: ${deletedAnswer}"
                        )
                    }
                }

                override fun onFailure(call: Call<Answer>, t: Throwable) {
                    t.printStackTrace()
                    val accessThread = activity as AccessThread
                    accessThread.deleteAnswer(deletedAnswer)
                    dismiss()
                    Log.e(
                        "ERROR NO SERVIDOR AO DELETAR ANSWER",
                        "Call: ${call}"
                    )
                }
            })

    }
}