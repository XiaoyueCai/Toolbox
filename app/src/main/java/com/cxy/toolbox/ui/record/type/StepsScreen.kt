package com.cxy.toolbox.ui.record.type

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cxy.toolbox.ui.AdvancedTimePicker
import com.cxy.toolbox.ui.DateRangePickerModal
import com.cxy.toolbox.ui.getActivity
import com.cxy.toolbox.ui.pojo.AppRecordType
import com.cxy.toolbox.ui.pojo.HourMinute
import com.cxy.toolbox.ui.theme.ToolboxTheme
import com.cxy.toolbox.utils.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsScreen(
    type: AppRecordType,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Insert Record: ${type.name}")
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
        ScrollContent(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScrollContent(
    innerPadding: PaddingValues,
) {
    var showRangeModal by rememberSaveable { mutableStateOf(false) }
    var selectedDateRange by rememberSaveable {
        mutableStateOf<Pair<Long?, Long?>>(null to null)
    }

    var showMinTimeModal by rememberSaveable { mutableStateOf(false) }
    var selectedMinTime by rememberSaveable {
        mutableStateOf(
            HourMinute(0, 1)
        )
    }

    var showMaxTimeModal by rememberSaveable { mutableStateOf(false) }
    var selectedMaxTime by rememberSaveable {
        mutableStateOf(
            HourMinute(0, 30)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "Start Date - End Date",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )

            Button(
                onClick = { showRangeModal = true },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                if (selectedDateRange.first != null && selectedDateRange.second != null) {
                    val formattedStartDate = Utils.convertMillisToDate(selectedDateRange.first!!)
                    val formattedEndDate = Utils.convertMillisToDate(selectedDateRange.second!!)
                    Text("$formattedStartDate - $formattedEndDate")
                } else {
                    Text("No date range selected")
                }
            }

            if (showRangeModal) {
                DateRangePickerModal(
                    initialSelectedStartDateMillis = selectedDateRange.first,
                    initialSelectedEndDateMillis = selectedDateRange.second,
                    onDateRangeSelected = {
                        selectedDateRange = it
                        showRangeModal = false
                    },
                    onDismiss = { showRangeModal = false }
                )
            }
        }

        item {
            Text(
                "Min Duration",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        showMinTimeModal = true
                    }
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = null,
                )

                Text(selectedMinTime.toString())
            }

            if (showMinTimeModal) {
                AdvancedTimePicker(
                    title = "Select Min Duration",
                    hourInit = selectedMinTime.hour,
                    minuteInit = selectedMinTime.minute,
                    onConfirm = {
                        selectedMinTime = Utils.convertTimeStateToHourMinute(it)
                        showMinTimeModal = false
                    },
                    onDismiss = {
                        showMinTimeModal = false
                    }
                )
            }
        }

        item {
            Text(
                "Max Duration",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        showMaxTimeModal = true
                    }
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = null,
                )

                Text(selectedMaxTime.toString())
            }

            if (showMaxTimeModal) {
                AdvancedTimePicker(
                    title = "Select Max Duration",
                    hourInit = selectedMaxTime.hour,
                    minuteInit = selectedMaxTime.minute,
                    onConfirm = {
                        selectedMaxTime = Utils.convertTimeStateToHourMinute(it)
                        showMaxTimeModal = false
                    },
                    onDismiss = {
                        showMaxTimeModal = false
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun StepsScreenPreview() {
    ToolboxTheme {
        StepsScreen(
            type = AppRecordType.TYPE_STEPS
        )
    }
}