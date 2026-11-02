package com.uvg.evora.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun EvoraNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        val pantallasTemporales = listOf(
            Routes.SPLASH, Routes.LOGIN, Routes.REGISTER, Routes.ROLE, Routes.ONBOARDING,
            Routes.BUYER_HOME, Routes.EXPLORE, Routes.STORE, Routes.CART, Routes.CHECKOUT,
            Routes.SELLER_HOME, Routes.INVENTORY, Routes.NEW_SALE, Routes.CHARGE,
            Routes.REPORTS, Routes.SCANNER, Routes.PROFILE
        )
        pantallasTemporales.forEach { ruta ->
            composable(ruta) { PantallaTemporal(ruta) }
        }
    }
}

@Composable
private fun PantallaTemporal(nombre: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = nombre)
    }
}