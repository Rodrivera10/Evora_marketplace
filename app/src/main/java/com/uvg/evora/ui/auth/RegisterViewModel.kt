package com.uvg.evora.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.uvg.evora.data.model.User
import com.uvg.evora.data.repository.AuthRepository

data class RegisterUiState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmacion: String = "",
    val aceptaTerminos: Boolean = false,
    val cargando: Boolean = false,
    val cuentaCreada: Boolean = false,
    val registroCompleto: Boolean = false,
    val error: String? = null
)

class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository()
    private var perfilPendiente: User? = null

    var estado by mutableStateOf(RegisterUiState())
        private set

    fun actualizarNombre(valor: String) {
        if (estado.cargando || estado.cuentaCreada) return
        estado = estado.copy(nombre = valor, error = null)
    }

    fun actualizarCorreo(valor: String) {
        if (estado.cargando || estado.cuentaCreada) return
        estado = estado.copy(correo = valor, error = null)
    }

    fun actualizarContrasena(valor: String) {
        if (estado.cargando || estado.cuentaCreada) return
        estado = estado.copy(contrasena = valor, error = null)
    }

    fun actualizarConfirmacion(valor: String) {
        if (estado.cargando || estado.cuentaCreada) return
        estado = estado.copy(confirmacion = valor, error = null)
    }

    fun actualizarTerminos(valor: Boolean) {
        if (estado.cargando || estado.cuentaCreada) return
        estado = estado.copy(aceptaTerminos = valor, error = null)
    }

    fun registrar() {
        if (estado.cargando || estado.registroCompleto) return

        val pendiente = perfilPendiente
        if (pendiente != null) {
            estado = estado.copy(cargando = true, error = null)
            completarPerfil(pendiente)
            return
        }

        val mensaje = when {
            estado.nombre.isBlank() ->
                "Ingresa tu nombre."

            estado.correo.isBlank() ->
                "Ingresa tu correo."

            estado.contrasena.length < 8 ->
                "La contraseña debe tener al menos 8 caracteres."

            estado.contrasena != estado.confirmacion ->
                "Las contraseñas no coinciden."

            !estado.aceptaTerminos ->
                "Debes aceptar los términos y la política de privacidad."

            else -> null
        }

        if (mensaje != null) {
            mostrarError(mensaje)
            return
        }

        val nombre = estado.nombre.trim()
        val correo = estado.correo.trim()
        val contrasena = estado.contrasena

        estado = estado.copy(cargando = true, error = null)

        repository.crearCuenta(
            correo = correo,
            contrasena = contrasena,
            onSuccess = { uid ->
                val usuario = User(
                    uid = uid,
                    nombre = nombre,
                    correo = correo,
                    fechaCreacion = System.currentTimeMillis()
                )

                perfilPendiente = usuario

                estado = estado.copy(
                    cuentaCreada = true,
                    contrasena = "",
                    confirmacion = ""
                )

                completarPerfil(usuario)
            },
            onError = { mensajeError ->
                mostrarError(mensajeError)
            }
        )
    }

    private fun completarPerfil(usuario: User) {
        repository.obtenerPerfilActual(
            onSuccess = { existente ->
                if (existente != null) {
                    finalizarRegistro()
                } else {
                    repository.guardarPerfil(
                        usuario = usuario,
                        onSuccess = { finalizarRegistro() },
                        onError = { mensaje -> mostrarError(mensaje) }
                    )
                }
            },
            onError = { mensaje -> mostrarError(mensaje) }
        )
    }

    private fun finalizarRegistro() {
        perfilPendiente = null
        estado = estado.copy(
            cargando = false,
            registroCompleto = true,
            error = null
        )
    }

    private fun mostrarError(mensaje: String) {
        estado = estado.copy(
            cargando = false,
            error = mensaje
        )
    }
}