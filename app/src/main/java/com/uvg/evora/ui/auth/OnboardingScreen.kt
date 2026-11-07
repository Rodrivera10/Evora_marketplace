package com.uvg.evora.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uvg.evora.R

@Composable
fun OnboardingScreen(
    onFinalizar: () -> Unit
) {
    var pagina by rememberSaveable {
        mutableStateOf(0)
    }

    var finalizando by rememberSaveable {
        mutableStateOf(false)
    }

    val titulos = listOf(
        "Todo lo que necesitas en un solo lugar",
        "Tus productos, bajo control",
        "Conecta. Vende. Crece."
    )

    val descripciones = listOf(
        "Gestiona tu negocio y descubre emprendimientos " +
                "de tu comunidad universitaria.",
        "Organiza tu inventario, registra ventas y " +
                "consulta los productos con stock bajo.",
        "Encuentra productos y utiliza códigos QR " +
                "para identificarlos fácilmente."
    )

    val ilustraciones = listOf(
        R.drawable.onboarding_comunidad,
        R.drawable.onboarding_inventario,
        R.drawable.onboarding_qr
    )

    val azul = Color(0xFF0D2142)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.evora_logo),
            contentDescription = "Evora Marketplace",
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(ilustraciones[pagina]),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = titulos[pagina],
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = azul
        )

        Text(
            text = descripciones[pagina],
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(titulos.size) { indice ->
                Box(
                    modifier = Modifier
                        .size(
                            if (pagina == indice) 10.dp else 8.dp
                        )
                        .background(
                            color = if (pagina == indice) {
                                azul
                            } else {
                                azul.copy(alpha = 0.2f)
                            },
                            shape = CircleShape
                        )
                )
            }
        }

        Button(
            onClick = {
                if (pagina < titulos.lastIndex) {
                    pagina++
                } else {
                    finalizando = true
                    onFinalizar()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp),
            enabled = !finalizando,
            colors = ButtonDefaults.buttonColors(
                containerColor = azul,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (pagina == titulos.lastIndex) {
                    "Comenzar"
                } else {
                    "Siguiente"
                }
            )
        }

        if (pagina > 0) {
            TextButton(
                onClick = { pagina-- },
                enabled = !finalizando
            ) {
                Text(
                    text = "Anterior",
                    color = azul
                )
            }
        }
    }
}