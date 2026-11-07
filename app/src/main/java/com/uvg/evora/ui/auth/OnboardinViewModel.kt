package com.uvg.evora.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.uvg.evora.data.model.Roles
import com.uvg.evora.data.repository.AuthRepository

class OnboardingViewModel : ViewModel() {

    private val repository = AuthRepository()

    var rol by mutableStateOf<String?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        consultarPerfil()
    }

    fun consultarPerfil() {
        if (cargando) return

        cargando = true
        error = null

        repository.obtenerPerfilActual(
            onSuccess = { usuario ->
                cargando = false

                when (usuario?.rolActivo) {
                    Roles.COMPRADOR, Roles.VENDEDOR -> {
                        rol = usuario.rolActivo
                    }

                    else -> {
                        error = "No se encontró un perfil válido."
                    }
                }
            },
            onError = { detalle ->
                cargando = false
                error = detalle
            }
        )
    }
}