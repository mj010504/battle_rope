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

@Composable
fun HomeRoute(
    navigateToExercise: () -> Unit,
    navigateToCompetition: () -> Unit,
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

    var pendingTarget by remember { mutableStateOf<BlePermissionTarget?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { results ->
        val granted = results.values.all { it }
        pendingTarget?.let { target ->
            viewModel.onIntent(HomeIntent.BlePermissionResult(granted, target))
        }
        pendingTarget = null
    }

    viewModel.collectSideEffect { sideEffect ->
        fun requestPermission(target: BlePermissionTarget) {
            val alreadyGranted = blePermissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
            if (alreadyGranted) {
                viewModel.onIntent(HomeIntent.BlePermissionResult(true, target))
            } else {
                pendingTarget = target
                permissionLauncher.launch(blePermissions)
            }
        }

        when (sideEffect) {
            HomeSideEffect.RequestBlePermissionForExercise -> requestPermission(BlePermissionTarget.EXERCISE)
            HomeSideEffect.RequestBlePermissionForCompetition -> requestPermission(BlePermissionTarget.COMPETITION)
            HomeSideEffect.NavigateToExercise -> navigateToExercise()
            HomeSideEffect.NavigateToCompetition -> navigateToCompetition()
        }
    }

    HomeScreen(
        uiState = uiState,
        onStartClick = { viewModel.onIntent(HomeIntent.ClickStartExercise) },
        onCompetitionClick = { viewModel.onIntent(HomeIntent.ClickCompetitionMode) },
    )
}

@Composable
fun HomeScreen(
    uiState: HomeState,
    onStartClick: () -> Unit,
    onCompetitionClick: () -> Unit,
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
                text = "테스트",
                style = MakeitallTheme.typography.bodyMSB,
            )
        }
        Button(
            onClick = onCompetitionClick,
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(
                text = "경쟁 모드",
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
            onCompetitionClick = {},
        )
    }
}
