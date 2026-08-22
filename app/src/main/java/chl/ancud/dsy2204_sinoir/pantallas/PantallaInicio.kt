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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import chl.ancud.dsy2204_sinoir.navegacion.Rutas

// Opciones rápidas que se muestran en la grilla de la pantalla de inicio
private val opcionesRapidas = listOf("Escribir", "Hablar", "Alertas", "Contactos")

// Pantalla de bienvenida que se muestra después de un login exitoso
@Composable
fun PantallaInicio(controladorNavegacion: NavHostController, nombreUsuario: String) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "¡Bienvenido $nombreUsuario!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Elige una opción para comenzar")

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Solo para mostrar una grilla")


        Spacer(modifier = Modifier.height(20.dp))

        // Grilla con las opciones rápidas de la app
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(opcionesRapidas) { opcion ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f),
                    colors = CardDefaults.cardColors()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = opcion, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para cerrar sesión y volver al login
        OutlinedButton(
            onClick = { controladorNavegacion.navigate(Rutas.LOGIN) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}
