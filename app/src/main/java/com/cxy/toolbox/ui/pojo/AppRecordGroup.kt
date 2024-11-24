package com.cxy.toolbox.ui.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppRecordGroup(val name:String, val types: List<AppRecordType>) : Parcelable {

    companion object {
        val GROUP_ACTIVITY = AppRecordGroup(
            "Activity",
            listOf(AppRecordType.TYPE_STEPS)
        )

        val GROUP_SLEEP = AppRecordGroup(
            "Sleep",
            listOf(AppRecordType.TYPE_SLEEP_SESSION)
        )

        val GROUP_VITALS = AppRecordGroup(
            "Vitals",
            listOf(AppRecordType.TYPE_HEART_RATE_VARIABILITY)
        )

        val GROUP_LIST = listOf(GROUP_ACTIVITY, GROUP_SLEEP, GROUP_VITALS)
    }
}
