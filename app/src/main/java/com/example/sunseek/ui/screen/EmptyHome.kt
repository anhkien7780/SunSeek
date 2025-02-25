package com.example.sunseek.ui.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sunseek.R
import com.example.sunseek.ui.theme.SunSeekTheme


@Composable
fun EmptyHomeBody(
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

@Preview
@Composable
fun EmptyHomeScreenPreview() {
    SunSeekTheme {
        EmptyHomeBody(
            onFavoriteLocationsButtonClick = {},
        )
    }
}


