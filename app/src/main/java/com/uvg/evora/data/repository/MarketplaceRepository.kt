package com.uvg.evora.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.uvg.evora.data.model.Product
import com.uvg.evora.data.model.Store

class MarketplaceRepository {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerNegocios(
        onSuccess: (List<Store>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("stores")
            .limit(20)
            .get()
            .addOnSuccessListener { resultado ->
                try {
                    val negocios = resultado.documents.mapNotNull { documento ->
                        documento.toObject(Store::class.java)?.copy(
                            id = documento.id
                        )
                    }

                    onSuccess(negocios)
                } catch (e: Exception) {
                    onError("No se pudieron interpretar los negocios.")
                }
            }
            .addOnFailureListener {
                onError(
                    "No se pudieron cargar los negocios. " +
                            "Revisa tu conexión y los permisos."
                )
            }
    }

    fun obtenerProductos(
        onSuccess: (List<Product>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("products")
            .whereEqualTo("activo", true)
            .limit(20)
            .get()
            .addOnSuccessListener { resultado ->
                try {
                    val productos = resultado.documents.mapNotNull { documento ->
                        documento.toObject(Product::class.java)?.copy(
                            id = documento.id
                        )
                    }

                    onSuccess(productos)
                } catch (e: Exception) {
                    onError("No se pudieron interpretar los productos.")
                }
            }
            .addOnFailureListener {
                onError(
                    "No se pudieron cargar los productos. " +
                            "Revisa tu conexión y los permisos."
                )
            }
    }

    fun obtenerNegocio(
        storeId: String,
        onSuccess: (Store?) -> Unit,
        onError: (String) -> Unit
    ) {
        if (storeId.isBlank()) {
            onError("El identificador del negocio no es válido.")
            return
        }

        db.collection("stores")
            .document(storeId)
            .get()
            .addOnSuccessListener { documento ->
                try {
                    val negocio = if (documento.exists()) {
                        documento.toObject(Store::class.java)?.copy(
                            id = documento.id
                        )
                    } else {
                        null
                    }

                    onSuccess(negocio)
                } catch (e: Exception) {
                    onError("No se pudo interpretar el negocio.")
                }
            }
            .addOnFailureListener {
                onError(
                    "No se pudo consultar el negocio. " +
                            "Revisa tu conexión y los permisos."
                )
            }
    }

    fun obtenerProductosDelNegocio(
        storeId: String,
        onSuccess: (List<Product>) -> Unit,
        onError: (String) -> Unit
    ) {
        if (storeId.isBlank()) {
            onError("El identificador del negocio no es válido.")
            return
        }

        db.collection("products")
            .whereEqualTo("storeId", storeId)
            .whereEqualTo("activo", true)
            .limit(50)
            .get()
            .addOnSuccessListener { resultado ->
                try {
                    val productos = resultado.documents.mapNotNull { documento ->
                        documento.toObject(Product::class.java)?.copy(
                            id = documento.id
                        )
                    }

                    onSuccess(productos)
                } catch (e: Exception) {
                    onError(
                        "No se pudieron interpretar los productos del negocio."
                    )
                }
            }
            .addOnFailureListener {
                onError(
                    "No se pudieron consultar los productos del negocio. " +
                            "Revisa tu conexión y los permisos."
                )
            }
    }
}