package com.educa.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.educa.R
import com.educa.api.model.TopicResponseArray

class FragmentCardTopic : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val topicList: TopicResponseArray? = activity?.intent?.getParcelableExtra("topicList")
        val position: String? = activity?.intent?.getStringExtra("position")
        val topic = topicList?.topic?.get(position!!.toInt())

        val view: View = inflater.inflate(R.layout.fragment_card_topic, container, false)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}