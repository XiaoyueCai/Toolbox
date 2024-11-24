package com.cxy.toolbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cxy.toolbox.ui.record.RecordGroupListScreen
import com.cxy.toolbox.ui.theme.ToolboxTheme

class RecordGroupListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToolboxTheme {
                RecordGroupListScreen()
            }
        }
    }
}