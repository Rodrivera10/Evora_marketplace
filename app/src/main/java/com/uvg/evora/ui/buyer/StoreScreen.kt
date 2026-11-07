package com.uvg.evora.ui.buyer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uvg.evora.R
import java.util.Locale

@Composable
fun StoreScreen(
    estado: StoreUiState,
    onVolver: () -> Unit,
    onReintentarNegocio: () -> Unit,
    onReintentarProductos: () -> Unit
) {
    val negocio = estado.negocio
    val errorNegocio = estado.errorNegocio
    val errorProductos = estado.errorProductos

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                Text(
                    text = "Detalle del negocio",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        when {
            estado.cargandoNegocio -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            errorNegocio != null -> {
                item {
                    ErrorDetalle(
                        mensaje = errorNegocio,
                        onReintentar = onReintentarNegocio
                    )
                }
            }

            negocio != null -> {
                item {
                    if (negocio.id == "burritos_caseros") {
                        Image(
                            painter = painterResource(
                                R.drawable.producto_burrito_clasico
                            ),
                            contentDescription = "Burritos del negocio de ejemplo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                modifier = Modifier.size(72.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = negocio.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = negocio.categoria,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = if (negocio.abierta) {
                                "Abierto"
                            } else {
                                "Cerrado"
                            },
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        if (negocio.tiempoEstimado.isNotBlank()) {
                            Text(
                                text = "Tiempo estimado: ${negocio.tiempoEstimado}"
                            )
                        }

                        if (negocio.totalResenas > 0) {
                            Text(
                                text = String.format(
                                    Locale.US,
                                    "%.1f · %d reseñas",
                                    negocio.calificacion,
                                    negocio.totalResenas
                                )
                            )
                        } else {
                            Text(
                                text = "Sin reseñas todavía",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (negocio.descripcion.isNotBlank()) {
                            Text(text = negocio.descripcion)
                        }
                    }
                }

                item {
                    HorizontalDivider()
                }

                item {
                    Text(
                        text = "Productos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                when {
                    estado.cargandoProductos -> {
                        item {
                            CircularProgressIndicator()
                        }
                    }

                    errorProductos != null -> {
                        item {
                            ErrorDetalle(
                                mensaje = errorProductos,
                                onReintentar = onReintentarProductos
                            )
                        }
                    }

                    estado.productos.isEmpty() -> {
                        item {
                            Text("Este negocio aún no tiene productos disponibles.")
                        }
                    }

                    else -> {
                        items(
                            items = estado.productos,
                            key = { producto -> producto.id }
                        ) { producto ->
                            val imagenLocal = when (producto.id) {
                                "burrito_clasico" ->
                                    R.drawable.producto_burrito_clasico

                                "burrito_mixto" ->
                                    R.drawable.producto_burrito_mixto

                                "nachos" ->
                                    R.drawable.producto_nachos

                                "agua_natural" ->
                                    R.drawable.producto_agua

                                else -> null
                            }

                            OutlinedCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    if (imagenLocal != null) {
                                        Image(
                                            painter = painterResource(imagenLocal),
                                            contentDescription = producto.nombre,
                                            modifier = Modifier
                                                .size(96.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = producto.nombre,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (producto.descripcion.isNotBlank()) {
                                            Text(
                                                text = producto.descripcion,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Text(
                                            text = String.format(
                                                Locale.US,
                                                "Q%.2f",
                                                producto.precio
                                            ),
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )

                                        Text(
                                            text = if (producto.stock > 0) {
                                                "Disponible"
                                            } else {
                                                "Agotado"
                                            },
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorDetalle(
    mensaje: String,
    onReintentar: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = mensaje,
            color = MaterialTheme.colorScheme.error
        )

        OutlinedButton(onClick = onReintentar) {
            Text("Reintentar")
        }
    }
}