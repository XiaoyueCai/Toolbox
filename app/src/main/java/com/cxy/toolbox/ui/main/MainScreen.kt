package com.cxy.toolbox.ui.main

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cxy.toolbox.RecordGroupListActivity
import com.cxy.toolbox.ui.requestPermissions
import com.cxy.toolbox.ui.theme.ToolboxTheme
import com.cxy.toolbox.utils.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Dev Toolbox")
                }
            )
        },
    ) { innerPadding ->
        ScrollContent(innerPadding)
    }
}

@Composable
private fun ScrollContent(innerPadding: PaddingValues) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val context = LocalContext.current

        Button(
            onClick = {
                Utils.openHealthConnect(context)
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Open Health Connect")
        }

        ReqPermissionsButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = {
                context.startActivity(Intent(context, RecordGroupListActivity::class.java))
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Insert Health Record")
        }
    }
}

@Composable
private fun ReqPermissionsButton(
    modifier: Modifier,
    onPermissionsResult: (Boolean) -> Unit = {}
) {
    val requestPermissionsClick = requestPermissions(onPermissionsResult)
    Button(
        modifier = modifier,
        onClick = requestPermissionsClick
    ) {
        Text(text = "Request all permissions")
    }
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun MainScreenPreview() {
    ToolboxTheme {
        MainScreen()
    }
}