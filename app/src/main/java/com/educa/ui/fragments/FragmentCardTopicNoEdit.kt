package com.educa.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.educa.R
import com.educa.api.model.TopicResponseArray

class FragmentCardTopicNoEdit : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val topicList: TopicResponseArray? = activity?.intent?.getParcelableExtra("topicList")
        val position: String? = activity?.intent?.getStringExtra("position")
        val topic = topicList?.topic?.get(position!!.toInt())
        val view: View = inflater.inflate(R.layout.fragment_card_topic_no_edit, container, false)

        Log.e("TOPIC LIST NO FRAGMENT", topicList.toString())
        val title = view.findViewById<TextView>(R.id.title)
        title?.text = topic?.titulo

        val name = view.findViewById<TextView>(R.id.txt_nameStudent)
        name?.text = topic?.usuario?.nome

        val posted = view.findViewById<TextView>(R.id.txt_postedAt)
        posted?.text = topic?.dataCriacao

        val answers = view.findViewById<TextView>(R.id.answers)
        answers?.text = "${topic?.respostas?.size} respostas"

        val description = view.findViewById<TextView>(R.id.description)
        description?.text = topic?.descricao

        return view
    }


}