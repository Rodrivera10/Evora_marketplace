package com.uvg.evora.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.uvg.evora.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Source

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    fun iniciarSesion(
        correo: String,
        contrasena: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(
            correo.trim(),
            contrasena
        )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(
                    "No se pudo iniciar sesión. " +
                            "Revisa tus datos y tu conexión."
                )
            }
    }
    fun crearCuenta(
        correo: String,
        contrasena: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(
            correo.trim(),
            contrasena
        )
            .addOnSuccessListener { resultado ->
                val uid = resultado.user?.uid

                if (uid != null) {
                    onSuccess(uid)
                } else {
                    onError(
                        "La cuenta se creó, pero no pudimos " +
                                "obtener su identificador."
                    )
                }
            }
            .addOnFailureListener {
                onError(
                    "No se pudo crear la cuenta. Revisa tu conexión, " +
                            "los datos o si el correo ya está registrado."
                )
            }
    }
    fun guardarPerfil(
        usuario: User,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uidActual = auth.currentUser?.uid

        if (uidActual == null || usuario.uid != uidActual) {
            onError("No se pudo verificar el usuario del perfil.")
            return
        }

        db.collection("users")
            .document(uidActual)
            .set(usuario)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(
                    "Tu cuenta existe, pero no se pudo guardar " +
                            "el perfil. Inténtalo nuevamente."
                )
            }
    }
    fun obtenerPerfilActual(
        onSuccess: (User?) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Debes iniciar sesión para consultar tu perfil.")
            return
        }

        db.collection("users")
            .document(uid)
            .get(Source.SERVER)
            .addOnSuccessListener { documento ->
                if (!documento.exists()) {
                    onSuccess(null)
                    return@addOnSuccessListener
                }

                val usuario = try {
                    documento.toObject(User::class.java)
                } catch (e: Exception) {
                    onError("El perfil guardado tiene un formato inválido.")
                    return@addOnSuccessListener
                }

                if (usuario == null || usuario.uid != uid) {
                    onError("No se pudo verificar el perfil guardado.")
                } else {
                    onSuccess(usuario)
                }
            }
            .addOnFailureListener {
                onError(
                    "No se pudo consultar tu perfil. " +
                            "Revisa tu conexión e inténtalo nuevamente."
                )
            }
    }
    fun recuperarContrasena(
        correo: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val correoLimpio = correo.trim()

        if (correoLimpio.isBlank()) {
            onError("Ingresa tu correo para recuperar la contraseña.")
            return
        }

        auth.sendPasswordResetEmail(correoLimpio)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(
                    "No se pudo solicitar la recuperación. " +
                            "Revisa el correo y tu conexión."
                )
            }
    }
    fun actualizarRol(
        rol: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Debes iniciar sesión para elegir tu perfil.")
            return
        }

        if (
            rol != com.uvg.evora.data.model.Roles.COMPRADOR &&
            rol != com.uvg.evora.data.model.Roles.VENDEDOR
        ) {
            onError("Selecciona un perfil válido.")
            return
        }

        db.collection("users")
            .document(uid)
            .update("rolActivo", rol)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(
                    "No se pudo guardar tu perfil. " +
                            "Revisa tu conexión e inténtalo nuevamente."
                )
            }
    }
}