package com.cxy.toolbox.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.launch

@Composable
fun ComposableLifecycle(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun requestPermissions(
    onPermissionsResult: (Boolean) -> Unit = {}
) : ()-> Unit {
    val permissions =
        setOf(
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getWritePermission(StepsRecord::class)
        )
    val context = LocalContext.current

    // Create the permissions launcher
    val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
    val requestPermissions = rememberLauncherForActivityResult(requestPermissionActivityContract) { granted ->
        if (granted.containsAll(permissions)) {
            // Permissions successfully granted
            onPermissionsResult(true)
        } else {
            // Lack of required permissions
            onPermissionsResult(false)
        }
    }

    val coroutineScope = rememberCoroutineScope()

    return {
        coroutineScope.launch { // Launch a coroutine in the lifecycleScope
            val healthConnectClient = HealthConnectClient.getOrCreate(context)
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (granted.containsAll(permissions)) {
                // Permissions already granted; proceed with inserting or reading data
                onPermissionsResult(true)
            } else {
                requestPermissions.launch(permissions)
            }
        }
    }
}
