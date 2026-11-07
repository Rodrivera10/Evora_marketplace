package com.uvg.evora.ui.buyer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.uvg.evora.data.model.Product
import com.uvg.evora.data.model.Store
import com.uvg.evora.data.repository.MarketplaceRepository

data class StoreUiState(
    val negocio: Store? = null,
    val productos: List<Product> = emptyList(),
    val cargandoNegocio: Boolean = false,
    val cargandoProductos: Boolean = false,
    val errorNegocio: String? = null,
    val errorProductos: String? = null
)

class StoreViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = MarketplaceRepository()

    private val storeId: String =
        savedStateHandle.get<String>("storeId").orEmpty()

    var estado by mutableStateOf(StoreUiState())
        private set

    init {
        cargarNegocio()
        cargarProductos()
    }

    fun cargarNegocio() {
        if (estado.cargandoNegocio) return

        estado = estado.copy(
            cargandoNegocio = true,
            errorNegocio = null
        )

        repository.obtenerNegocio(
            storeId = storeId,
            onSuccess = { negocio ->
                estado = estado.copy(
                    negocio = negocio,
                    cargandoNegocio = false,
                    errorNegocio = if (negocio == null) {
                        "Este negocio ya no está disponible."
                    } else {
                        null
                    }
                )
            },
            onError = { detalle ->
                estado = estado.copy(
                    cargandoNegocio = false,
                    errorNegocio = detalle
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

        repository.obtenerProductosDelNegocio(
            storeId = storeId,
            onSuccess = { productos ->
                estado = estado.copy(
                    productos = productos,
                    cargandoProductos = false
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