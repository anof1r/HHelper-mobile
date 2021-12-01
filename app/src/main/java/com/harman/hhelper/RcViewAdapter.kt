package com.harman.hhelper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.harman.hhelper.information_response.Information
import com.harman.hhelper.main_content_jsonresponse.MainContent

class RcViewAdapter(listArray:ArrayList<MainContent>, context: Context): RecyclerView.Adapter<RcViewAdapter.ViewHolder>() {

    private var listArrayRc = listArray
    private var contextR = context

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvContent = itemView.findViewById<TextView>(R.id.tvContent)
        val img = itemView.findViewById<ImageView>(R.id.img)
        fun bind(listItem: MainContent, context: Context){
            tvTitle.text = listItem.title
            tvContent.text = listItem.content
            img.setImageResource(listItem.imageId)
            itemView.setOnClickListener {
                val intent = Intent(context,ContentActivity::class.java).apply {
                    putExtra("title",tvTitle.text.toString())
                    putExtra("content",tvContent.text.toString())
                    putExtra("image",listItem.imageId)
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
        var listItem = listArrayRc[position]
        holder.bind(listItem,contextR)
    }

    override fun getItemCount(): Int {
        return listArrayRc.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listArray: List<MainContent>){
        listArrayRc.clear()
        listArrayRc.addAll(listArray)
        notifyDataSetChanged()
    }
}