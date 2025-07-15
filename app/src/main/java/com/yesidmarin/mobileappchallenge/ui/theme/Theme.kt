package com.yesidmarin.mobileappchallenge.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = Blue,
    secondary = YellowLight,
    tertiary = YellowLight,
    background = White,
    onBackground = Black,
    surface = Yellow,
    surfaceContainer = White,
    surfaceContainerLow = White,
    surfaceContainerLowest = White,
    surfaceContainerHigh = White,
    surfaceContainerHighest = White,
    outline = Gray
)

@Composable
fun MobileAppChallengeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}