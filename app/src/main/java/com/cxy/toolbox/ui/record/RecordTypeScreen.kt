package com.cxy.toolbox.ui.record

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cxy.toolbox.RecordTypeActivity
import com.cxy.toolbox.RecordTypeActivity.Companion.EXTRA_RECORD_TYPE
import com.cxy.toolbox.ui.getActivity
import com.cxy.toolbox.ui.pojo.AppRecordGroup
import com.cxy.toolbox.ui.theme.ToolboxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordGroupScreen(
    group: AppRecordGroup,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Insert Record: ${group.name}")
                },
                navigationIcon = {
                    val context: Context = LocalContext.current
                    IconButton(
                        onClick = {
                            context.getActivity()?.finish()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        ScrollContent(innerPadding, group)
    }
}

@Composable
private fun ScrollContent(
    innerPadding: PaddingValues,
    group: AppRecordGroup,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = innerPadding,
    ) {
        group.types.map { appRecordType ->
            item {
                val context = LocalContext.current

                Text(
                    text = appRecordType.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            context.startActivity(
                                Intent(context, RecordTypeActivity::class.java)
                                    .apply {
                                        putExtra(EXTRA_RECORD_TYPE, appRecordType)
                                    }
                            )
                        }
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun RecordGroupScreenPreview() {
    ToolboxTheme {
        RecordGroupScreen(
            group = AppRecordGroup.GROUP_ACTIVITY
        )
    }
}