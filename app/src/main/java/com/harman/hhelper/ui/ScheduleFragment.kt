package com.harman.hhelper.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.harman.hhelper.R


class ScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.schedule_fragment, container, false)

        val textTitle = view.findViewById<TextView>(R.id.tf_title)
        textTitle.text = activity?.intent?.extras?.getString("schedule")
        return view
    }

}