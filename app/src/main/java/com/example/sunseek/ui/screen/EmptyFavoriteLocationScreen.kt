package com.example.sunseek.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
fun EmptyFavoriteLocationScreen(
    onBack: () -> Unit,
    onNavigateToSelectLocationScreen: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
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
                title = {
                    Text(
                        text = stringResource(R.string.my_favorite_locations),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.empty_list),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.padding(vertical = 10.dp))
            Button(
                onClick = {
                    onNavigateToSelectLocationScreen()
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
                    text = stringResource(R.string.navigate_to_select_location_screen),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun EmptyFavoriteLocationScreenPreview() {
    SunSeekTheme {
        EmptyFavoriteLocationScreen(
            onBack = {}, onNavigateToSelectLocationScreen = {},
        )
    }
}