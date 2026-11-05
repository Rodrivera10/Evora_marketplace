package com.uvg.evora.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvg.evora.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinalizar: () -> Unit
) {
    val azulMarino = Color(0xFF0D1930)
    val finalizarActual by rememberUpdatedState(onFinalizar)
    var puntoActivo by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        delay(2200)
        finalizarActual()
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(350)
            puntoActivo = (puntoActivo + 1) % 3
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(azulMarino)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        R.drawable.evora_logo_blanco
                    ),
                    contentDescription = "Evora Marketplace",
                    modifier = Modifier
                        .widthIn(max = 260.dp)
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Conecta. Vende. Crece.",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 64.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { indice ->
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(
                                color = Color.White.copy(
                                    alpha = if (indice == puntoActivo) {
                                        1f
                                    } else {
                                        0.3f
                                    }
                                ),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}