package chl.ancud.dsy2204_sinoir.pantallas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import chl.ancud.dsy2204_sinoir.datos.RepositorioUsuarios
import chl.ancud.dsy2204_sinoir.navegacion.Rutas

// Vista de inicio de sesión. Permite ingresar con usuario y contraseña,
// e ir a Registro o a Recuperar contraseña.
@Composable
fun PantallaLogin(controladorNavegacion: NavHostController) {

    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "dsy2204_sinoir",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Comunicación accesible para personas sordas",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input: usuario
        OutlinedTextField(
            value = nombreUsuario,
            onValueChange = { nombreUsuario = it },
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Input: contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        if (mensajeError != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón: ingresar
        Button(
            onClick = {
                val usuarioEncontrado = RepositorioUsuarios.buscarUsuario(nombreUsuario, contrasena)
                if (usuarioEncontrado != null) {
                    mensajeError = ""
                    controladorNavegacion.navigate(Rutas.crearRutaInicio(nombreUsuario))
                } else {
                    mensajeError = "Usuario o contraseña incorrectos"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Usuario de prueba: usuario_demo / 12345",
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Vínculo: ir a recuperar contraseña
        TextButton(onClick = { controladorNavegacion.navigate(Rutas.RECUPERAR_CONTRASENA) }) {
            Text("¿Olvidaste tu contraseña?")
        }

        // Vínculo: ir a registro
        TextButton(onClick = { controladorNavegacion.navigate(Rutas.REGISTRO) }) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}
