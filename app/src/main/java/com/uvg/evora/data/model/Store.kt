package com.uvg.evora.data.model

object Categorias {
    const val COMIDA = "Comida"
    const val BEBIDAS = "Bebidas"
    const val PAPELERIA = "Papelería"
    const val TECNOLOGIA = "Tecnología"
    const val SERVICIOS = "Servicios"

    val todas = listOf(COMIDA, BEBIDAS, PAPELERIA, TECNOLOGIA, SERVICIOS)
}

data class Store(
    val id: String = "",
    val ownerUid: String = "",
    val nombre: String = "",
    val categoria: String = Categorias.COMIDA,
    val descripcion: String = "",
    val abierta: Boolean = true,
    val calificacion: Double = 0.0,
    val totalResenas: Int = 0,
    val tiempoEstimado: String = "",
    val fechaCreacion: Long = 0L
)