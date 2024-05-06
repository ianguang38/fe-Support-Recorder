package com.example.myapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecordAdapter(private val noteMap: ArrayList<Records>,private val activity: MainActivity) :
    RecyclerView.Adapter<RecordAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val edit:Button= itemView.findViewById(R.id.editButton)
        val deleteButton:Button=itemView.findViewById(R.id.deleteButton)
//        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.edit_record, parent, false)
        return NoteViewHolder(view)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val p1=position+1
        holder.titleTextView.text= "No.$p1"

        holder.edit.setOnClickListener{
            activity.goToRecord(noteMap[position])

        }

        holder.deleteButton.setOnClickListener{
            activity.comfirm(position,::test)
        }
        holder.titleTextView


    }

    private fun test(position:Int):Int{

        noteMap.removeAt(position)
        notifyItemRemoved(position)
        return 0
    }



    override fun getItemCount(): Int {
        return noteMap.size
    }
}
