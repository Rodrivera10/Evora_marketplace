package com.uvg.evora.data.model

data class Product(
    val id: String = "",
    val storeId: String = "",
    val ownerUid: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val umbralStockBajo: Int = 5,
    val activo: Boolean = true,
    val imagenUrl: String = "",
    val codigoQr: String = "",
    val fechaCreacion: Long = 0L
)
