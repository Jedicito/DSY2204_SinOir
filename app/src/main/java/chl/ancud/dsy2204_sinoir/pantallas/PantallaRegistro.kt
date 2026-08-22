package chl.ancud.dsy2204_sinoir.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import chl.ancud.dsy2204_sinoir.datos.RepositorioUsuarios
import chl.ancud.dsy2204_sinoir.modelo.Usuario
import chl.ancud.dsy2204_sinoir.navegacion.Rutas

// Opciones para el combo box de tipo de discapacidad auditiva
private val opcionesTipoDiscapacidad = listOf("Sordera total", "Hipoacusia", "Otro")

// Opciones para los radio button de forma de comunicación preferida
private val opcionesComunicacion = listOf("Lengua de señas", "Lectura labial", "Texto escrito")

// Vista de registro de usuario. Guarda los datos en el arreglo del
// RepositorioUsuarios y muestra una tabla con los usuarios ya registrados.
@Composable
fun PantallaRegistro(controladorNavegacion: NavHostController) {

    var nombreUsuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // Combo box: tipo de discapacidad
    var menuDiscapacidadAbierto by remember { mutableStateOf(false) }
    var tipoDiscapacidadSeleccionada by remember { mutableStateOf(opcionesTipoDiscapacidad[0]) }

    // Radio buttons: forma de comunicación preferida
    var comunicacionSeleccionada by remember { mutableStateOf(opcionesComunicacion[0]) }

    // Checkbox: aceptar términos
    var aceptaTerminos by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Registro de usuario",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombreUsuario,
            onValueChange = { nombreUsuario = it },
            label = { Text("Nombre de usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmarContrasena,
            onValueChange = { confirmarContrasena = it },
            label = { Text("Confirmar contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Combo box de tipo de discapacidad
        Text(text = "Tipo de discapacidad auditiva", fontWeight = FontWeight.Medium)
        Box(modifier = Modifier.wrapContentSize()) {
            OutlinedTextField(
                value = tipoDiscapacidadSeleccionada,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { menuDiscapacidadAbierto = true }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Abrir opciones")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = menuDiscapacidadAbierto,
                onDismissRequest = { menuDiscapacidadAbierto = false }
            ) {
                opcionesTipoDiscapacidad.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            tipoDiscapacidadSeleccionada = opcion
                            menuDiscapacidadAbierto = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Radio buttons: forma de comunicación preferida
        Text(text = "Forma de comunicación preferida", fontWeight = FontWeight.Medium)
        Column(modifier = Modifier.selectableGroup()) {
            opcionesComunicacion.forEach { opcion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (opcion == comunicacionSeleccionada),
                            onClick = { comunicacionSeleccionada = opcion }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (opcion == comunicacionSeleccionada),
                        onClick = { comunicacionSeleccionada = opcion }
                    )
                    Text(text = opcion)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox: aceptar términos
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = aceptaTerminos,
                onCheckedChange = { aceptaTerminos = it }
            )
            Text(text = "Acepto los términos y condiciones")
        }

        if (mensaje != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = mensaje, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón: registrar
        Button(
            onClick = {
                if (nombreUsuario == "" || correo == "" || contrasena == "") {
                    mensaje = "Debes completar todos los campos"
                } else if (contrasena != confirmarContrasena) {
                    mensaje = "Las contraseñas no coinciden"
                } else if (!aceptaTerminos) {
                    mensaje = "Debes aceptar los términos y condiciones"
                } else {
                    val usuarioNuevo = Usuario(
                        nombreUsuario = nombreUsuario,
                        correo = correo,
                        contrasena = contrasena,
                        tipoComunicacion = comunicacionSeleccionada
                    )
                    val seGuardo = RepositorioUsuarios.agregarUsuario(usuarioNuevo)
                    mensaje = if (seGuardo) {
                        "Usuario registrado con éxito"
                    } else {
                        "Ya se alcanzó el máximo de 5 usuarios registrados"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarme")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Vínculo: volver al login
        TextButton(onClick = { controladorNavegacion.navigate(Rutas.LOGIN) }) {
            Text("Ya tengo cuenta, volver a Ingresar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tabla simple con los usuarios ya registrados
        Text(text = "Usuarios registrados", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Usuario", modifier = Modifier.padding(4.dp).fillMaxWidth().weight(1f), fontWeight = FontWeight.Medium)
            Text(text = "Correo", modifier = Modifier.padding(4.dp).fillMaxWidth().weight(1f), fontWeight = FontWeight.Medium)
        }

        RepositorioUsuarios.obtenerUsuariosRegistrados().forEach { usuarioRegistrado ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = usuarioRegistrado.nombreUsuario, modifier = Modifier.padding(4.dp).weight(1f))
                Text(text = usuarioRegistrado.correo, modifier = Modifier.padding(4.dp).weight(1f))
            }
        }
    }
}
