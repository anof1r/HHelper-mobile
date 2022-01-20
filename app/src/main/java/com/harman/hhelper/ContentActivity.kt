package com.harman.hhelper

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContentActivity : AppCompatActivity() {

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }
    private var clicked = false
    //private var mcResponse: MainContentResponse = MainContentResponse()

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_layout)

        val tvTitleCL: TextView = findViewById(R.id.tvTitleCL)
        val tvContentCL: TextView = findViewById(R.id.tvContentCL)
        val tvHomeWork: TextView = findViewById(R.id.hwTView)
        val imgCL: ImageView = findViewById(R.id.imgCL)

        tvTitleCL.text = intent.getStringExtra("title")
        tvContentCL.text = intent.getStringExtra("content")
        tvHomeWork.text = intent.getStringExtra("hw")
        imgCL.setImageResource(intent.getIntExtra("image", R.drawable.harman2))

        val fab = findViewById<FloatingActionButton>(R.id.fab_CL)
        val fabDelete = findViewById<FloatingActionButton>(R.id.fab_delete)
        val fabEdit = findViewById<FloatingActionButton>(R.id.fab_edit)
        setVisibility(true)

        //lateinit var arrayToDelete : ArrayList<MainContent>

        fab.setOnClickListener() {
                onSettingsButtonClicked()
            println("main CLICKED")
        }
        fabDelete.setOnClickListener {
            println("DELETE CLICKED")
            val dialog = AlertDialog.Builder(this)
                .setTitle("Delete lecture")
                .setMessage("Are you sure you want to delete this lecture?")
                .setPositiveButton("Delete", null)
                .setNegativeButton("Cancel",null)
                .show()
            val pBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            pBtn.setOnClickListener{
                //TODO
                dialog.dismiss()
            }
            val nBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            nBtn.setOnClickListener{
                dialog.dismiss()
            }
            GlobalScope.launch {
                //arrayToDelete = mcResponse.getMainContent()
                //arrayToDelete.removeLast()
            }
        }
        fabEdit.setOnClickListener {
            println("Edit CLICKED")
        }
    }
    private fun onSettingsButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        val fab = findViewById<FloatingActionButton>(R.id.fab_CL)
        val fabEdit = findViewById<FloatingActionButton>(R.id.fab_edit)
        val fabDelete = findViewById<FloatingActionButton>(R.id.fab_delete)

        if (!clicked) {
            fab.startAnimation(rotateOpen)
            fabDelete.startAnimation(fromBottom)
            fabEdit.startAnimation(fromBottom)
        } else {
            fab.startAnimation(rotateClose)
            fabDelete.startAnimation(toBottom)
            fabEdit.startAnimation(toBottom)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        val fabEdit = findViewById<FloatingActionButton>(R.id.fab_edit)
        val fabDelete = findViewById<FloatingActionButton>(R.id.fab_delete)
        if (!clicked) {
            fabDelete.visibility = View.VISIBLE
            fabEdit.visibility = View.VISIBLE
        } else {
            fabDelete.visibility = View.GONE
            fabEdit.visibility = View.GONE
        }
    }
    private fun setClickable(clicked: Boolean){
        val fabEdit = findViewById<FloatingActionButton>(R.id.fab_edit)
        val fabDelete = findViewById<FloatingActionButton>(R.id.fab_delete)
        if (!clicked){
            fabDelete.isClickable = true
            fabEdit.isClickable = true
        } else {
            fabDelete.isClickable = false
            fabEdit.isClickable = false
        }
    }
}