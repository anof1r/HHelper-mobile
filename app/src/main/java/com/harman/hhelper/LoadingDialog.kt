package com.harman.hhelper

import android.app.Activity
import android.app.AlertDialog

class LoadingDialog(private val myActivity: Activity) {
    private lateinit var dialog : AlertDialog

    fun startLoadingDialog(){
        val builder = AlertDialog.Builder(myActivity)
        val inflater = myActivity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog,null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }
    fun dismissDialog(){
        dialog.dismiss()
    }
}