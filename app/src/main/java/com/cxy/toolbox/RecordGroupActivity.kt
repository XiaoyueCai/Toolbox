package com.cxy.toolbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.IntentCompat
import com.cxy.toolbox.ui.record.RecordGroupScreen
import com.cxy.toolbox.ui.pojo.AppRecordGroup
import com.cxy.toolbox.ui.theme.ToolboxTheme

class RecordGroupActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val group = IntentCompat.getParcelableExtra(intent, EXTRA_RECORD_GROUP, AppRecordGroup::class.java) ?: AppRecordGroup.GROUP_ACTIVITY
        setContent {
            ToolboxTheme {
                RecordGroupScreen(
                    group = group,
                )
            }
        }
    }

    companion object {
        const val EXTRA_RECORD_GROUP = "recordGroup"
    }
}