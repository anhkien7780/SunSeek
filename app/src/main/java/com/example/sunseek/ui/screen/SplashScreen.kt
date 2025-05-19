package com.example.sunseek.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sunseek.ui.theme.SunSeekTheme

@Composable
fun SplashScreen() {
    val color = MaterialTheme.colorScheme.primary
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.drawBehind
            {
                drawCircle(
                    radius = 50f,
                    color = color
                )
            },
        )
    }
}

@Composable
fun SunRay(
    center: Offset,
    radius: Float,
    length: Float,
    color: Color
) {
    val density = LocalDensity.current

    val start = with(density) {  }
    val end = with(density) { Offset(length.dp.toPx(), length.dp.toPx()) }
    val infiniteTransition = rememberInfiniteTransition()
    val angle = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 8000,
                easing = LinearEasing,
            )
        )
    )
    Box(modifier = Modifier
        .fillMaxSize()
        .drawBehind {
            rotate(degrees = angle.value) {

            }
        })
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SunSeekTheme {
        SplashScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SunRayPreview() {
    SunSeekTheme {
        SunRay(
            center = Offset(100f, 100f),
            radius = 50f,
            length = 50f,
            color = Color.Red
        )
    }
}