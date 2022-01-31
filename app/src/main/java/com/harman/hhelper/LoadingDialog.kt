package com.harman.hhelper


import android.app.Activity
import android.app.AlertDialog

class LoadingDialog(val mActivity:Activity) {
    private lateinit var isdialog:AlertDialog
    fun startLoading(){
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loading_dialog,null)
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()
    }
    fun isDismiss(){
        isdialog.dismiss()
    }
}