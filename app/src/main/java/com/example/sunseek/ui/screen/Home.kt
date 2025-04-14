package com.example.sunseek.ui.screen

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunseek.R
import com.example.sunseek.model.Description
import com.example.sunseek.model.Information
import com.example.sunseek.model.WeatherReport
import com.example.sunseek.ui.theme.SunSeekTheme
import com.example.sunseek.viewmodel.AccountViewModel
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
    val username by accountViewModel.email
    var openDialogState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val weatherReport = remember { weatherViewModel.weatherReport.value }

    Scaffold(containerColor = MaterialTheme.colorScheme.background, bottomBar = {
        BottomAppBar(
            modifier = Modifier.fillMaxWidth()
        ) {
            MyCustomButton(
                modifier = Modifier
                    .weight(1f)
                    .clickable { },
                painterResourceID = R.drawable.settings,
                contentDescription = stringResource(R.string.setting),
                buttonText = stringResource(R.string.setting)
            ) { onSettingButtonClick() }

            MyCustomButton(
                modifier = Modifier
                    .weight(1f)
                    .clickable { },
                painterResourceID = R.drawable.favorite_border,
                contentDescription = stringResource(R.string.favorite_locations),
                buttonText = stringResource(R.string.my_favorite_locations)
            ) { onFavoriteLocationsButtonClick() }

            MyCustomButton(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onMapButtonClick()
                    },
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
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(44.dp)
                            .clip(shape = CircleShape)
                            .background(color = MaterialTheme.colorScheme.primaryContainer)
                            .clickable {
                                openDialogState = !openDialogState
                            },
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
            when {
                weatherReport != null -> {
                    // Image, Location, and Address
                    item {
                        // Image
                        Box(
                            modifier = Modifier
                                .size(352.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.hoanghon),
                                contentDescription = stringResource(R.string.location_image),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
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
                                        .clickable {},
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
                        // Address information
                        Text(
                            weatherReport.detailAddress,
                            modifier = Modifier.padding(top = 30.dp),
                            style = MaterialTheme.typography.displaySmall
                        )
                        // Address
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
                else -> {
                    item {
                        EmptyWeatherReportBody {
                            onFavoriteLocationsButtonClick()
                        }
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
        // Logout Dialog
        when {
            openDialogState -> {
                AlertDialog(
                    onDismissRequest = { openDialogState = false },
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        try {
                                            val isLogoutSuccess =
                                                accountViewModel.logout()
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

@Preview(showBackground = true)
@Composable
fun InformationBarPreview() {
    SunSeekTheme {
        InformationBar(
            informationName = "Nhiệt độ",
            weatherInfo = "25℃",
            weatherLevel = WeatherLevel.Normal
        )
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

sealed class WeatherLevel {
    data object Good : WeatherLevel()
    data object Normal : WeatherLevel()
    data object Bad : WeatherLevel()
}

val notes = listOf(
    "Chọn địa điểm: Tìm nơi có tầm nhìn thoáng, không bị che khuất bởi cây cối hay tòa nhà.",
    "Theo dõi thời tiết: Kiểm tra dự báo chi tiết ít nhất vài giờ trước khi đi.",
    "Trang phục: Luôn mang thêm một lớp áo để điều chỉnh theo thời tiết.",
    "Thời gian: Đến địa điểm ngắm hoàng hôn sớm hơn ít nhất 20-30 phút để chọn vị trí và thư giãn."
)

fun WeatherReport.toListWeatherInfo(): List<Information> {
    val temperatureInfo = "${temp.toInt()}℃"
    val temperatureLevel = when {
        temp < 15 -> WeatherLevel.Bad
        temp in 15f..30f -> WeatherLevel.Good
        else -> WeatherLevel.Normal
    }
    val airConditionLevel = when (aqi) {
        in 1..2 -> WeatherLevel.Good
        3 -> WeatherLevel.Normal
        in 4..5 -> WeatherLevel.Bad
        else -> WeatherLevel.Normal
    }
    val cloudLevel = when {
        cloud < 30 -> WeatherLevel.Good
        cloud in 30..70 -> WeatherLevel.Normal
        else -> WeatherLevel.Bad
    }

    val rainLevel = if (rain == 0f) WeatherLevel.Good else WeatherLevel.Bad
    val rainInfo = if (rain == 0f) "Không mưa" else "${rain}mm"

    val humidityLevel = when {
        humidity < 30 -> WeatherLevel.Bad
        humidity in 30..70 -> WeatherLevel.Good
        else -> WeatherLevel.Normal
    }

    return listOf(
        Information(
            R.string.temperature,
            weatherInfo = temperatureInfo,
            weatherLevel = temperatureLevel
        ),
        Information(
            R.string.air_condition,
            weatherInfo = aqi.toString(),
            weatherLevel = airConditionLevel
        ),
        Information(
            R.string.cloud,
            weatherInfo = when {
                cloud < 30 -> "Ít mây"
                cloud in 30..70 -> "Trời quang mây"
                else -> "Nhiều mây"
            },
            weatherLevel = cloudLevel
        ),
        Information(
            R.string.rain,
            weatherInfo = rainInfo,
            weatherLevel = rainLevel
        ),
        Information(
            R.string.humidity,
            weatherInfo = "$humidity%",
            weatherLevel = humidityLevel
        )
    )
}

val listDescription = listOf(
    Description(
        informationName = R.string.temperature,
        information = R.string.temperature_description
    ),
    Description(
        informationName = R.string.air_condition,
        information = R.string.air_conditional_description
    ),
    Description(
        informationName = R.string.cloud,
        information = R.string.cloud_description
    ),
    Description(
        informationName = R.string.rain,
        information = R.string.rain_description
    ),
    Description(
        informationName = R.string.humidity,
        information = R.string.humidity_description
    )
)

