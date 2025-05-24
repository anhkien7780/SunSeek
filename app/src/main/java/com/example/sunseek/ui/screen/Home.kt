package com.example.sunseek.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.sunseek.R
import com.example.sunseek.model.Description
import com.example.sunseek.model.WeatherLevel
import com.example.sunseek.model.listDescription
import com.example.sunseek.model.toListWeatherInfo
import com.example.sunseek.viewmodel.AccountViewModel
import com.example.sunseek.model.LoadingUIState
import com.example.sunseek.viewmodel.OpenWeatherViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.text.Typography.bullet

@Serializable
object Home

val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))
val goodWeatherContainerColor = Color(123, 206, 105)
val goodWeatherColor = Color(6, 235, 98)
val badWeatherContainerColor = Color(243, 124, 111)
val badWeatherColor = Color(245, 25, 1)
val normalWeatherContainerColor = Color(243, 186, 111)
val normalWeatherColor = Color(185, 120, 0)

@Composable
fun HomeScreen(
    accountViewModel: AccountViewModel,
    weatherViewModel: OpenWeatherViewModel,
    onSettingButtonClick: () -> Unit,
    onFavoriteLocationsButtonClick: () -> Unit,
    onMapButtonClick: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val context = LocalContext.current
    val notes = Description.getInformationTips(context)
    val loadingUIState by accountViewModel.loadingUIState.collectAsState()
    val username by accountViewModel.email
    var openLogoutDialog by remember { mutableStateOf(false) }
    var openGetImageDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val weatherReport = remember { weatherViewModel.weatherReport.value }
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

    Scaffold(containerColor = MaterialTheme.colorScheme.background, bottomBar = {
        BottomAppBar(
            modifier = Modifier.fillMaxWidth()
        ) {
            MyCustomButton(
                modifier = Modifier
                    .weight(1f),
                painterResourceID = R.drawable.settings,
                contentDescription = stringResource(R.string.setting),
                buttonText = stringResource(R.string.setting)
            ) { onSettingButtonClick() }

            MyCustomButton(
                modifier = Modifier
                    .weight(1f),
                painterResourceID = R.drawable.favorite_border,
                contentDescription = stringResource(R.string.favorite_locations),
                buttonText = stringResource(R.string.my_favorite_locations)
            ) { onFavoriteLocationsButtonClick() }

            MyCustomButton(
                modifier = Modifier
                    .weight(1f),
                painterResourceID = R.drawable.map,
                contentDescription = stringResource(R.string.map),
                buttonText = stringResource(R.string.select_location)
            ) { onMapButtonClick() }

        }
    }) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Account Button
            item {
                AvatarButton(modifier = Modifier.fillMaxWidth(), username = username) {
                    openLogoutDialog = !openLogoutDialog
                }
            }
            when (weatherReport){
                null -> {
                    item {
                        EmptyWeatherReportBody {
                            onFavoriteLocationsButtonClick()
                        }
                    }
                }
                else -> {
                   item {
                       // Image Picker
                       ImagePicker(
                           imageUri = imageUri,
                           bitmap = bitmap,
                       ) {
                           openGetImageDialog = it
                       }
                       // Detail Address
                       Text(
                           weatherReport.detailAddress,
                           modifier = Modifier.padding(top = 30.dp),
                           textAlign = TextAlign.Center,
                           style = MaterialTheme.typography.displaySmall
                       )
                       // Street Address
                       Text(
                           "Địa chỉ: ${weatherReport.streetAddress}",
                           style = MaterialTheme.typography.labelSmall,
                           fontStyle = FontStyle.Italic,
                           modifier = Modifier.padding(bottom = 10.dp)
                       )
                   }
                   // Information bar
                   items(weatherReport.toListWeatherInfo()) { weatherInfo ->
                       InformationBar(
                           modifier = Modifier.padding(bottom = 5.dp),
                           informationName = stringResource(weatherInfo.informationName),
                           weatherInfo = weatherInfo.weatherInfo,
                           weatherLevel = weatherInfo.weatherLevel,
                       )
                   }
               }
            }

            // Information description bar
            item {
                Text(
                    text = stringResource(R.string.information),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(listDescription) { description ->
                ExpandableInformationBar(
                    informationName = stringResource(description.informationName),
                    information = stringResource(description.information)
                )
                Spacer(Modifier.padding(vertical = 5.dp))
            }
            item {
                Text(
                    text = stringResource(R.string.note),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = buildAnnotatedString {
                        notes.forEach {
                            withStyle(style = paragraphStyle) {
                                append(bullet)
                                append("\t\t")
                                append(it)
                            }
                        }
                    },
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        // Dialog
        when {
            openLogoutDialog -> {
                AlertDialog(
                    onDismissRequest = { openLogoutDialog = false },
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    openLogoutDialog = false
                                    scope.launch {
                                        try {
                                            val isLogoutSuccess =
                                                accountViewModel.logout(context = context)
                                            if (isLogoutSuccess) {
                                                onLogoutSuccess()
                                            }
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.logout),
                                    contentDescription = stringResource(
                                        R.string.logout_button
                                    )
                                )
                                Spacer(modifier = Modifier.size(5.dp))
                                Text(
                                    stringResource(R.string.logout),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    },
                    title = {
                        Text(text = username, style = MaterialTheme.typography.headlineSmall)
                    }
                )
            }

            openGetImageDialog -> {
                AlertDialog(
                    containerColor = MaterialTheme.colorScheme.surface,
                    onDismissRequest = {
                        openGetImageDialog = false
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
                                    openGetImageDialog = false
                                },
                            ) {
                                Text(
                                    text = stringResource(R.string.get_photo_from_collection),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                            TextButton(
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                onClick = {
                                    takePictureLauncher.launch(null)
                                    openGetImageDialog = false
                                },
                            ) {
                                Text(
                                    text = stringResource(R.string.take_photo),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    },
                    confirmButton = {},
                )
            }
        }
        when{
            loadingUIState == LoadingUIState.Loading ->{
                FullScreenLoading()
            }
        }
    }
}

//----------------------------------------------------------------------------------------------------


@Composable
fun MyCustomButton(
    modifier: Modifier = Modifier,
    painterResourceID: Int,
    contentDescription: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(painterResourceID),
            contentDescription = contentDescription
        )
        Text(
            text = buttonText, style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun EmptyWeatherReportBody(
    onFavoriteLocationsButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier.height(200.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_home_description),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.padding(vertical = 10.dp))
        Button(
            onClick = {
                onFavoriteLocationsButtonClick()
            },
            modifier = Modifier
                .padding(10.dp)
                .height(40.dp)
                .clip(shape = RoundedCornerShape(10.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Text(
                text = stringResource(R.string.navigate_to_favorite_screen),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

}

@Composable
fun InformationBar(
    modifier: Modifier = Modifier,
    informationName: String,
    weatherInfo: String,
    weatherLevel: WeatherLevel
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = when (weatherLevel) {
                    WeatherLevel.Bad -> badWeatherContainerColor
                    WeatherLevel.Good -> goodWeatherContainerColor
                    WeatherLevel.Normal -> normalWeatherContainerColor
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = informationName,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(start = 20.dp)
                .weight(1f)
        )
        Box(
            modifier = Modifier
                .padding(end = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    color = when (weatherLevel) {
                        WeatherLevel.Bad -> badWeatherColor
                        WeatherLevel.Good -> goodWeatherColor
                        WeatherLevel.Normal -> normalWeatherColor
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = weatherInfo,
                modifier = Modifier.padding(horizontal = 10.dp),
                color = if (weatherLevel is WeatherLevel.Good) Color.Black else Color.White
            )
        }
    }
}


@Composable
fun ExpandableInformationBar(
    informationName: String,
    information: String,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = if (!expanded) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                    expanded = !expanded
                }
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                informationName,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.weight(1f))
            Icon(
                painter = if (!expanded) painterResource(R.drawable.arrow_drop_down) else painterResource(
                    R.drawable.arrow_drop_up
                ),
                contentDescription = "Expand Button",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 5.dp)
            )
        }
        if (expanded) {
            Text(
                modifier = Modifier.padding(all = 5.dp),
                text = information,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun AvatarButton(modifier: Modifier = Modifier, username: String, onClick: (Boolean) -> Unit) {
    var state by remember { mutableStateOf(false) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 10.dp)
                .size(44.dp)
                .clip(shape = CircleShape)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .clickable(onClick = {
                    state = !state
                    onClick(!state)
                }),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(R.string.account_button),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            "Xin chào $username",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    imageUri: Uri? = null,
    bitmap: Bitmap? = null,
    onClickStateChange: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .size(352.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when {
            imageUri != null -> {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = stringResource(R.string.location_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            bitmap != null -> {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = stringResource(R.string.location_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> Image(
                painter = painterResource(R.drawable.hoanghon),
                contentDescription = stringResource(R.string.location_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        // Change image button
        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .background(color = MaterialTheme.colorScheme.tertiaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .size(height = 60.dp, width = 273.dp)
                    .clickable {
                        onClickStateChange(true)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.image),
                    contentDescription = "Change Image Button",
                    modifier = Modifier.padding(end = 21.dp)
                )
                Text(
                    stringResource(R.string.change_image),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        accountViewModel = viewModel(),
        weatherViewModel = viewModel(),
        onSettingButtonClick = {},
        onFavoriteLocationsButtonClick = {},
        onMapButtonClick = {},
        onLogoutSuccess = {}
    )
}


