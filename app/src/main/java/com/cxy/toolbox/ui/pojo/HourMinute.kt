package com.cxy.toolbox.ui.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HourMinute(val hour: Int, val minute: Int) : Parcelable {

    override fun toString(): String {
        val hour = if (hour > 0) "${hour}h" else ""
        val min = "${minute}min"
        return "$hour $min"
    }
}
