package com.educa.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.educa.R
import com.educa.api.model.TopicResponse
import com.educa.ui.fragments.FragmentModalDelete
import com.educa.ui.fragments.FragmentModalUpdate
import com.educa.ui.recyclerview.RecyclerViewInterface
import java.text.SimpleDateFormat
import java.util.*

class TopicListAdapter(
    private val context: Context,
    private val topics: MutableList<TopicResponse>,
    private val recyclerViewInterface: RecyclerViewInterface
) : RecyclerView.Adapter<TopicListAdapter.ViewHolder>() {

    class ViewHolder(view: View, recyclerViewInterface: RecyclerViewInterface) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(topic: TopicResponse, recyclerViewInterface: RecyclerViewInterface) {

            val title = itemView.findViewById<TextView>(R.id.title)
            title.text = topic.titulo

            val name = itemView.findViewById<TextView>(R.id.txt_nameStudent)
            name.text = topic.usuario?.nome

            val posted = itemView.findViewById<TextView>(R.id.txt_postedAt)

            val answers = itemView.findViewById<TextView>(R.id.answers)
            answers.text = "${topic.respostas?.size} respostas"

            val description = itemView.findViewById<TextView>(R.id.description)
            description.text = topic.descricao

            val dateString = topic.dataCriacao

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate = dateFormat.parse(dateString)

            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsedDate)

            val postInString = itemView.context.getString(R.string.txt_postIn)
            val finalDateString = "$postInString $formattedDate por "
            posted.text = finalDateString

            title.setOnClickListener(View.OnClickListener {
                if (true) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(position)
                    }
                }
            })

            val editContent = itemView.findViewById<ImageButton>(R.id.edit_content)
            val deleteContent = itemView.findViewById<ImageButton>(R.id.delete_content)

            editContent?.setOnClickListener {
                val showPopUp = FragmentModalUpdate((topic.idTopico))
                showPopUp.show((itemView.context as AppCompatActivity).supportFragmentManager, "showPopUp")
            }
            deleteContent?.setOnClickListener {
                val showPopUp = FragmentModalDelete(topic.idTopico)
                showPopUp.show((itemView.context as AppCompatActivity).supportFragmentManager, "showPopUp")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)

        val fragment = getCurrentActivity()
        val view = inflater.inflate(fragment, parent, false)
        return ViewHolder(view, recyclerViewInterface)
    }

    fun getCurrentActivity(): Int {
        if (context.toString().contains("com.educa.MyQuestions") ) {
            return R.layout.fragment_card_topic
        } else {
            return R.layout.fragment_card_topic_no_edit
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topics[position]
        holder.bind(topic, recyclerViewInterface)
    }

    override fun getItemCount(): Int = topics.size
}
