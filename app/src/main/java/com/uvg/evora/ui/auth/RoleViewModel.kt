package com.uvg.evora.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.uvg.evora.data.repository.AuthRepository

class RoleViewModel : ViewModel() {

    private val repository = AuthRepository()

    var cargando by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var rolGuardado by mutableStateOf(false)
        private set

    fun guardarRol(rol: String) {
        if (cargando || rolGuardado) return

        cargando = true
        error = null

        repository.actualizarRol(
            rol = rol,
            onSuccess = {
                cargando = false
                rolGuardado = true
            },
            onError = { detalle ->
                cargando = false
                error = detalle
            }
        )
    }
}