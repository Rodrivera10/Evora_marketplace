package com.uvg.evora.ui.buyer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.uvg.evora.data.model.Store
import com.uvg.evora.data.repository.MarketplaceRepository

data class ExploreUiState(
    val negocios: List<Store> = emptyList(),
    val busqueda: String = "",
    val categoria: String? = null,
    val cargando: Boolean = false,
    val error: String? = null
) {
    val negociosVisibles: List<Store>
        get() {
            val consulta = busqueda.trim()

            return negocios.filter { negocio ->
                val coincideCategoria = categoria == null ||
                        negocio.categoria == categoria

                val coincideBusqueda = consulta.isBlank() ||
                        negocio.nombre.contains(
                            consulta,
                            ignoreCase = true
                        ) ||
                        negocio.descripcion.contains(
                            consulta,
                            ignoreCase = true
                        )

                coincideCategoria && coincideBusqueda
            }
        }
}

class ExploreViewModel : ViewModel() {

    private val repository = MarketplaceRepository()

    var estado by mutableStateOf(ExploreUiState())
        private set

    init {
        cargarNegocios()
    }

    fun actualizarBusqueda(valor: String) {
        estado = estado.copy(busqueda = valor)
    }

    fun seleccionarCategoria(valor: String?) {
        estado = estado.copy(categoria = valor)
    }

    fun cargarNegocios() {
        if (estado.cargando) return

        estado = estado.copy(
            cargando = true,
            error = null
        )

        repository.obtenerNegocios(
            onSuccess = { negocios ->
                estado = estado.copy(
                    negocios = negocios,
                    cargando = false
                )
            },
            onError = { detalle ->
                estado = estado.copy(
                    cargando = false,
                    error = detalle
                )
            }
        )
    }
}