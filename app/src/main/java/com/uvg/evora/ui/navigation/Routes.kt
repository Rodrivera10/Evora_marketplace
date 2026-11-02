package com.uvg.evora.ui.navigation

object Routes {

    //Entrada
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val ROLE = "role"
    const val ONBOARDING = "onboarding"

    // Comprador
    const val BUYER_HOME = "buyer_home"
    const val EXPLORE = "explore"
    const val STORE = "store/{storeId}"
    const val CART = "cart"
    const val CHECKOUT = "checkout"

    // Vendedor
    const val SELLER_HOME = "seller_home"
    const val INVENTORY = "inventory"
    const val NEW_SALE = "new_sale"
    const val CHARGE = "charge"
    const val REPORTS = "reports"

    // Compartidas
    const val SCANNER = "scanner"
    const val PROFILE = "profile"

    fun store(id: String) = "store/$id"
}