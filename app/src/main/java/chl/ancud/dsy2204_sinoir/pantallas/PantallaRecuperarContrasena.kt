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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import chl.ancud.dsy2204_sinoir.datos.RepositorioUsuarios
import chl.ancud.dsy2204_sinoir.ui.theme.VerdeExito
import chl.ancud.dsy2204_sinoir.utils.correoEsValido

// Vista para recuperar la contraseña.
// Firebase envía el correo de recuperación de verdad.
// Por seguridad, Firebase no dice si el correo tiene cuenta o no
// (así nadie puede averiguar qué correos están registrados).
@Composable
fun PantallaRecuperarContrasena(controladorNavegacion: NavHostController) {

    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var esError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        EncabezadoPantalla(
            titulo = "Recuperar contraseña",
            alVolver = { controladorNavegacion.popBackStack() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ingresa tu correo y te enviaremos las instrucciones para recuperar tu contraseña",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it.trim() },
            label = { Text("Correo electrónico") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        if (mensaje != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensaje,
                color = if (esError) MaterialTheme.colorScheme.error else VerdeExito,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (!correoEsValido(correo)) {
                    esError = true
                    mensaje = "El correo no tiene un formato válido"
                } else {
                    RepositorioUsuarios.recuperarContrasena(correo) { error ->
                        if (error == null) {
                            esError = false
                            mensaje = "Si existe una cuenta con $correo, las instrucciones se enviarán a ese correo. Revisa también la carpeta de spam."
                        } else {
                            esError = true
                            mensaje = error
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vínculo: volver al login
        TextButton(onClick = { controladorNavegacion.popBackStack() }) {
            Text("Volver a Ingresar")
        }
    }
}
