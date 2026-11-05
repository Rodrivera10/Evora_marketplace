package com.uvg.evora.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.uvg.evora.R

@Composable
fun LoginScreen(
    correo: String,
    contrasena: String,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onIniciarSesion: () -> Unit,
    onIrARegistro: () -> Unit,
    cargando: Boolean = false,
    error: String? = null,
    onRecuperarContrasena: () -> Unit,
    mensaje: String? = null
) {
    var mostrarContrasena by rememberSaveable {
        mutableStateOf(false)
    }

    var modoRecuperacion by rememberSaveable {
        mutableStateOf(false)
    }

    var solicitudRecuperacion by rememberSaveable {
        mutableStateOf(false)
    }

    var intentoLogin by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.evora_logo),
            contentDescription = "Evora Marketplace",
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (modoRecuperacion) {
                "Recupera tu contraseña"
            } else {
                "¡Bienvenido!"
            },
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (modoRecuperacion) {
                "Ingresa tu correo y te enviaremos un enlace para cambiarla."
            } else {
                "Inicia sesión para continuar."
            },
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = correo,
            onValueChange = {
                solicitudRecuperacion = false
                intentoLogin = false
                onCorreoChange(it)
            },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !cargando,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        if (!modoRecuperacion) {
            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    intentoLogin = false
                    onContrasenaChange(it)
                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !cargando,
                visualTransformation = if (mostrarContrasena) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            mostrarContrasena = !mostrarContrasena
                        },
                        enabled = !cargando
                    ) {
                        Icon(
                            imageVector = if (mostrarContrasena) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (mostrarContrasena) {
                                "Ocultar contraseña"
                            } else {
                                "Mostrar contraseña"
                            }
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )

            TextButton(
                onClick = {
                    modoRecuperacion = true
                    mostrarContrasena = false
                    solicitudRecuperacion = false
                    intentoLogin = false
                },
                modifier = Modifier.align(Alignment.End),
                enabled = !cargando
            ) {
                Text("¿Olvidaste tu contraseña?")
            }
        }

        if (
            modoRecuperacion &&
            solicitudRecuperacion &&
            mensaje != null
        ) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary
            )
        }

        val mostrarError = if (modoRecuperacion) {
            solicitudRecuperacion
        } else {
            intentoLogin
        }

        if (mostrarError && error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                if (modoRecuperacion) {
                    solicitudRecuperacion = true
                    onRecuperarContrasena()
                } else {
                    intentoLogin = true
                    onIniciarSesion()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp),
            enabled = !cargando
        ) {
            Text(
                text = when {
                    cargando && modoRecuperacion -> "Enviando enlace..."
                    cargando -> "Iniciando sesión..."
                    modoRecuperacion -> "Enviar enlace"
                    else -> "Iniciar sesión"
                }
            )
        }

        if (modoRecuperacion) {
            TextButton(
                onClick = {
                    modoRecuperacion = false
                    solicitudRecuperacion = false
                    intentoLogin = false
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            ) {
                Text("Volver a iniciar sesión")
            }
        } else {
            TextButton(
                onClick = onIrARegistro,
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            ) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}