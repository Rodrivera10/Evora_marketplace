package com.uvg.evora.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.uvg.evora.data.repository.AuthRepository

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    var correo by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    var cargando by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    var sesionIniciada by mutableStateOf(false)
        private set

    fun actualizarCorreo(valor: String) {
        correo = valor
        error = null
    }

    fun actualizarContrasena(valor: String) {
        contrasena = valor
        error = null
    }

    fun iniciarSesion() {
        if (cargando || sesionIniciada) return

        if (correo.isBlank() || contrasena.isEmpty()) {
            error = "Ingresa tu correo y contraseña."
            return
        }

        cargando = true
        error = null

        repository.iniciarSesion(
            correo = correo,
            contrasena = contrasena,
            onSuccess = {
                cargando = false
                contrasena = ""
                sesionIniciada = true
            },
            onError = { mensaje ->
                cargando = false
                error = mensaje
            }
        )
    }
    fun recuperarContrasena() {
        if (cargando || sesionIniciada) return

        if (correo.isBlank()) {
            error = "Ingresa tu correo para recuperar la contraseña."
            mensaje = null
            return
        }

        cargando = true
        error = null
        mensaje = null

        repository.recuperarContrasena(
            correo = correo,
            onSuccess = {
                cargando = false
                mensaje =
                    "Si el correo está registrado, recibirás un enlace " +
                            "para cambiar tu contraseña. Revisa también spam."
            },
            onError = { detalle ->
                cargando = false
                error = detalle
            }
        )
    }
}