package com.cxy.toolbox.ui.record.type

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import com.cxy.toolbox.ui.DateRangePickerModal
import com.cxy.toolbox.ui.days
import com.cxy.toolbox.ui.getActivity
import com.cxy.toolbox.ui.pojo.AppRecordType
import com.cxy.toolbox.ui.requestPermissions
import com.cxy.toolbox.ui.theme.ToolboxTheme
import com.cxy.toolbox.utils.Utils
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import kotlin.random.Random

private const val TAG = "HRVScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HRVScreen(
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

@Composable
private fun ScrollContent(
    innerPadding: PaddingValues,
) {
    var showRangeModal by rememberSaveable { mutableStateOf(false) }
    var selectedDateRange by rememberSaveable {
        mutableStateOf<Pair<Long?, Long?>>(null to null)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val numericRegex by remember { mutableStateOf(Regex("[0-9]+")) }

    var minDailyRecordCount by rememberSaveable { mutableStateOf("1") }
    var maxDailyRecordCount by rememberSaveable { mutableStateOf("10") }
    var minHRV by rememberSaveable { mutableStateOf("16") }
    var maxHRV by rememberSaveable { mutableStateOf("80") }

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
            TextField(
                value = minDailyRecordCount,
                onValueChange = { newText ->
                    minDailyRecordCount = numericRegex.find(newText)?.value ?: ""
                },
                label = {
                    Text("Min Daily Record Count")
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }

        item {
            TextField(
                value = maxDailyRecordCount,
                onValueChange = { newText ->
                    maxDailyRecordCount = numericRegex.find(newText)?.value ?: ""
                },
                label = {
                    Text("Max Daily Record Count")
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }

        item {
            TextField(
                value = minHRV,
                onValueChange = { newText ->
                    minHRV = numericRegex.find(newText)?.value ?: ""
                },
                label = {
                    Text("Min HRV")
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }

        item {
            TextField(
                value = maxHRV,
                onValueChange = { newText ->
                    maxHRV = numericRegex.find(newText)?.value ?: ""
                },
                label = {
                    Text("Max HRV")
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }

        item {
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val onPermissionsResult = { granted: Boolean ->
                if (granted) {
                    coroutineScope.launch { // Launch a coroutine in the lifecycleScope
                        val healthConnectClient = HealthConnectClient.getOrCreate(context)
                        if (selectedDateRange.first != null && selectedDateRange.second != null) {
                            insertHRV(
                                context,
                                healthConnectClient,
                                startTime = selectedDateRange.first!!,
                                endTime = selectedDateRange.second!!,
                                minDailyRecordCount = minDailyRecordCount.toInt(),
                                maxDailyRecordCount = maxDailyRecordCount.toInt(),
                                minHRV = minHRV.toInt(),
                                maxHRV = maxHRV.toInt()
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

suspend fun insertHRV(
    context: Context,
    healthConnectClient: HealthConnectClient,
    startTime: Long,
    endTime: Long,
    minDailyRecordCount: Int,
    maxDailyRecordCount: Int,
    minHRV: Int,
    maxHRV: Int,
) {
    try {
        val zoneOffset = OffsetDateTime.now().offset
        val startDateTime = Utils.millisToLocalDateTime(startTime)
        val endDateTime = Utils.millisToLocalDateTime(endTime)
        val records = mutableListOf<HeartRateVariabilityRmssdRecord>()
        val currentDateTime = LocalDateTime.now()
        for (i in 0..startDateTime.toLocalDate().days(endDateTime.toLocalDate())) {
            val dateTime = startDateTime.plusDays(i.toLong())
            var randomDateTime = dateTime.withHour(Random.nextInt(20, 24))
                .withMinute(Random.nextInt(0, 60))
            if (randomDateTime.isAfter(currentDateTime)) {
                randomDateTime = currentDateTime
            }
            val dateMillis = Duration.ofHours(randomDateTime.hour.toLong())
                .plusMinutes(randomDateTime.minute.toLong()).toMillis()
            var end = randomDateTime.toInstant(zoneOffset)
            val dailyRecordCount = Random.nextInt(minDailyRecordCount, maxDailyRecordCount + 1)
            val minIntervalMillis = dateMillis / (maxDailyRecordCount + 1)
            val maxIntervalMillis = dateMillis / (dailyRecordCount + 1)
            for (j in 0 until dailyRecordCount) {
                val minusMillis = if (minIntervalMillis == maxIntervalMillis) {
                    maxIntervalMillis
                } else {
                    Random.nextLong(minIntervalMillis, maxIntervalMillis)
                }
                val time = end.minusMillis(minusMillis)
                val hrvRecord = HeartRateVariabilityRmssdRecord(
                    time = time,
                    zoneOffset = zoneOffset,
                    heartRateVariabilityMillis = Random.nextInt(minHRV, maxHRV + 1).toDouble(),
                )
                records.add(hrvRecord)
                end = time
            }
        }
        Log.d(TAG, "insertHRV: $records")
        healthConnectClient.insertRecords(records)
        Toast.makeText(context, "HRV inserted", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Log.e(TAG, "insertHRV: ", e)
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun HRVScreenPreview() {
    ToolboxTheme {
        HRVScreen(
            type = AppRecordType.TYPE_HEART_RATE_VARIABILITY
        )
    }
}