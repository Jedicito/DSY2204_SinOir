package chl.ancud.dsy2204_sinoir.pantallas

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import chl.ancud.dsy2204_sinoir.datos.RepositorioMensajes
import chl.ancud.dsy2204_sinoir.datos.RepositorioUsuarios
import chl.ancud.dsy2204_sinoir.modelo.Mensaje
import chl.ancud.dsy2204_sinoir.modelo.OrigenMensaje
import chl.ancud.dsy2204_sinoir.modelo.opcionesComunicacion
import chl.ancud.dsy2204_sinoir.navegacion.volverAlLogin
import chl.ancud.dsy2204_sinoir.utils.formatearFecha
import chl.ancud.dsy2204_sinoir.utils.textoEsValido

// Vista Mi perfil.
// - UPDATE de usuario: editar nombre y forma de comunicación
// - DELETE de usuario: eliminar la cuenta
// - Historial: mensajes guardados de Escribir y Hablar, con opción de borrar
@Composable
fun PantallaPerfil(controladorNavegacion: NavHostController) {

    val contexto = LocalContext.current
    val usuario = RepositorioUsuarios.usuarioActual

    // Si por alguna razón no hay sesión, no se muestra nada más
    if (usuario == null) {
        Text(text = "No hay una sesión iniciada", modifier = Modifier.padding(24.dp))
        return
    }

    var nombreUsuario by remember { mutableStateOf(usuario.nombreUsuario) }
    var comunicacionSeleccionada by remember { mutableStateOf(usuario.tipoComunicacion) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    val mensajes = remember { mutableStateListOf<Mensaje>() }

    // Recarga el historial desde el repositorio
    fun cargarMensajes() {
        RepositorioMensajes.obtenerPorUsuario(usuario.id) { lista ->
            mensajes.clear()
            mensajes.addAll(lista)
        }
    }

    LaunchedEffect(Unit) {
        cargarMensajes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        EncabezadoPantalla(
            titulo = "Mi perfil",
            alVolver = { controladorNavegacion.popBackStack() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ----- Datos de la cuenta -----
        Text(text = "Correo: ${usuario.correo}")
        Text(text = "Tipo de discapacidad: ${usuario.tipoDiscapacidad}")

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nombreUsuario,
            onValueChange = { nombreUsuario = it },
            label = { Text("Nombre de usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

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

        // Botón: guardar cambios del perfil
        Button(
            onClick = {
                if (!textoEsValido(nombreUsuario)) {
                    Toast.makeText(contexto, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                } else {
                    val usuarioEditado = usuario.copy(
                        nombreUsuario = nombreUsuario.trim(),
                        tipoComunicacion = comunicacionSeleccionada
                    )
                    RepositorioUsuarios.actualizar(usuarioEditado) { error ->
                        val texto = error ?: "Datos actualizados"
                        Toast.makeText(contexto, texto, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar cambios")
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // ----- Historial de mensajes -----
        Text(text = "Historial de mensajes", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        if (mensajes.isEmpty()) {
            Text(text = "Todavía no hay mensajes guardados")
        }

        mensajes.forEach { mensaje ->
            // Según el origen se muestra un ícono distinto
            val etiquetaOrigen = if (mensaje.origen == OrigenMensaje.ESCUCHADO) {
                "🎤 Escuchado"
            } else {
                "🔊 Dicho"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "$etiquetaOrigen · ${formatearFecha(mensaje.fecha)}",
                            fontSize = 12.sp
                        )
                        Text(text = mensaje.texto, fontSize = 16.sp)
                    }
                    IconButton(onClick = {
                        RepositorioMensajes.eliminar(mensaje.id) { cargarMensajes() }
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar mensaje")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // Botón: eliminar cuenta (pide confirmación)
        OutlinedButton(
            onClick = { mostrarDialogoEliminar = true },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar mi cuenta")
        }
    }

    // Diálogo de confirmación para eliminar la cuenta
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("¿Eliminar tu cuenta?") },
            text = { Text("Se borrarán tus datos, tus frases y tu historial. No se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoEliminar = false
                    RepositorioUsuarios.eliminarCuenta { error ->
                        if (error == null) {
                            Toast.makeText(contexto, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
                            volverAlLogin(controladorNavegacion)
                        } else {
                            Toast.makeText(contexto, error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
