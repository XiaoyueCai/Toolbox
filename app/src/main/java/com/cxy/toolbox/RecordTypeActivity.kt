package com.cxy.toolbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.IntentCompat
import com.cxy.toolbox.ui.ToDoScreen
import com.cxy.toolbox.ui.pojo.AppRecordType
import com.cxy.toolbox.ui.record.type.HRVScreen
import com.cxy.toolbox.ui.record.type.SleepScreen
import com.cxy.toolbox.ui.record.type.StepsScreen
import com.cxy.toolbox.ui.theme.ToolboxTheme

class RecordTypeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type =
            IntentCompat.getParcelableExtra(intent, EXTRA_RECORD_TYPE, AppRecordType::class.java)
                ?: AppRecordType.TYPE_STEPS
        enableEdgeToEdge()
        setContent {
            ToolboxTheme {
                when (type) {
                    AppRecordType.TYPE_STEPS -> StepsScreen(type)
                    AppRecordType.TYPE_SLEEP_SESSION -> SleepScreen(type)
                    AppRecordType.TYPE_HEART_RATE_VARIABILITY -> HRVScreen(type)
                    else -> ToDoScreen()
                }
            }
        }
    }

    companion object {
        const val EXTRA_RECORD_TYPE = "recordType"
    }
}