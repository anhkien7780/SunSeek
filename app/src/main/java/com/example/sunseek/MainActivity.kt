package com.example.sunseek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sunseek.ui.theme.SunSeekTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SunSeekTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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

                    Column(modifier = Modifier.padding(innerPadding)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextField(
                                value = latitude,
                                onValueChange = { latitude = it },
                                label = { Text(text = "Latitude") },
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.width(100.dp)
                            )
                            TextField(
                                value = longitude,
                                onValueChange = { longitude = it },
                                label = { Text(text = "Longitude") },
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.width(100.dp)
                            )
                            Button(
                                onClick = {
                                    singapore = LatLng(latitude.toDouble(), longitude.toDouble())
                                    markerState.position = LatLng(latitude.toDouble(), longitude.toDouble())
                                    cameraPositionState.position = CameraPosition.fromLatLngZoom(singapore, 10f)
                                }, shape = RectangleShape, modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(text = "Set location")
                            }
                        }
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
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewFunction() {

    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = latitude.toString(),
            onValueChange = { latitude = it.toDouble() },
            label = { Text(text = "Latitude") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(100.dp)
        )
        TextField(
            value = longitude.toString(),
            onValueChange = { longitude = it.toDouble() },
            label = { Text(text = "Longitude") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(100.dp)
        )
        Button(
            onClick = {

            }, shape = RectangleShape, modifier = Modifier.fillMaxHeight()
        ) {
            Text(text = "Set location")
        }
    }
}