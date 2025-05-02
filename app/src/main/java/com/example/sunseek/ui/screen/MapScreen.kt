package com.example.sunseek.ui.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sunseek.R
import com.example.sunseek.ui.theme.SunSeekTheme
import com.example.sunseek.viewmodel.LoadingUIState
import com.example.sunseek.viewmodel.LocationViewModel
import com.example.sunseek.viewmodel.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object Map


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBack: () -> Unit,
    fusedLocationClient: FusedLocationProviderClient,
    locationViewModel: LocationViewModel,
    mapViewModel: MapViewModel,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationUIState by locationViewModel.loadingUIState.collectAsState()
    var addressInput by remember { mutableStateOf("") }
    val currentLocation = mapViewModel.currentLocation.collectAsState()
    val localManager = LocalFocusManager.current
    var openDialogState by remember { mutableStateOf(false) }
    val location by mapViewModel.location.collectAsState()
    val cameraPositionState = rememberCameraPositionState()
    var lastLocation by remember { mutableStateOf<LatLng?>(null) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        run {
            if (isGranted) {
                locationViewModel.getLastLocation(
                    fusedLocationProviderClient = fusedLocationClient,
                    onLocationReceived = { latLng ->
                        lastLocation = latLng
                        mapViewModel.setCurrentLocation(context, latLng)
                        cameraPositionState.position =
                            CameraPosition.fromLatLngZoom(lastLocation ?: LatLng(1.0, 1.0), 15f)
                    }
                )
            }
        }
    }

    // Check permission
    LaunchedEffect(Unit) {
        locationViewModel.getLastLocation(
            fusedLocationProviderClient = fusedLocationClient,
            onLocationReceived = { latLng ->
                mapViewModel.setCurrentLocation(context, latLng)
                lastLocation = latLng
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(lastLocation ?: LatLng(1.0, 1.0), 15f)
            },
            onPermissionDenied = {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            OutlinedIconButton(
                modifier = Modifier.padding(bottom = 60.dp, end = 10.dp),
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    scope.launch {
                        currentLocation.value?.let {
                            lastLocation = LatLng(it.latitude, it.longitude)
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLng(
                                    LatLng(
                                        it.latitude,
                                        it.longitude
                                    )
                                )
                            )
                        }
                    }
                }
            ) {
                Icon(imageVector = Icons.Outlined.Place, contentDescription = "Vị trí hiện tại")
            }
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    TextField(
                        value = addressInput,
                        onValueChange = { addressInput = it },
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
                            // Search location button
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        mapViewModel.getLocation(context, addressInput)
                                        location?.let {
                                            LatLng(
                                                it.latitude,
                                                it.longitude
                                            )
                                        }?.let {
                                            lastLocation = it
                                            CameraUpdateFactory.newLatLng(it)
                                        }?.let {
                                            cameraPositionState.animate(
                                                update = it
                                            )
                                        }
                                    }
                                }) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = stringResource(R.string.search_button)
                                )
                            }
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                localManager.clearFocus(true)
                            })
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
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
        Box(modifier = Modifier.padding(innerPadding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    lastLocation = latLng
                    scope.launch {
                        mapViewModel.getAddressName(
                            context,
                            lon = latLng.longitude.toString(),
                            lat = latLng.latitude.toString()
                        )

                    }
                }
            ) {
                lastLocation?.let { MarkerState(position = it) }?.let {
                    Marker(
                        state = it,
                        title = stringResource(R.string.selected_location),
                    )
                }
            }
            TextButton(
                onClick = {
                    openDialogState = true
                },
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .size(width = 310.dp, height = 40.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        when {
            openDialogState -> AlertDialog(
                onDismissRequest = { openDialogState = false },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Cancel Button
                        Button(
                            onClick = {
                                openDialogState = false
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.cancel),
                                contentDescription = stringResource(
                                    R.string.cancel
                                )
                            )
                            Spacer(Modifier.size(8.dp))
                            Text(
                                stringResource(R.string.disagree),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        // Accept Button
                        Button(
                            onClick = {
                                scope.launch {
                                    if (mapViewModel.isCurrentLocation()) {
                                        currentLocation.value?.let {
                                            locationViewModel.addAddress(context, it) {
                                                openDialogState = false
                                            }
                                        }
                                        locationViewModel.getListLocation(context)
                                    } else {
                                        location?.let {
                                            locationViewModel.addAddress(
                                                context,
                                                it
                                            ) { openDialogState = false }
                                            locationViewModel.getListLocation(context)
                                        }
                                    }
                                }
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.check_circle),
                                contentDescription = stringResource(R.string.accept)
                            )
                            Spacer(Modifier.size(8.dp))
                            Text(
                                stringResource(R.string.agree),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                textContentColor = MaterialTheme.colorScheme.onSurface,
                title = {
                    Text(
                        text = stringResource(R.string.save_location_ask),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                text = {
                    (if (location != null) location!!.name.split('%')[0]
                    else stringResource(R.string.current_location)).let {
                        Text(
                            text = it, style = MaterialTheme.typography.labelLarge
                        )
                    }
                })
        }

        when (locationUIState) {
            LoadingUIState.Failed -> {}
            LoadingUIState.Idle -> {}
            LoadingUIState.Loading -> FullScreenLoading(innerPadding = innerPadding)
            LoadingUIState.Success -> {}
        }

    }
}


@Preview
@Composable
fun SelectLocationScreenPreview() {
    SunSeekTheme {
        MapScreen(
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                LocalContext.current
            ), locationViewModel = LocationViewModel(), onBack = {}, mapViewModel = MapViewModel()
        )
    }
}