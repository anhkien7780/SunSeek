package com.example.sunseek.ui.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sunseek.ui.theme.SunSeekTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun ImagePickerPreview() {
    SunSeekTheme {
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var bitmap by remember { mutableStateOf<Bitmap?>(null) }
        val pickImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageUri = uri
            bitmap = null
        }
        val takePictureLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bmp ->
            bitmap = bmp
            imageUri = null
        }
        var openDialogState by remember {
            mutableStateOf(false)
        }
        Scaffold {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImagePicker(
                    imageUri = imageUri,
                    bitmap = bitmap,
                ) {
                    openDialogState = it
                }
            }
            when {
                openDialogState -> {
                    AlertDialog(
                        containerColor = MaterialTheme.colorScheme.surface,
                        onDismissRequest = {
                            openDialogState = false
                        },
                        title = {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                TextButton(
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    onClick = {
                                        pickImageLauncher.launch("image/*")
                                        openDialogState = false
                                    },
                                ) {
                                    Text(
                                        text = "Chọn ảnh từ thư viện",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                                TextButton(
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    onClick = {
                                        takePictureLauncher.launch(null)
                                        openDialogState = false
                                    },
                                ) {
                                    Text(
                                        text = "Chụp ảnh",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        },
                        confirmButton = {},
                    )
                }
            }
        }
    }
}