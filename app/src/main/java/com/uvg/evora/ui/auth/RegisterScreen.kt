package com.uvg.evora.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.uvg.evora.R

@Composable
fun RegisterScreen(
    estado: RegisterUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onConfirmacionChange: (String) -> Unit,
    onTerminosChange: (Boolean) -> Unit,
    onCrearCuenta: () -> Unit,
    onIrALogin: () -> Unit
) {
    val editable = !estado.cargando && !estado.cuentaCreada

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.evora_logo),
            contentDescription = "Evora Marketplace",
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Crea tu cuenta",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Conecta con emprendedores de tu comunidad."
        )

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = editable
        )

        OutlinedTextField(
            value = estado.correo,
            onValueChange = onCorreoChange,
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = editable,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        if (!estado.cuentaCreada) {
            OutlinedTextField(
                value = estado.contrasena,
                onValueChange = onContrasenaChange,
                label = { Text("Contraseña") },
                supportingText = { Text("Mínimo 8 caracteres") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = editable,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )

            OutlinedTextField(
                value = estado.confirmacion,
                onValueChange = onConfirmacionChange,
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = editable,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = estado.aceptaTerminos,
                onCheckedChange = onTerminosChange,
                enabled = editable
            )

            Text(
                text = "Acepto los términos y la política de privacidad.",
                modifier = Modifier.weight(1f)
            )
        }

        if (estado.error != null) {
            Text(
                text = estado.error,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (estado.cuentaCreada) {
            Text(
                text = "Tu cuenta ya existe. Estamos completando tu perfil."
            )
        }

        Button(
            onClick = onCrearCuenta,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp),
            enabled = !estado.cargando && !estado.registroCompleto
        ) {
            Text(
                text = when {
                    estado.cargando -> "Procesando..."
                    estado.cuentaCreada -> "Reintentar guardar perfil"
                    else -> "Crear cuenta"
                }
            )
        }

        TextButton(
            onClick = onIrALogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !estado.cargando
        ) {
            Text("Ya tienes cuenta: inicia sesión")
        }
    }
}
