package com.harman.hhelper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harman.hhelper.api.LectureJson
import com.harman.hhelper.api.LectureJsonItem
import com.harman.hhelper.ui.ContentActivity


class RcViewAdapter(listArray:LectureJson, context: Context): RecyclerView.Adapter<RcViewAdapter.ViewHolder>(){

    private var listArrayRc = listArray
    private var contextR = context


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val tvTitle: TextView = itemView.findViewById(R.id.content_title)
        private val tvContent: TextView = itemView.findViewById(R.id.content_info)
        private val img: ImageView = itemView.findViewById(R.id.content_img)
        private val date: TextView = itemView.findViewById(R.id.tvDate)

        private fun getImageId(context: Context, imageName: String): Int {
            return context.resources.getIdentifier("drawable/$imageName", null, context.packageName)
        }
        @SuppressLint("SetTextI18n")
        fun bind(listItem: LectureJsonItem, context: Context){
            val resId = getImageId(context,listItem.imageId)
            tvTitle.text = listItem.title
            if (listItem.content.length >= 60) {
                tvContent.text = listItem.content.substring(0,60) + "..."
            } else {
                tvContent.text = listItem.content
            }
            date.text = listItem.date
            img.setImageResource(resId)
            itemView.setOnClickListener {
                val intent = Intent(context, ContentActivity::class.java).apply {
                    putExtra("title",tvTitle.text.toString())
                    putExtra("content",listItem.content)
                    putExtra("hw",listItem.homeWork)
                    putExtra("image",resId)
                    putExtra("date",listItem.date)
                    putExtra("id",listItem.id)
                }
                context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(contextR)
        return ViewHolder(inflater.inflate(R.layout.item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = listArrayRc[position]
        holder.bind(listItem,contextR)
    }

    override fun getItemCount(): Int {
        return listArrayRc.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listArray: LectureJson) {
            listArrayRc.clear()
            listArrayRc.addAll(listArray)
            notifyDataSetChanged()
    }
}