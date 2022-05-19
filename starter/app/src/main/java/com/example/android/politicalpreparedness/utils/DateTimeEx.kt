package com.example.android.politicalpreparedness.utils

import java.text.SimpleDateFormat
import java.util.*

fun format(date:Date):String{
    val formatter = SimpleDateFormat("dd MMMM yyyy")
    return formatter.format(date)
}