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
import com.harman.hhelper.main_content_jsonresponse.MainContent


class RcViewAdapter(listArray:ArrayList<MainContent>, context: Context): RecyclerView.Adapter<RcViewAdapter.ViewHolder>(){

    private var listArrayRc = listArray
    private var contextR = context

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        private val img: ImageView = itemView.findViewById(R.id.img)

        private fun getImageId(context: Context, imageName: String): Int {
            return context.resources.getIdentifier("drawable/$imageName", null, context.packageName)
        }

        fun bind(listItem: MainContent, context: Context){
            val resId = getImageId(context,listItem.imageId)
            tvTitle.text = listItem.title
            tvContent.text = listItem.content
            img.setImageResource(resId)
            itemView.setOnClickListener {
                val intent = Intent(context,ContentActivity::class.java).apply {
                    putExtra("title",tvTitle.text.toString())
                    putExtra("content",tvContent.text.toString())
                    putExtra("image",resId)
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
    fun updateAdapter(listArray: List<MainContent>) {
            listArrayRc.clear()
            listArrayRc.addAll(listArray)
            notifyDataSetChanged()
    }
}