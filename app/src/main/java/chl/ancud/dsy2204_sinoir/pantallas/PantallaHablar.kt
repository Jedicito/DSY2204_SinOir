package chl.ancud.dsy2204_sinoir.pantallas

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import chl.ancud.dsy2204_sinoir.datos.RepositorioFrases
import chl.ancud.dsy2204_sinoir.datos.RepositorioMensajes
import chl.ancud.dsy2204_sinoir.datos.RepositorioUsuarios
import chl.ancud.dsy2204_sinoir.modelo.FraseFrecuente
import chl.ancud.dsy2204_sinoir.modelo.Mensaje
import chl.ancud.dsy2204_sinoir.modelo.OrigenMensaje
import chl.ancud.dsy2204_sinoir.utils.textoEsValido
import java.util.Locale

// Vista Hablar (texto a voz).
// El usuario escribe y el teléfono lo dice en voz alta con TextToSpeech.
// Cada mensaje dicho se guarda solo en el historial.
// Las frases frecuentes tienen CRUD: agregar, ver, editar y borrar.
@Composable
fun PantallaHablar(controladorNavegacion: NavHostController) {

    val contexto = LocalContext.current
    val usuario = RepositorioUsuarios.usuarioActual

    var textoMensaje by remember { mutableStateOf("") }
    var motorListo by remember { mutableStateOf(false) }
    var avisoVoz by remember { mutableStateOf("") }

    // Frases frecuentes
    val frases = remember { mutableStateListOf<FraseFrecuente>() }
    var textoNuevaFrase by remember { mutableStateOf("") }

    // Diálogo para editar una frase (null = diálogo cerrado)
    var fraseEnEdicion by remember { mutableStateOf<FraseFrecuente?>(null) }
    var textoEdicion by remember { mutableStateOf("") }

    // Motor de texto a voz. Tarda un momento en cargar y avisa en la lambda
    val motorVoz = remember {
        TextToSpeech(contexto) { estado ->
            motorListo = (estado == TextToSpeech.SUCCESS)
        }
    }

    // Cuando el motor está listo, se le indica el idioma español
    LaunchedEffect(motorListo) {
        if (motorListo) {
            val resultado = motorVoz.setLanguage(Locale.forLanguageTag("es-CL"))
            if (resultado == TextToSpeech.LANG_MISSING_DATA ||
                resultado == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                avisoVoz = "Falta la voz en español. Descárgala en Ajustes > Texto a voz"
            }
        }
    }

    // Al salir de la pantalla se apaga el motor para liberar recursos
    DisposableEffect(Unit) {
        onDispose {
            motorVoz.stop()
            motorVoz.shutdown()
        }
    }

    // Recarga la lista de frases desde el repositorio
    fun cargarFrases() {
        if (usuario == null) return
        RepositorioFrases.obtenerPorUsuario(usuario.id) { lista ->
            frases.clear()
            frases.addAll(lista)
        }
    }

    // Dice el texto en voz alta y lo guarda en el historial
    fun decir(texto: String) {
        if (!motorListo) {
            avisoVoz = "El motor de voz aún no está listo, intenta de nuevo"
            return
        }
        motorVoz.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "mensaje")
        if (usuario != null) {
            RepositorioMensajes.agregar(
                Mensaje(
                    idUsuario = usuario.id,
                    texto = texto,
                    origen = OrigenMensaje.DICHO,
                    fecha = System.currentTimeMillis()
                )
            )
        }
    }

    // Carga las frases una vez, al abrir la pantalla
    LaunchedEffect(Unit) {
        cargarFrases()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        EncabezadoPantalla(
            titulo = "Hablar",
            alVolver = { controladorNavegacion.popBackStack() }
        )

        Text(text = "Escribe lo que quieres decir y el teléfono lo dirá en voz alta")
        Text(text = "Si no escuchas nada, sube el volumen multimedia", fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Input: mensaje a decir
        OutlinedTextField(
            value = textoMensaje,
            onValueChange = { textoMensaje = it },
            label = { Text("Tu mensaje") },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        if (avisoVoz != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = avisoVoz, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón: decir en voz alta
        Button(
            onClick = {
                if (textoEsValido(textoMensaje)) {
                    decir(textoMensaje)
                    textoMensaje = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Text("🔊 Decir en voz alta", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ----- Frases frecuentes -----
        Text(text = "Frases frecuentes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = "Toca una frase para decirla", fontSize = 12.sp)

        Spacer(modifier = Modifier.height(8.dp))

        if (frases.isEmpty()) {
            Text(text = "Aún no tienes frases guardadas")
        }

        frases.forEach { frase ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = frase.frase,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { decir(frase.frase) }
                            .padding(16.dp)
                    )
                    // Editar
                    IconButton(onClick = {
                        fraseEnEdicion = frase
                        textoEdicion = frase.frase
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar frase")
                    }
                    // Eliminar
                    IconButton(onClick = {
                        RepositorioFrases.eliminar(frase.id) { cargarFrases() }
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar frase")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Agregar una frase nueva
        OutlinedTextField(
            value = textoNuevaFrase,
            onValueChange = { textoNuevaFrase = it },
            label = { Text("Nueva frase") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                if (usuario != null && textoEsValido(textoNuevaFrase)) {
                    val fraseNueva = FraseFrecuente(idUsuario = usuario.id, frase = textoNuevaFrase.trim())
                    RepositorioFrases.agregar(fraseNueva) {
                        textoNuevaFrase = ""
                        cargarFrases()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar frase")
        }
    }

    // Diálogo para editar una frase
    val fraseAEditar = fraseEnEdicion
    if (fraseAEditar != null) {
        AlertDialog(
            onDismissRequest = { fraseEnEdicion = null },
            title = { Text("Editar frase") },
            text = {
                OutlinedTextField(
                    value = textoEdicion,
                    onValueChange = { textoEdicion = it },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (textoEsValido(textoEdicion)) {
                        RepositorioFrases.actualizar(fraseAEditar.copy(frase = textoEdicion.trim())) {
                            fraseEnEdicion = null
                            cargarFrases()
                        }
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { fraseEnEdicion = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
