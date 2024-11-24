package com.cxy.toolbox.utils

import android.content.Context
import android.content.Intent
import androidx.health.connect.client.HealthConnectClient.Companion.ACTION_HEALTH_CONNECT_SETTINGS

object Utils {

    fun openHealthConnect(context: Context) {
        val intent = Intent(ACTION_HEALTH_CONNECT_SETTINGS)
        context.startActivity(intent)
    }

}