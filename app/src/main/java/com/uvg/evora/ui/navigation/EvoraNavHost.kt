package com.uvg.evora.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uvg.evora.ui.auth.LoginScreen
import com.uvg.evora.ui.auth.LoginViewModel
import com.uvg.evora.ui.auth.RegisterScreen
import com.uvg.evora.ui.auth.RegisterViewModel
import com.uvg.evora.ui.auth.SplashScreen
import androidx.compose.runtime.LaunchedEffect
import com.uvg.evora.ui.auth.RoleScreen
import com.uvg.evora.ui.auth.RoleViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import com.uvg.evora.data.model.Roles
import com.uvg.evora.ui.auth.OnboardingScreen
import com.uvg.evora.ui.auth.OnboardingViewModel
import com.uvg.evora.ui.buyer.BuyerHomeScreen
import com.uvg.evora.ui.buyer.BuyerHomeViewModel
import com.uvg.evora.ui.buyer.ExploreScreen
import com.uvg.evora.ui.buyer.ExploreViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.uvg.evora.ui.buyer.StoreScreen
import com.uvg.evora.ui.buyer.StoreViewModel


@Composable
fun EvoraNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinalizar = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.REGISTER) {
            val registerViewModel: RegisterViewModel = viewModel()

            if (registerViewModel.estado.registroCompleto) {
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.ROLE) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            } else {
                RegisterScreen(
                    estado = registerViewModel.estado,
                    onNombreChange =
                        registerViewModel::actualizarNombre,
                    onCorreoChange =
                        registerViewModel::actualizarCorreo,
                    onContrasenaChange =
                        registerViewModel::actualizarContrasena,
                    onConfirmacionChange =
                        registerViewModel::actualizarConfirmacion,
                    onTerminosChange =
                        registerViewModel::actualizarTerminos,
                    onCrearCuenta = registerViewModel::registrar,
                    onIrALogin = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.REGISTER) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        composable(Routes.LOGIN) {
            val loginViewModel: LoginViewModel = viewModel()

            if (loginViewModel.sesionIniciada) {
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.ROLE) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.ROLE) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            } else {
                LoginScreen(
                    correo = loginViewModel.correo,
                    contrasena = loginViewModel.contrasena,
                    onCorreoChange = loginViewModel::actualizarCorreo,
                    onContrasenaChange = loginViewModel::actualizarContrasena,
                    onIniciarSesion = loginViewModel::iniciarSesion,
                    onIrARegistro = {
                        navController.navigate(Routes.REGISTER) {
                            launchSingleTop = true
                        }
                    },
                    cargando = loginViewModel.cargando,
                    error = loginViewModel.error,
                    onRecuperarContrasena =
                        loginViewModel::recuperarContrasena,
                    mensaje = loginViewModel.mensaje
                )
            }

        }
        composable(Routes.ROLE) {
            val roleViewModel: RoleViewModel = viewModel()

            LaunchedEffect(roleViewModel.rolGuardado) {
                if (roleViewModel.rolGuardado) {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.ROLE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }

            RoleScreen(
                onContinuar = roleViewModel::guardarRol,
                cargando = roleViewModel.cargando,
                error = roleViewModel.error
            )

        }
        composable(Routes.ONBOARDING) {
            val onboardingViewModel: OnboardingViewModel = viewModel()

            val rol = onboardingViewModel.rol
            val errorPerfil = onboardingViewModel.error

            when {
                onboardingViewModel.cargando -> {
                    PantallaTemporal("Cargando tu perfil...")
                }

                errorPerfil != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = errorPerfil)

                            Button(
                                onClick = onboardingViewModel::consultarPerfil
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                rol != null -> {
                    OnboardingScreen(
                        onFinalizar = {
                            val destino = when (rol) {
                                Roles.VENDEDOR -> Routes.SELLER_HOME
                                else -> Routes.BUYER_HOME
                            }

                            navController.navigate(destino) {
                                popUpTo(Routes.ONBOARDING) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
        composable(Routes.BUYER_HOME) {
            val buyerHomeViewModel: BuyerHomeViewModel = viewModel()

            BuyerHomeScreen(
                estado = buyerHomeViewModel.estado,
                onReintentarPerfil = buyerHomeViewModel::cargarPerfil,
                onReintentarNegocios = buyerHomeViewModel::cargarNegocios,
                onReintentarProductos = buyerHomeViewModel::cargarProductos,
                onExplorar = {
                    navController.navigate(Routes.EXPLORE) {
                        launchSingleTop = true
                    }
                },
                onCarrito = {
                    navController.navigate(Routes.CART) {
                        launchSingleTop = true
                    }
                },
                onPerfil = {
                    navController.navigate(Routes.PROFILE) {
                        launchSingleTop = true
                    }
                },
                onAbrirNegocio = { storeId ->
                    navController.navigate(Routes.store(storeId)) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.EXPLORE) {
            val exploreViewModel: ExploreViewModel = viewModel()

            ExploreScreen(
                estado = exploreViewModel.estado,
                onBusquedaChange = exploreViewModel::actualizarBusqueda,
                onCategoriaChange = exploreViewModel::seleccionarCategoria,
                onReintentar = exploreViewModel::cargarNegocios,
                onVolver = {
                    navController.popBackStack()
                },
                onAbrirNegocio = { storeId ->
                    navController.navigate(Routes.store(storeId)) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Routes.STORE,
            arguments = listOf(
                navArgument("storeId") {
                    type = NavType.StringType
                }
            )
        ) {
            val storeViewModel: StoreViewModel = viewModel()

            StoreScreen(
                estado = storeViewModel.estado,
                onVolver = {
                    navController.popBackStack()
                },
                onReintentarNegocio = storeViewModel::cargarNegocio,
                onReintentarProductos = storeViewModel::cargarProductos
            )
        }

        val pantallasTemporales = listOf(
            Routes.CART,
            Routes.CHECKOUT,
            Routes.SELLER_HOME,
            Routes.INVENTORY,
            Routes.NEW_SALE,
            Routes.CHARGE,
            Routes.REPORTS,
            Routes.SCANNER,
            Routes.PROFILE
        )

        pantallasTemporales.forEach { ruta ->
            composable(ruta) {
                PantallaTemporal(ruta)
            }
        }
    }
}

@Composable
private fun PantallaTemporal(nombre: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = nombre)
    }
}