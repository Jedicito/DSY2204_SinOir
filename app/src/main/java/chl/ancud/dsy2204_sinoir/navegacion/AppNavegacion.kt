package chl.ancud.dsy2204_sinoir.navegacion

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import chl.ancud.dsy2204_sinoir.pantallas.PantallaBuscarDispositivo
import chl.ancud.dsy2204_sinoir.pantallas.PantallaEscribir
import chl.ancud.dsy2204_sinoir.pantallas.PantallaHablar
import chl.ancud.dsy2204_sinoir.pantallas.PantallaHomeMenu
import chl.ancud.dsy2204_sinoir.pantallas.PantallaLogin
import chl.ancud.dsy2204_sinoir.pantallas.PantallaPerfil
import chl.ancud.dsy2204_sinoir.pantallas.PantallaRecuperarContrasena
import chl.ancud.dsy2204_sinoir.pantallas.PantallaRegistro

// Nombres de las rutas de navegación de la app.
// Ya no se pasa el nombre de usuario por la ruta: ahora el usuario con
// sesión iniciada queda guardado en RepositorioUsuarios.usuarioActual,
// y cualquier pantalla lo puede leer.
object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val RECUPERAR_CONTRASENA = "recuperar_contrasena"
    const val HOME_MENU = "home_menu"
    const val ESCRIBIR = "escribir"
    const val HABLAR = "hablar"
    const val BUSCAR_DISPOSITIVO = "buscar_dispositivo"
    const val PERFIL = "perfil"
}

// Composable principal que arma el grafo de navegación entre las vistas
@Composable
fun AppNavegacion() {
    val controladorNavegacion: NavHostController = rememberNavController()

    NavHost(
        navController = controladorNavegacion,
        startDestination = Rutas.LOGIN,
        // Evita que el contenido quede debajo de la barra de estado y del teclado
        modifier = Modifier.safeDrawingPadding()
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
        composable(Rutas.HOME_MENU) {
            PantallaHomeMenu(controladorNavegacion = controladorNavegacion)
        }
        composable(Rutas.ESCRIBIR) {
            PantallaEscribir(controladorNavegacion = controladorNavegacion)
        }
        composable(Rutas.HABLAR) {
            PantallaHablar(controladorNavegacion = controladorNavegacion)
        }
        composable(Rutas.BUSCAR_DISPOSITIVO) {
            PantallaBuscarDispositivo(controladorNavegacion = controladorNavegacion)
        }
        composable(Rutas.PERFIL) {
            PantallaPerfil(controladorNavegacion = controladorNavegacion)
        }
    }
}

// Vuelve al Login y borra todas las pantallas anteriores de la pila,
// para que el botón "atrás" no regrese a una pantalla con sesión.
// Se usa al cerrar sesión y al eliminar la cuenta.
fun volverAlLogin(controladorNavegacion: NavHostController) {
    controladorNavegacion.navigate(Rutas.LOGIN) {
        popUpTo(Rutas.HOME_MENU) { inclusive = true }
    }
}
