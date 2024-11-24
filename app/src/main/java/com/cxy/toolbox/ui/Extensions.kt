package com.cxy.toolbox.ui

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun LocalDate.days(date2: LocalDate): Int {
    val daysBetween = ChronoUnit.DAYS.between(this, date2)
    return daysBetween.toInt()
}
