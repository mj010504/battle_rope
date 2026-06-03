package com.choiminjun.makeitall.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.makeitall.designsystem.theme.MakeitallTheme
import timber.log.Timber

@Composable
fun HomeRoute(
    navigateToExercise: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.collectAsState()

    val blePermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
        )
    } else {
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
        )
    }

    var allPermissionsGranted by remember { mutableStateOf(false) }

    LaunchedEffect(allPermissionsGranted) {
        if (allPermissionsGranted) {
            Timber.d("[HomeRoute] LaunchedEffect: 권한 허용됨 → navigateToExercise 호출")
            allPermissionsGranted = false
            navigateToExercise()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { result ->
        Timber.tag("Home").d("권한 결과: $result")
        val btGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            result[Manifest.permission.BLUETOOTH_SCAN] == true &&
                result[Manifest.permission.BLUETOOTH_CONNECT] == true
        } else {
            result[Manifest.permission.BLUETOOTH] == true &&
                result[Manifest.permission.BLUETOOTH_ADMIN] == true
        }
        Timber.tag("Home").d("btGranted=$btGranted")
        if (btGranted) {
            Timber.tag("Home").d("권한 통과 → allPermissionsGranted = true")
            allPermissionsGranted = true
        } else {
            Timber.tag("Home").w("권한 부족: bt=$btGranted")
        }
    }

    HomeScreen(
        uiState = uiState,
        onStartClick = {
            val alreadyGranted = blePermissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
            Timber.tag("Home").d("onStartClick: alreadyGranted=$alreadyGranted")
            if (alreadyGranted) {
                navigateToExercise()
            } else {
                permissionLauncher.launch(blePermissions)
            }
        },
    )
}

@Composable
fun HomeScreen(
    uiState: HomeState,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "스마트 줄넘기",
            style = MakeitallTheme.typography.headingXLSB,
        )
        Button(
            onClick = onStartClick,
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text(
                text = "운동 시작",
                style = MakeitallTheme.typography.bodyMSB,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MakeitallTheme {
        HomeScreen(
            uiState = HomeState(),
            onStartClick = {},
        )
    }
}
