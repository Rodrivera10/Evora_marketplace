package com.uvg.evora.data.model

data class OrderItem(
    val productId: String = "",
    val nombre: String = "",
    val precioUnitario: Double = 0.0,
    val cantidad: Int = 0

)

data class Order(
    val id: String = "",
    val storeId: String = "",
    val ownerUid: String = "",
    val buyerUid: String = "",
    val nombreCliente: String = "",
    val items: List<OrderItem> = emptyList(),
    val subtotal: Double = 0.0,
    val costoEnvio: Double = 0.0,
    val total: Double = 0.0,
    val estado: String = "pendiente",
    val metodoPago: String = "",
    val estadoPago: String = "pendiente",
    val origen: String = "marketplace",
    val puntoEntrega: String = "",
    val indicaciones: String = "",
    val fechaCreacion: Long = 0L
)