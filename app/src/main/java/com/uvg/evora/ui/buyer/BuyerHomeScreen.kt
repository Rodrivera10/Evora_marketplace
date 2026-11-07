package com.uvg.evora.ui.buyer

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uvg.evora.R
import com.uvg.evora.data.model.Categorias
import java.util.Locale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

@Composable
fun BuyerHomeScreen(
    estado: BuyerHomeUiState,
    onReintentarPerfil: () -> Unit,
    onReintentarNegocios: () -> Unit,
    onReintentarProductos: () -> Unit,
    onExplorar: () -> Unit,
    onCarrito: () -> Unit,
    onPerfil: () -> Unit,
    onAbrirNegocio: (String) -> Unit
) {
    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    var categoriaSeleccionada by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val consulta = busqueda.trim()

    val negociosVisibles = estado.negocios.filter { negocio ->
        val coincideCategoria = categoriaSeleccionada == null ||
                negocio.categoria == categoriaSeleccionada

        val coincideBusqueda = consulta.isBlank() ||
                negocio.nombre.contains(consulta, ignoreCase = true) ||
                negocio.descripcion.contains(consulta, ignoreCase = true)

        coincideCategoria && coincideBusqueda
    }

    val negociosPorId = estado.negocios.associateBy { it.id }

    val productosVisibles = estado.productos.filter { producto ->
        val negocio = negociosPorId[producto.storeId]

        val coincideCategoria = categoriaSeleccionada == null ||
                negocio?.categoria == categoriaSeleccionada

        val coincideBusqueda = consulta.isBlank() ||
                producto.nombre.contains(consulta, ignoreCase = true) ||
                producto.descripcion.contains(consulta, ignoreCase = true)

        coincideCategoria && coincideBusqueda
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Home, contentDescription = null)
                    },
                    label = { Text("Inicio") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onExplorar,
                    icon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    label = { Text("Explorar") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onCarrito,
                    icon = {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null
                        )
                    },
                    label = { Text("Carrito") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onPerfil,
                    icon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.evora_logo),
                contentDescription = "Evora Marketplace",
                modifier = Modifier
                    .width(190.dp)
                    .height(64.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )

            Text(
                text = if (estado.nombre.isBlank()) {
                    "¡Hola!"
                } else {
                    "¡Hola, ${estado.nombre.trim().substringBefore(" ")}!"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Descubre lo que ofrece tu comunidad.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (estado.errorPerfil != null) {
                ErrorInicio(
                    mensaje = estado.errorPerfil,
                    onReintentar = onReintentarPerfil
                )
            }

            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Buscar en el catálogo cargado")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true
            )

            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = categoriaSeleccionada == null,
                    onClick = { categoriaSeleccionada = null },
                    label = { Text("Todas") }
                )

                Categorias.todas.forEach { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = {
                            categoriaSeleccionada = categoria
                        },
                        label = { Text(categoria) }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = "Negocios",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = onExplorar) {
                    Text("Explorar")
                }
            }

            when {
                estado.cargandoNegocios -> {
                    CircularProgressIndicator()
                }

                estado.errorNegocios != null -> {
                    ErrorInicio(
                        mensaje = estado.errorNegocios,
                        onReintentar = onReintentarNegocios
                    )
                }

                negociosVisibles.isEmpty() -> {
                    Text(
                        text = if (estado.negocios.isEmpty()) {
                            "Todavía no hay negocios disponibles."
                        } else {
                            "No hay negocios que coincidan con los filtros."
                        }
                    )
                }

                else -> {
                    negociosVisibles.forEach { negocio ->
                        OutlinedCard(
                            onClick = {
                                onAbrirNegocio(negocio.id)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = negocio.nombre,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(text = negocio.categoria)

                                    Text(
                                        text = if (negocio.abierta) {
                                            "Abierto"
                                        } else {
                                            "Cerrado"
                                        },
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    if (negocio.tiempoEstimado.isNotBlank()) {
                                        Text(text = negocio.tiempoEstimado)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Text(
                text = "Productos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            when {
                estado.cargandoProductos -> {
                    CircularProgressIndicator()
                }

                estado.errorProductos != null -> {
                    ErrorInicio(
                        mensaje = estado.errorProductos,
                        onReintentar = onReintentarProductos
                    )
                }

                productosVisibles.isEmpty() -> {
                    Text(
                        text = if (estado.productos.isEmpty()) {
                            "Todavía no hay productos disponibles."
                        } else {
                            "No hay productos que coincidan con los filtros."
                        }
                    )
                }

                else -> {
                    productosVisibles.forEach { producto ->
                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
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

                                if (imagenLocal != null) {
                                    Image(
                                        painter = painterResource(imagenLocal),
                                        contentDescription = producto.nombre,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                Text(
                                    text = producto.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                if (producto.descripcion.isNotBlank()) {
                                    Text(text = producto.descripcion)
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
                                    }
                                )

                                if (negociosPorId.containsKey(producto.storeId)) {
                                    TextButton(
                                        onClick = {
                                            onAbrirNegocio(producto.storeId)
                                        }
                                    ) {
                                        Text("Ver negocio")
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
private fun ErrorInicio(
    mensaje: String,
    onReintentar: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
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