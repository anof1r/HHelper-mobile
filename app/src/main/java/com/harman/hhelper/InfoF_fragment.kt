package com.harman.hhelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class InfoF_fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_f_fragment, container, false)

        val textTitle = view.findViewById<TextView>(R.id.ff_title)
        textTitle.text = activity?.intent?.extras?.getString("links")
        return view
    }

}