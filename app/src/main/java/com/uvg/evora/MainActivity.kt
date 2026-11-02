package com.uvg.evora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uvg.evora.ui.navigation.EvoraNavHost
import com.uvg.evora.ui.theme.EvoraMarketplaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EvoraMarketplaceTheme {
                EvoraNavHost()
            }
        }
    }
}