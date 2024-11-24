package com.cxy.toolbox.utils

import android.content.Context
import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.health.connect.client.HealthConnectClient.Companion.ACTION_HEALTH_CONNECT_SETTINGS
import com.cxy.toolbox.ui.pojo.HourMinute
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun openHealthConnect(context: Context) {
        val intent = Intent(ACTION_HEALTH_CONNECT_SETTINGS)
        context.startActivity(intent)
    }

    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun convertTimeStateToHourMinute(selectedMinTime: TimePickerState?): HourMinute {
        val hour = selectedMinTime?.hour ?: 0
        val min = selectedMinTime?.minute ?: 0
        return HourMinute(hour, min)
    }
}