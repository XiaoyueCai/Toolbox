package com.cxy.toolbox.ui.record.type

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.SleepSessionRecord
import com.cxy.toolbox.ui.AdvancedTimePicker
import com.cxy.toolbox.ui.DateRangePickerModal
import com.cxy.toolbox.ui.days
import com.cxy.toolbox.ui.getActivity
import com.cxy.toolbox.ui.pojo.AppRecordType
import com.cxy.toolbox.ui.pojo.CheckBoxItem
import com.cxy.toolbox.ui.pojo.HourMinute
import com.cxy.toolbox.ui.requestPermissions
import com.cxy.toolbox.ui.theme.ToolboxTheme
import com.cxy.toolbox.utils.Utils
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.ceil
import kotlin.random.Random

private const val TAG = "SleepScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepScreen(
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
            HourMinute(6, 30)
        )
    }

    var showMaxTimeModal by rememberSaveable { mutableStateOf(false) }
    var selectedMaxTime by rememberSaveable {
        mutableStateOf(
            HourMinute(9, 30)
        )
    }

    var showEachStageTimeModal by rememberSaveable { mutableStateOf(false) }
    var selectedEachStageTime by rememberSaveable {
        mutableStateOf(
            HourMinute(0, 10)
        )
    }

    val stages = remember {
        mutableStateListOf(
            CheckBoxItem(SleepSessionRecord.STAGE_TYPE_UNKNOWN, "Unknown", false),
            CheckBoxItem(SleepSessionRecord.STAGE_TYPE_AWAKE, "Awake", true),
            CheckBoxItem(SleepSessionRecord.STAGE_TYPE_SLEEPING, "Sleeping", false),
            CheckBoxItem(SleepSessionRecord.STAGE_TYPE_OUT_OF_BED, "Out of bed", false),
            CheckBoxItem(SleepSessionRecord.STAGE_TYPE_LIGHT, "Light", true),
            CheckBoxItem(SleepSessionRecord.STAGE_TYPE_DEEP, "Deep", true),
            CheckBoxItem(SleepSessionRecord.STAGE_TYPE_REM, "REM", true),
            CheckBoxItem(SleepSessionRecord.STAGE_TYPE_AWAKE_IN_BED, "Awake in bed", false)
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
                "Min Sleep Duration",
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
                        width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp)
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
                "Max Sleep Duration",
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
                        width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp)
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

        item {
            Text(
                "Each stage Duration",
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
                        width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        showEachStageTimeModal = true
                    }
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = null,
                )

                Text(selectedEachStageTime.toString())
            }

            if (showEachStageTimeModal) {
                AdvancedTimePicker(
                    title = "Select Each Stage Duration",
                    hourInit = selectedEachStageTime.hour,
                    minuteInit = selectedEachStageTime.minute,
                    onConfirm = {
                        selectedEachStageTime = Utils.convertTimeStateToHourMinute(it)
                        showEachStageTimeModal = false
                    },
                    onDismiss = {
                        showEachStageTimeModal = false
                    }
                )
            }
        }

        item {
            CheckboxGrid(stages) { id, checked ->
                val index = stages.indexOfFirst { it.id == id }
                if (index != -1) stages[index] = stages[index].copy(isChecked = checked)
            }
        }

        item {
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val onPermissionsResult = { granted: Boolean ->
                if (granted) {
                    coroutineScope.launch { // Launch a coroutine in the lifecycleScope
                        val healthConnectClient = HealthConnectClient.getOrCreate(context)
                        val selectedStage = stages.filter { it.isChecked }.map { it.id }
                        if (selectedDateRange.first != null && selectedDateRange.second != null) {
                            insertSleep(
                                context,
                                healthConnectClient,
                                startMillis = selectedDateRange.first!!,
                                endMillis = selectedDateRange.second!!,
                                minSleepDuration = selectedMinTime,
                                maxSleepDuration = selectedMaxTime,
                                eachStageDuration = selectedEachStageTime,
                                stages = selectedStage,
                            )
                        }
                    }
                }
            }
            val requestPermissionsClick = requestPermissions(onPermissionsResult)

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                onClick = requestPermissionsClick,
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
private fun CheckboxGrid(items: List<CheckBoxItem>, onCheckedChange: (Int, Boolean) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        for (row in 0 until 3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    val item = items.getOrNull(index)
                    if (item != null) {
                        Row(
                            modifier = Modifier
                                .height(56.dp)
                                .toggleable(
                                    value = item.isChecked,
                                    onValueChange = { checked ->
                                        onCheckedChange(
                                            item.id,
                                            checked
                                        )
                                    },
                                    role = Role.Checkbox
                                )
                                .padding(4.dp)
                        ) {
                            Checkbox(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically),
                                checked = item.isChecked,
                                onCheckedChange = null
                            )
                            Text(
                                text = item.label,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .align(Alignment.CenterVertically),
                            )
                        }
                    }
                }
            }
        }
    }
}

