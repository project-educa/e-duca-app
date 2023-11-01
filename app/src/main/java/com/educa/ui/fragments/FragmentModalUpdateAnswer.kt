package com.educa.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.educa.AccessThread
import com.educa.R
import com.educa.api.model.Answer
import com.educa.api.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragmentModalUpdateAnswer(val idResposta: Int) : DialogFragment() {
    lateinit var apiClient: ApiClient
    private lateinit var answerField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modal_update_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiClient = ApiClient()

        val btnClosePopUp: Button = view.findViewById(R.id.btn_back)
        val btn_updateTopic = view.findViewById<Button>(R.id.btn_updateAnswer)

        val topicId = idResposta

        btn_updateTopic.setOnClickListener {
            val userAnswer = answerField.text.toString()

            if (userAnswer.isNotBlank()) {
                val updatedAnswer = Answer(
                    idTopico = topicId,
                    resposta = userAnswer
                )
                updateCurrentAnswer(updatedAnswer)
            } else {
                Log.e("ERRO", "Os campos não podem estar vazios")
            }
        }

        btnClosePopUp.setOnClickListener {
            dismiss()
        }

        answerField = view.findViewById(R.id.ipt_answerBody)
    }

    fun updateCurrentAnswer(updatedAnswer: Answer) {
        apiClient.getMainApiService(
            requireActivity().applicationContext
        ).updateAnswer(updatedAnswer.idTopico, updatedAnswer)
            .enqueue(object : Callback<Answer> {
                override fun onResponse(
                    call: Call<Answer>,
                    response: Response<Answer>
                ) {
                    if (response.isSuccessful) {
                        val updatedAnswerResponse = response.body()
                        Log.w("ANSWER ATUALIZADA", "${updatedAnswerResponse}")

                        val answer = activity as AccessThread

                        if (Objects.nonNull(updatedAnswer)) {
                            answer.updateListTAnswer(updatedAnswer, updatedAnswer.idTopico)
                        }
                        dismiss()
                    } else {
                        Log.e(
                            "ERRO AO ATULIZAR ANSWER",
                            "Call: ${call} Response: ${response} ANSWER no else: ${updatedAnswer}"
                        )
                    }
                }

                override fun onFailure(call: Call<Answer>, t: Throwable) {
                    t.printStackTrace()
                    Log.e(
                        "ERRO NO SERVIDOR AO ATULIZAR ANSWER",
                        "Call: ${call}"
                    )
                }
            })
    }
}
