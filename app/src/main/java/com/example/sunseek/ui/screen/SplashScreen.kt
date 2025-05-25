package com.example.sunseek.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunseek.R
import com.example.sunseek.ui.theme.SunSeekTheme
import com.example.sunseek.viewmodel.ServerViewModel
import kotlinx.serialization.Serializable

@Serializable
object Splash

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    serverViewModel: ServerViewModel,
    onRefresh: () -> Unit,
    onNavigate: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        serverViewModel.isNetworkAvailable(context)
    }

    val isNetworkAvailable by serverViewModel.isNetworkAvailable.collectAsState()
    val isServerRunning by serverViewModel.isServerRunning.collectAsState()
    val isRequestTimeOut by serverViewModel.isRequestTimeOut.collectAsState()
    when (isRequestTimeOut) {
        true -> TimeOutBody()
        false -> Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (isNetworkAvailable) {
                null -> {}
                false -> NetWorkUnavailableBody(onRefresh)
                true -> {
                    LaunchedEffect(Unit) {
                        serverViewModel.wakeServer()
                    }
                    LaunchedEffect(isServerRunning) {
                        if (isServerRunning) {
                            onNavigate()
                        }
                    }
                    Image(
                        modifier = Modifier.size(200.dp),
                        painter = painterResource(R.drawable.sunseek_logo),
                        contentDescription = "Logo"
                    )
                    Text(
                        text = "SunSeek",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun NetWorkUnavailableBody(onRefresh: () -> Unit) {
    IconButton(onClick = onRefresh) {
        Icon(
            painter = painterResource(R.drawable.autorenew),
            contentDescription = stringResource(R.string.refresh)
        )
    }
    Text(
        text = stringResource(R.string.reconnect),
        style = MaterialTheme.typography.labelLarge
    )
}

@Composable
fun TimeOutBody() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.server_no_response),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SunSeekTheme {
        SplashScreen(
            onNavigate = {},
            modifier = Modifier,
            serverViewModel = viewModel(),
            onRefresh = {})
    }
}
