package chl.ancud.dsy2204_sinoir.pantallas

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Encabezado que se repite en las pantallas internas:
// flecha para volver + título.
// Recibe una lambda "alVolver" para que cada pantalla decida qué hacer al volver.
@Composable
fun EncabezadoPantalla(titulo: String, alVolver: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = alVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
        Text(text = titulo, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}
