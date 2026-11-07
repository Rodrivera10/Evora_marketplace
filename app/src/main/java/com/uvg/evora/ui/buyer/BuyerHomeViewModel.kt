package com.uvg.evora.ui.buyer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.uvg.evora.data.model.Product
import com.uvg.evora.data.model.Store
import com.uvg.evora.data.repository.AuthRepository
import com.uvg.evora.data.repository.MarketplaceRepository

data class BuyerHomeUiState(
    val nombre: String = "",
    val negocios: List<Store> = emptyList(),
    val productos: List<Product> = emptyList(),
    val cargandoPerfil: Boolean = false,
    val cargandoNegocios: Boolean = false,
    val cargandoProductos: Boolean = false,
    val errorPerfil: String? = null,
    val errorNegocios: String? = null,
    val errorProductos: String? = null
)

class BuyerHomeViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val marketplaceRepository = MarketplaceRepository()

    var estado by mutableStateOf(BuyerHomeUiState())
        private set

    init {
        cargarPerfil()
        cargarNegocios()
        cargarProductos()
    }

    fun cargarPerfil() {
        if (estado.cargandoPerfil) return

        estado = estado.copy(
            cargandoPerfil = true,
            errorPerfil = null
        )

        authRepository.obtenerPerfilActual(
            onSuccess = { usuario ->
                estado = estado.copy(
                    cargandoPerfil = false,
                    nombre = usuario?.nombre.orEmpty(),
                    errorPerfil = if (usuario == null) {
                        "No se encontró tu perfil."
                    } else {
                        null
                    }
                )
            },
            onError = { detalle ->
                estado = estado.copy(
                    cargandoPerfil = false,
                    errorPerfil = detalle
                )
            }
        )
    }

    fun cargarNegocios() {
        if (estado.cargandoNegocios) return

        estado = estado.copy(
            cargandoNegocios = true,
            errorNegocios = null
        )

        marketplaceRepository.obtenerNegocios(
            onSuccess = { negocios ->
                estado = estado.copy(
                    cargandoNegocios = false,
                    negocios = negocios
                )
            },
            onError = { detalle ->
                estado = estado.copy(
                    cargandoNegocios = false,
                    errorNegocios = detalle
                )
            }
        )
    }

    fun cargarProductos() {
        if (estado.cargandoProductos) return

        estado = estado.copy(
            cargandoProductos = true,
            errorProductos = null
        )

        marketplaceRepository.obtenerProductos(
            onSuccess = { productos ->
                estado = estado.copy(
                    cargandoProductos = false,
                    productos = productos
                )
            },
            onError = { detalle ->
                estado = estado.copy(
                    cargandoProductos = false,
                    errorProductos = detalle
                )
            }
        )
    }
}