package com.example.android.politicalpreparedness.utils

import android.view.View
import androidx.fragment.app.Fragment

fun Fragment.setOnClick(views: List<View>, listener : View.OnClickListener){
    views.map {
        it.setOnClickListener(listener)
    }
}