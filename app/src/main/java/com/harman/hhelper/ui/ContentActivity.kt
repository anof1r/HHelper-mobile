package com.harman.hhelper.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.harman.hhelper.api.ApiRequests
import com.harman.hhelper.R
import com.harman.hhelper.api.LectureJsonItem
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

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
    private var Id: Int = 0
    private var clicked = false

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_layout)

        val tvTitleCL: TextView = findViewById(R.id.tvTitleCL)
        val tvContentCL: TextView = findViewById(R.id.tvContentCL)
        val tvHomeWork: TextView = findViewById(R.id.hwTView)
        val imgCL: ImageView = findViewById(R.id.imgCL)
        val date: TextView = findViewById(R.id.cDate)

        tvTitleCL.text = intent.getStringExtra("title")
        tvContentCL.text = intent.getStringExtra("content")
        tvHomeWork.text = "Homework: " + intent.getStringExtra("hw")
        date.text = intent.getStringExtra("date")
        imgCL.setImageResource(intent.getIntExtra("image", R.drawable.harman))
        val id: Int = intent.getIntExtra("id", 0)
        Id = id
        val fab = findViewById<FloatingActionButton>(R.id.fab_CL)
        val fabDelete = findViewById<FloatingActionButton>(R.id.fab_delete)
        val fabEdit = findViewById<FloatingActionButton>(R.id.fab_edit)
        setVisibility(true)

        @DelicateCoroutinesApi
        suspend fun deleteCurrentData(id: Int) {
            val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)
            api.deleteLecture(id).awaitResponse()
        }

        fab.setOnClickListener {
            onSettingsButtonClicked()
            println("main CLICKED")
        }
        fabDelete.setOnClickListener {
            println("DELETE CLICKED")
            val deleteDialog = AlertDialog.Builder(this)
                .setTitle("Delete lecture")
                .setMessage("Are you sure you want to delete this lecture?")
                .setPositiveButton("Delete", null)
                .setNegativeButton("Cancel", null)
                .show()
            val pBtn = deleteDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            pBtn.setOnClickListener {
                GlobalScope.launch {
                    deleteCurrentData(id)
                }
                deleteDialog.dismiss()
                finish()
            }
            val nBtn = deleteDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            nBtn.setOnClickListener {
                deleteDialog.dismiss()
            }
        }
        fabEdit.setOnClickListener {
            editLecture()
        }
    }

    @DelicateCoroutinesApi
    suspend fun editCurrentData(item: LectureJsonItem) {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)
        api.editLecture(item.content, item.date, item.homeWork, Id, item.imageId, item.title)
            .awaitResponse()
    }

    @SuppressLint("SimpleDateFormat")
    @DelicateCoroutinesApi
    private fun editLecture() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.edit_layout, null)

        val format = "dd-MM-yyyy"
        val addTitle = v.findViewById<EditText>(R.id.addTitle)
        val addLectureInfo = v.findViewById<EditText>(R.id.addContent)
        val addImg = v.findViewById<EditText>(R.id.addImage)
        val addDate = v.findViewById<TextView>(R.id.addDate)
        val hw = v.findViewById<TextView>(R.id.addHomeWork)

        addLectureInfo.setText(intent.getStringExtra("content"))
        addDate.text = intent.getStringExtra("date")
        hw.text = "Homework: " + intent.getStringExtra("hw")
        addTitle.setText(intent.getStringExtra("title"))

        addDate.text = SimpleDateFormat(format).format(System.currentTimeMillis())
        val calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat(format, Locale.FRANCE)
            addDate.text = sdf.format(calendar.time)
        }
        addDate.setOnClickListener {
            DatePickerDialog(
                this@ContentActivity, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        val editDialog = AlertDialog.Builder(this)
        editDialog.setView(v)
        editDialog.setPositiveButton("Edit") { dialog, _ ->
            val content = addLectureInfo.text.toString()
            val date = addDate.text.toString()
            val homeWork = hw.text.toString()
            val imageId = addImg.text.toString()
            val title = addTitle.text.toString()
            val item = LectureJsonItem(
                content,
                date,
                homeWork,
                Id,
                imageId,
                title
            )
            if (TextUtils.isEmpty(addTitle.toString())) {
                addTitle.error = "Title cannot be empty"
                addTitle.requestFocus()
            } else if (TextUtils.isEmpty(addImg.toString())) {
                addImg.error = "Password cannot be empty"
                addImg.requestFocus()
            } else if (TextUtils.isEmpty(addLectureInfo.toString())) {
                addLectureInfo.error = "Content cannot be empty"
                addLectureInfo.requestFocus()
            } else {
                GlobalScope.launch {
                    editCurrentData(item)
                }
                Toast.makeText(
                    this,
                    intent.getStringExtra("title") + " edit Success",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
                finish()
            }
        }
            editDialog.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }
            editDialog.create()
            editDialog.show()
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

        private fun setClickable(clicked: Boolean) {
            val fabEdit = findViewById<FloatingActionButton>(R.id.fab_edit)
            val fabDelete = findViewById<FloatingActionButton>(R.id.fab_delete)
            if (!clicked) {
                fabDelete.isClickable = true
                fabEdit.isClickable = true
            } else {
                fabDelete.isClickable = false
                fabEdit.isClickable = false
            }
        }
    }