package com.cxy.toolbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cxy.toolbox.ui.main.MainScreen
import com.cxy.toolbox.ui.theme.ToolboxTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ToolboxTheme {
                MainScreen()
            }
        }
    }

}