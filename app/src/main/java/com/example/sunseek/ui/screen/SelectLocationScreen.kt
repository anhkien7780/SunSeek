package com.example.sunseek.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sunseek.R
import com.example.sunseek.ui.theme.SunSeekTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLocationScreen(fusedLocationClient: FusedLocationProviderClient) {
    var searchTextValue by remember { mutableStateOf("") }
    val localManager = LocalFocusManager.current
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    TextField(
                        value = searchTextValue,
                        onValueChange = { searchTextValue = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        ),
                        placeholder = { Text(text = stringResource(R.string.enter_address)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    localManager.clearFocus(true)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = stringResource(R.string.search_button)
                                )
                            }
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                localManager.clearFocus(true)
                            }
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(
                                R.string.back_button
                            )
                        )
                    }
                },
            )
        }) { innerPadding ->
        var latitude by remember { mutableStateOf("0.0") }
        var longitude by remember { mutableStateOf("0.0") }
        var singapore by remember {
            mutableStateOf(
                LatLng(
                    latitude.toDouble(),
                    longitude.toDouble()
                )
            )
        }
        val markerState = rememberMarkerState(position = singapore)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }

        Box(modifier = Modifier.padding(innerPadding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = markerState,
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }
            TextButton(
                onClick = {},
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .size(width = 310.dp, height = 40.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Xác nhận", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}


@Preview
@Composable
fun SelectLocationScreenPreview() {
    SunSeekTheme {
        SelectLocationScreen(
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                LocalContext.current
            )
        )
    }
}