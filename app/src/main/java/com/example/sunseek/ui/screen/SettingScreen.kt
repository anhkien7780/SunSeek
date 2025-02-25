package com.example.sunseek.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sunseek.R
import com.example.sunseek.ui.theme.SunSeekTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    isDarkMode: Boolean
) {
    var checked by remember { mutableStateOf(isDarkMode) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = { Text(text = stringResource(R.string.setting)) },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SettingItem(
                title = stringResource(R.string.setting)
            ) {
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = !checked },
                    colors = SwitchDefaults.colors(
                        uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary
                    ),
                )
            }
            SettingItem(
                title = stringResource(R.string.application_information)
            ) { }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    SunSeekTheme {
        SettingScreen(false)
    }
}

@Composable
fun SettingItem(
    title: String,
    onClick: (() -> Unit)? = null,
    suffixIcon: @Composable () -> Unit
) {

    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .size(height = 50.dp, width = 358.dp)
            .padding(10.dp)
            .clickable {
                if (onClick != null) {
                    onClick()
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.weight(1f))
        suffixIcon()
    }
}

@Preview(showBackground = true)
@Composable
fun SettingItemPreview() {
    SunSeekTheme {
        var checked by remember { mutableStateOf(false) }
        SettingItem(title = "Chế độ tối", onClick = {}) {
            Switch(
                checked = checked,
                onCheckedChange = { checked = !checked },
                colors = SwitchDefaults.colors(
                    uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        }
    }
}