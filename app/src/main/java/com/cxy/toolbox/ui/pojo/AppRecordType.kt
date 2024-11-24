package com.cxy.toolbox.ui.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppRecordType(val name: String) : Parcelable {

    companion object {
        val TYPE_STEPS = AppRecordType("Steps")

        val TYPE_SLEEP_SESSION = AppRecordType("Sleep Session")

        val TYPE_HEART_RATE_VARIABILITY = AppRecordType("Heart Rate Variability RMSSD")
    }
}
