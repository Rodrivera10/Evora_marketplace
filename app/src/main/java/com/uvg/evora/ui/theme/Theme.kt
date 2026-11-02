package com.uvg.evora.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EvoraColorScheme = lightColorScheme(
    primary = EvoraNavy,
    onPrimary = EvoraSurface,
    secondary = EvoraGold,
    onSecondary = EvoraSurface,
    background = EvoraBackground,
    onBackground = EvoraNavy,
    surface = EvoraSurface,
    onSurface = EvoraNavy,
    onSurfaceVariant = EvoraTextSecondary,
    outline = EvoraOutline,
    error = EvoraError
)

@Composable
fun EvoraMarketplaceTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EvoraColorScheme,
        typography = Typography,
        content = content
    )
}