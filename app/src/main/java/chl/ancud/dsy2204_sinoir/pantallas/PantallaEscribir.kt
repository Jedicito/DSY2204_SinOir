package chl.ancud.dsy2204_sinoir.pantallas

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import chl.ancud.dsy2204_sinoir.datos.RepositorioMensajes
import chl.ancud.dsy2204_sinoir.datos.RepositorioUsuarios
import chl.ancud.dsy2204_sinoir.modelo.Mensaje
import chl.ancud.dsy2204_sinoir.modelo.OrigenMensaje

// Vista Escribir (voz a texto).
// La otra persona habla, el teléfono lo transcribe y el usuario sordo lo lee.
// Usa el reconocedor de voz de Google que trae Android (con un Intent).
// Cada línea reconocida se guarda sola en el historial.
@Composable
fun PantallaEscribir(controladorNavegacion: NavHostController) {

    val usuario = RepositorioUsuarios.usuarioActual

    // Líneas de la conversación actual (solo lo que se ve en pantalla)
    val lineasConversacion = remember { mutableStateListOf<String>() }
    var mensajeError by remember { mutableStateOf("") }
    val estadoLista = rememberLazyListState()

    // Lanzador que abre el reconocedor de voz y recibe el resultado en la lambda
    val lanzadorReconocimiento = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { resultado ->
        if (resultado.resultCode == Activity.RESULT_OK) {
            // El reconocedor devuelve una lista de posibles textos; el primero es el más probable
            val posiblesTextos = resultado.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val textoReconocido = posiblesTextos?.firstOrNull()

            if (textoReconocido != null) {
                lineasConversacion.add(textoReconocido)

                // Guardado automático en el historial
                if (usuario != null) {
                    RepositorioMensajes.agregar(
                        Mensaje(
                            idUsuario = usuario.id,
                            texto = textoReconocido,
                            origen = OrigenMensaje.ESCUCHADO,
                            fecha = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }

    // Cada vez que llega una línea nueva, la lista baja hasta la última
    LaunchedEffect(lineasConversacion.size) {
        if (lineasConversacion.isNotEmpty()) {
            estadoLista.animateScrollToItem(lineasConversacion.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        EncabezadoPantalla(
            titulo = "Escribir",
            alVolver = { controladorNavegacion.popBackStack() }
        )

        Text(text = "Toca \"Escuchar\" y acerca el teléfono a quien te habla")

        Spacer(modifier = Modifier.height(16.dp))

        // Recuadro con el texto reconocido, en letra grande
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (lineasConversacion.isEmpty()) {
                Text(
                    text = "Aquí aparecerá lo que te digan",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    state = estadoLista,
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lineasConversacion) { linea ->
                        Text(text = linea, fontSize = 26.sp)
                    }
                }
            }
        }

        if (mensajeError != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = mensajeError, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Botón: limpiar la pantalla (no borra el historial guardado)
            OutlinedButton(
                onClick = { lineasConversacion.clear() },
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
            ) {
                Text("Limpiar")
            }

            // Botón: escuchar
            Button(
                onClick = {
                    mensajeError = ""
                    val intento = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intento.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    intento.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CL")
                    intento.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...")
                    try {
                        lanzadorReconocimiento.launch(intento)
                    } catch (e: ActivityNotFoundException) {
                        mensajeError = "Este dispositivo no tiene reconocimiento de voz"
                    }
                },
                modifier = Modifier
                    .weight(2f)
                    .height(64.dp)
            ) {
                Text("🎤 Escuchar", fontSize = 20.sp)
            }
        }
    }
}
