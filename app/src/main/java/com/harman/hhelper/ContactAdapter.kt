package com.harman.hhelper

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harman.hhelper.contatcts.ContactsJson
import com.harman.hhelper.contatcts.ContactsJsonItem

class ContactAdapter(listArray: ContactsJson, context: Context): RecyclerView.Adapter<ContactAdapter.RcViewAdapter.ViewHolder>(){

    private var listArrayRc = listArray
    private var contextR = context

    class RcViewAdapter{

        class ViewHolder(view: View): RecyclerView.ViewHolder(view){

            private val contactName: TextView = itemView.findViewById(R.id.contactName)
            private val tvContactNumber: TextView = itemView.findViewById(R.id.contactNumber)
            private val img: ImageView = itemView.findViewById(R.id.contactImg)
            private val cPosition: TextView = itemView.findViewById(R.id.contactPosition)
            private val cEmail: TextView = itemView.findViewById(R.id.contactEmail)
            private val cTelegram: TextView = itemView.findViewById(R.id.contactTelegram)

            private fun getImageId(context: Context, imageName: String): Int {
                return context.resources.getIdentifier("drawable/$imageName", null, context.packageName)
            }
            @SuppressLint("SetTextI18n")
            fun bind(listItem: ContactsJsonItem, context: Context){
                val resId = getImageId(context,listItem.contactImage)
                contactName.text = listItem.contactName
                tvContactNumber.text = listItem.contactNumber
                cPosition.text = listItem.contactPosition
                cEmail.text = listItem.contactEmail
                cTelegram.text = listItem.contactTelegram
                img.setImageResource(resId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RcViewAdapter.ViewHolder {
        val inflater = LayoutInflater.from(contextR)
        return RcViewAdapter.ViewHolder(inflater.inflate(R.layout.contact_item, parent, false))
    }

    override fun onBindViewHolder(holder: RcViewAdapter.ViewHolder, position: Int) {
        val listItem = listArrayRc[position]
        holder.bind(listItem,contextR)
    }

    override fun getItemCount(): Int {
        return listArrayRc.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listArray: ContactsJson) {
        listArrayRc.clear()
        listArrayRc.addAll(listArray)
        notifyDataSetChanged()
    }
}