suspend fun insertSleep(
    context: Context,
    healthConnectClient: HealthConnectClient,
    startMillis: Long,
    endMillis: Long,
    minSleepDuration: HourMinute,
    maxSleepDuration: HourMinute,
    eachStageDuration: HourMinute,
    stages: List<Int>,
) {
    try {
        val zoneOffset = OffsetDateTime.now().offset
        val startDateTime = Utils.millisToLocalDateTime(startMillis)
        val endDateTime = Utils.millisToLocalDateTime(endMillis)
        val records = mutableListOf<SleepSessionRecord>()
        val minDurationMillis =
            Duration.ofHours(minSleepDuration.hour.toLong())
                .plusMinutes(minSleepDuration.minute.toLong())
                .toMillis()
        val maxDurationMillis =
            Duration.ofHours(maxSleepDuration.hour.toLong())
                .plusMinutes(maxSleepDuration.minute.toLong())
                .toMillis()
        val currentDateTime = LocalDateTime.now()
        for (i in 0..startDateTime.toLocalDate().days(endDateTime.toLocalDate())) {
            val dateTime = startDateTime.plusDays(i.toLong())
            // 23:00 - 1:00
            val hour = Random.nextInt(23, 26)
            val minusDays = if (hour < 24) 1L else 0L
            val startTime = dateTime.minusDays(minusDays)
                .withHour(hour % 24)
                .withMinute(Random.nextInt(0, 60))
                .toInstant(zoneOffset)
            var endTime =
                startTime.plusMillis(Random.nextLong(minDurationMillis, maxDurationMillis))
            if (endTime.isAfter(currentDateTime.toInstant(zoneOffset))) {
                endTime = currentDateTime.toInstant(zoneOffset)
            }
            val sleepDurationMillis = startTime.until(endTime, ChronoUnit.MILLIS)
            if (sleepDurationMillis < minDurationMillis) {
                continue
            }
            val sleepStages = mutableListOf<SleepSessionRecord.Stage>()
            if (stages.isNotEmpty()) {
                val eachStageDurationMillis =
                    Duration.ofHours(eachStageDuration.hour.toLong())
                        .plusMinutes(eachStageDuration.minute.toLong())
                        .toMillis()
                val stageCount =
                    ceil(sleepDurationMillis.toDouble() / eachStageDurationMillis).toInt()
                var prevStage = -1
                for (j in 0 until stageCount) {
                    var stage: Int
                    do {
                        stage = stages.random()
                    } while (prevStage == stage)
                    val startStageTime = startTime.plusMillis(j * eachStageDurationMillis)
                    var endStageTime = startTime.plusMillis((j + 1) * eachStageDurationMillis)
                    if (endStageTime.isAfter(endTime)) {
                        endStageTime = endTime
                    }
                    sleepStages.add(
                        SleepSessionRecord.Stage(
                            startTime = startStageTime,
                            endTime = endStageTime,
                            stage = stage
                        )
                    )
                    prevStage = stage
                }
            }
            val stepsRecord = SleepSessionRecord(
                startTime = startTime,
                endTime = endTime,
                startZoneOffset = zoneOffset,
                endZoneOffset = zoneOffset,
                stages = sleepStages
            )
            records.add(stepsRecord)
        }
        Log.d(TAG, "insertSleep: $records")
        val response = healthConnectClient.insertRecords(records)
        if (response.recordIdsList.isNotEmpty()) {
            Toast.makeText(context, "Sleep inserted", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Log.e(TAG, "insertSleep: ", e)
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun SleepScreenPreview() {
    ToolboxTheme {
        SleepScreen(
            type = AppRecordType.TYPE_SLEEP_SESSION
        )
    }
}