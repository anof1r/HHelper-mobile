package com.harman.hhelper.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.harman.hhelper.R


class LiteratureFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infoLayout : InfoLayout
        val view = inflater.inflate(R.layout.literature_fragment, container, false)

        val textTitle = view.findViewById<TextView>(R.id.sf_title)
        textTitle.text = activity?.intent?.extras?.getString("literature")
        return view
    }

}