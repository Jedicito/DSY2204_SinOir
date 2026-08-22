package chl.ancud.dsy2204_sinoir.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import chl.ancud.dsy2204_sinoir.pantallas.PantallaInicio
import chl.ancud.dsy2204_sinoir.pantallas.PantallaLogin
import chl.ancud.dsy2204_sinoir.pantallas.PantallaRecuperarContrasena
import chl.ancud.dsy2204_sinoir.pantallas.PantallaRegistro

// Nombres de las rutas de navegación de la app
object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val RECUPERAR_CONTRASENA = "recuperar_contrasena"

    const val INICIO = "inicio/{nombreUsuario}"

    fun crearRutaInicio(nombreUsuario: String): String {
        return "inicio/$nombreUsuario"
    }

}

// Composable principal que arma el grafo de navegación entre las vistas
@Composable
fun AppNavegacion() {
    val controladorNavegacion: NavHostController = rememberNavController()

    NavHost(
        navController = controladorNavegacion,
        startDestination = Rutas.LOGIN
    ) {
        composable(Rutas.LOGIN) {
            PantallaLogin(controladorNavegacion = controladorNavegacion)
        }
        composable(Rutas.REGISTRO) {
            PantallaRegistro(controladorNavegacion = controladorNavegacion)
        }
        composable(Rutas.RECUPERAR_CONTRASENA) {
            PantallaRecuperarContrasena(controladorNavegacion = controladorNavegacion)
        }
        composable(
            route = Rutas.INICIO,
            arguments = listOf(navArgument("nombreUsuario") { type = NavType.StringType })
        ) { entradaNavegacion ->
            val nombreUsuario = entradaNavegacion.arguments?.getString("nombreUsuario") ?: ""
            PantallaInicio(
                controladorNavegacion = controladorNavegacion,
                nombreUsuario = nombreUsuario
            )
        }

    }
}
