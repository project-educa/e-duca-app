package com.educa

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.educa.ui.fragments.FragmentModalAddTopic

class ButtonAddNewTopic : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_button_add_new_topic, container, false)
    }

    @Nullable
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddTopic = view.findViewById<RelativeLayout>(R.id.btnAddTopic)

        btnAddTopic.setOnClickListener {
            val showPopUp = FragmentModalAddTopic()
            showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
        }
    }
}