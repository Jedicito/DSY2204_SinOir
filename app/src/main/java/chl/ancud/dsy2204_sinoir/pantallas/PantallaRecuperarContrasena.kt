package chl.ancud.dsy2204_sinoir.pantallas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import chl.ancud.dsy2204_sinoir.datos.RepositorioUsuarios
import chl.ancud.dsy2204_sinoir.navegacion.Rutas

// Vista para recuperar la contraseña. Por ahora solo valida que el correo
// exista en el repositorio y simula el envío de un correo de recuperación.
@Composable
fun PantallaRecuperarContrasena(controladorNavegacion: NavHostController) {

    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var esError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Recuperar contraseña",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ingresa tu correo y te enviaremos las instrucciones para recuperar tu contraseña",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (mensaje != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensaje,
                color = if (esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val usuarioEncontrado = RepositorioUsuarios.buscarUsuarioPorCorreo(correo)
                if (usuarioEncontrado != null) {
                    esError = false
                    mensaje = "Se enviaron las instrucciones a $correo"
                } else {
                    esError = true
                    mensaje = "No encontramos una cuenta con ese correo"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vínculo: volver al login
        TextButton(onClick = { controladorNavegacion.navigate(Rutas.LOGIN) }) {
            Text("Volver a Ingresar")
        }
    }
}
