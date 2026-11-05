package com.uvg.evora.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uvg.evora.R
import com.uvg.evora.data.model.Roles

@Composable
fun RoleScreen(
    onContinuar: (String) -> Unit,
    cargando: Boolean = false,
    error: String? = null
) {
    var rolSeleccionado by rememberSaveable {
        mutableStateOf(Roles.COMPRADOR)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
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

        Text(
            text = "¿Cómo vas a usar Evora?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Selecciona el tipo de perfil que mejor se adapte a ti.",
            style = MaterialTheme.typography.bodyLarge
        )

        OpcionPerfil(
            titulo = "Soy vendedor",
            descripcion = "Gestiona tu negocio, inventario y ventas.",
            icono = Icons.Default.Storefront,
            seleccionado = rolSeleccionado == Roles.VENDEDOR,
            habilitado = !cargando,
            onClick = { rolSeleccionado = Roles.VENDEDOR }
        )

        OpcionPerfil(
            titulo = "Soy comprador",
            descripcion = "Descubre productos y apoya a emprendedores locales.",
            icono = Icons.Default.ShoppingBag,
            seleccionado = rolSeleccionado == Roles.COMPRADOR,
            habilitado = !cargando,
            onClick = { rolSeleccionado = Roles.COMPRADOR }
        )

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onContinuar(rolSeleccionado) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp),
            enabled = !cargando
        ) {
            Text(
                text = if (cargando) {
                    "Guardando..."
                } else {
                    "Continuar"
                }
            )
        }
    }
}

@Composable
private fun OpcionPerfil(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    seleccionado: Boolean,
    habilitado: Boolean,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = habilitado,
                onClick = onClick
            ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (seleccionado) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (seleccionado) 2.dp else 1.dp,
            color = if (seleccionado) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (seleccionado) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Seleccionado",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}