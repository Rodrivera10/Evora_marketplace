package com.uvg.evora.ui.buyer

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uvg.evora.data.model.Categorias

@Composable
fun ExploreScreen(
    estado: ExploreUiState,
    onBusquedaChange: (String) -> Unit,
    onCategoriaChange: (String?) -> Unit,
    onReintentar: () -> Unit,
    onVolver: () -> Unit,
    onAbrirNegocio: (String) -> Unit
) {
    val error = estado.error

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onVolver) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                Text(
                    text = "Explorar negocios",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            OutlinedTextField(
                value = estado.busqueda,
                onValueChange = onBusquedaChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Buscar negocios")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = estado.categoria == null,
                    onClick = { onCategoriaChange(null) },
                    label = { Text("Todas") }
                )

                Categorias.todas.forEach { categoria ->
                    FilterChip(
                        selected = estado.categoria == categoria,
                        onClick = {
                            onCategoriaChange(categoria)
                        },
                        label = { Text(categoria) }
                    )
                }
            }
        }

        when {
            estado.cargando -> {
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

            error != null -> {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )

                        OutlinedButton(onClick = onReintentar) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            estado.negociosVisibles.isEmpty() -> {
                item {
                    Text(
                        text = if (estado.negocios.isEmpty()) {
                            "Todavía no hay negocios disponibles."
                        } else {
                            "No encontramos negocios con esos filtros."
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                items(
                    items = estado.negociosVisibles,
                    key = { negocio -> negocio.id }
                ) { negocio ->
                    OutlinedCard(
                        onClick = {
                            onAbrirNegocio(negocio.id)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                modifier = Modifier.size(44.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = negocio.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = negocio.categoria,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                if (negocio.descripcion.isNotBlank()) {
                                    Text(
                                        text = negocio.descripcion,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Text(
                                    text = if (negocio.abierta) {
                                        "Abierto"
                                    } else {
                                        "Cerrado"
                                    },
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                if (negocio.tiempoEstimado.isNotBlank()) {
                                    Text(
                                        text = negocio.tiempoEstimado,
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