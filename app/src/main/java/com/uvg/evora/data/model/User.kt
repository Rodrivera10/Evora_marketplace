package com.uvg.evora.data.model

object Roles {
    const val COMPRADOR = "comprador"
    const val VENDEDOR = "vendedor"
}

data class User(
    val uid: String = "",
    val nombre: String = "",
    val correo: String = "",
    val rolActivo: String = Roles.COMPRADOR,
    val fechaCreacion: Long = 0L
)
