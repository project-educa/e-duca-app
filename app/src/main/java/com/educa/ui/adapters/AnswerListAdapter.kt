package com.educa.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.educa.R
import org.json.JSONObject
import com.educa.api.model.AnswerResponse
import com.educa.api.service.SessionManager
import com.educa.ui.fragments.FragmentModalDeleteAnswer
import com.educa.ui.fragments.FragmentModalUpdateAnswer
import com.educa.ui.recyclerview.RecyclerViewInterface
import java.text.SimpleDateFormat
import java.util.*

class AnswerListAdapter(
    private val context: Context,
    private val answers: List<AnswerResponse>?,
    private val recyclerViewInterface: RecyclerViewInterface
) : RecyclerView.Adapter<AnswerListAdapter.ViewHolder>() {

     private lateinit var sessionManager: SessionManager
    class ViewHolder(view: View, recyclerViewInterface: RecyclerViewInterface) :
        RecyclerView.ViewHolder(view) {

        fun bind(
            answer: AnswerResponse,
            recyclerViewInterface: RecyclerViewInterface,
            userId: String
        ) {
            val title = itemView.findViewById<TextView>(R.id.answerBody)
            title?.text = answer?.resposta

            val dateString = answer.dataCriacao

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate = dateFormat.parse(dateString)

            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsedDate)

            val postInString = itemView.context.getString(R.string.txt_postIn)
            val finalDateString = "$postInString $formattedDate por "

            val posted = itemView.findViewById<TextView>(R.id.txt_postedAt)
            posted?.text = finalDateString

            val icons = itemView.findViewById<RelativeLayout>(R.id.icons)
            val editContent = itemView.findViewById<ImageButton>(R.id.edit_content)
            val deleteContent = itemView.findViewById<ImageButton>(R.id.delete_content)

            if(userId.contains(answer.usuario?.idUsuario.toString())) {
                Log.e("USER IF DE VALIDACAO DO USER", userId)
                icons.visibility = View.VISIBLE
            }

            editContent.setOnClickListener {
                val showPopUp = FragmentModalUpdateAnswer(answer.idResposta)
                showPopUp.show((itemView.context as AppCompatActivity).supportFragmentManager, "showPopUp")
            }
            deleteContent.setOnClickListener {
                val showPopUp = FragmentModalDeleteAnswer(answer.idResposta)
                showPopUp.show((itemView.context as AppCompatActivity).supportFragmentManager, "showPopUp")
            }

            itemView.setOnClickListener(View.OnClickListener {
                if (true) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(position)
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_answer, parent, false)
        return AnswerListAdapter.ViewHolder(view, recyclerViewInterface)
    }

    override fun onBindViewHolder(holder: AnswerListAdapter.ViewHolder, position: Int) {
        val answer = answers?.get(position)
        sessionManager = SessionManager(context)
        val mDecode = sessionManager.decodeToken(sessionManager.fetchAuthToken()!!)
        val userId = JSONObject(mDecode).getString("sub")
        holder.bind(answer!!, recyclerViewInterface, userId)
    }

    override fun getItemCount(): Int = answers!!.size
}