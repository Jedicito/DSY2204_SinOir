package chl.ancud.dsy2204_sinoir.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import chl.ancud.dsy2204_sinoir.datos.RepositorioUsuarios
import chl.ancud.dsy2204_sinoir.navegacion.Rutas
import chl.ancud.dsy2204_sinoir.navegacion.volverAlLogin

// Datos de cada tarjeta del menú
private data class OpcionMenu(
    val titulo: String,
    val emoji: String,
    val descripcion: String,
    val ruta: String
)

// Opciones del menú principal
private val opcionesMenu = listOf(
    OpcionMenu("Escribir", "🎤", "Convierte en texto lo que te dicen", Rutas.ESCRIBIR),
    OpcionMenu("Hablar", "🔊", "El teléfono dice en voz alta lo que escribes", Rutas.HABLAR),
    OpcionMenu("Buscar dispositivo", "📳", "Vibración y luces para encontrar tu teléfono", Rutas.BUSCAR_DISPOSITIVO),
    OpcionMenu("Mi perfil", "👤", "Tus datos y tu historial", Rutas.PERFIL)
)

// Menú principal que se muestra después de un login exitoso
@Composable
fun PantallaHomeMenu(controladorNavegacion: NavHostController) {

    // El "?." y el "?:" manejan el caso en que no haya sesión (null)
    val nombreUsuario = RepositorioUsuarios.usuarioActual?.nombreUsuario ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "¡Bienvenido, $nombreUsuario!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Elige una opción para comenzar")

        Spacer(modifier = Modifier.height(20.dp))

        // Grilla con las opciones del menú
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(opcionesMenu) { opcion ->
                Card(
                    onClick = { controladorNavegacion.navigate(opcion.ruta) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.9f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = opcion.emoji, fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = opcion.titulo,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = opcion.descripcion,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cerrar sesión y volver al login
        OutlinedButton(
            onClick = {
                RepositorioUsuarios.cerrarSesion()
                volverAlLogin(controladorNavegacion)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}
