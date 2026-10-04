package chl.ancud.dsy2204_sinoir.pantallas

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import chl.ancud.dsy2204_sinoir.ui.theme.AlertaAmarillo
import chl.ancud.dsy2204_sinoir.ui.theme.AlertaRojo
import kotlinx.coroutines.delay

// La alerta se apaga sola después de 1 minuto, para no calentar
// el teléfono ni gastar batería de más
private const val DURACION_ALERTA_MS = 60_000L

// Cada cuánto parpadean la linterna y la pantalla
private const val INTERVALO_PARPADEO_MS = 300L

// Vista Buscar dispositivo (versión local).
// Activa vibración, linterna parpadeante, pantalla parpadeante y sonido.
// Es una muestra de lo que haría el teléfono perdido si recibiera una
// notificación remota (eso requiere Firebase Cloud Messaging y un servidor).
@Composable
fun PantallaBuscarDispositivo(controladorNavegacion: NavHostController) {

    val contexto = LocalContext.current

    // Interruptores para elegir qué se activa
    var usarVibracion by remember { mutableStateOf(true) }
    var usarLinterna by remember { mutableStateOf(true) }
    var usarPantalla by remember { mutableStateOf(true) }
    var usarSonido by remember { mutableStateOf(true) }

    var alertaActiva by remember { mutableStateOf(false) }
    var luzEncendida by remember { mutableStateOf(false) }

    // Componentes del dispositivo (pueden ser null si el teléfono no los tiene)
    val vibrador: Vibrator? = remember { contexto.getSystemService(Vibrator::class.java) }
    val camara: CameraManager? = remember { contexto.getSystemService(CameraManager::class.java) }
    val idCamaraConFlash: String? = remember { buscarCamaraConFlash(camara) }
    val tono = remember {
        RingtoneManager.getRingtone(contexto, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
    }

    // Apaga todo lo que esté encendido
    fun detenerTodo() {
        vibrador?.cancel()
        tono?.stop()
        if (idCamaraConFlash != null) {
            cambiarLinterna(camara, idCamaraConFlash, false)
        }
        luzEncendida = false
    }

    // Se ejecuta cada vez que cambia "alertaActiva".
    // Si se activa: enciende vibración y sonido, y hace parpadear la luz.
    // Si se desactiva: detiene todo.
    LaunchedEffect(alertaActiva) {
        if (alertaActiva) {
            if (usarVibracion) {
                // Patrón: espera 0 ms, vibra 800 ms, pausa 400 ms. El 0 final = repetir
                val patron = longArrayOf(0, 800, 400)
                vibrador?.vibrate(VibrationEffect.createWaveform(patron, 0))
            }
            if (usarSonido) {
                tono?.isLooping = true
                tono?.play()
            }

            var tiempoTranscurrido = 0L
            while (tiempoTranscurrido < DURACION_ALERTA_MS) {
                luzEncendida = !luzEncendida
                if (usarLinterna && idCamaraConFlash != null) {
                    cambiarLinterna(camara, idCamaraConFlash, luzEncendida)
                }
                delay(INTERVALO_PARPADEO_MS)
                tiempoTranscurrido += INTERVALO_PARPADEO_MS
            }
            // Se cumplió el minuto: se apaga sola
            alertaActiva = false
        } else {
            detenerTodo()
        }
    }

    // Si el usuario sale de la pantalla con la alerta encendida, se apaga todo
    DisposableEffect(Unit) {
        onDispose { detenerTodo() }
    }

    // Color de fondo: parpadea entre amarillo y rojo mientras la alerta está activa
    val colorFondo = if (alertaActiva && usarPantalla) {
        if (luzEncendida) AlertaAmarillo else AlertaRojo
    } else {
        MaterialTheme.colorScheme.background
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(24.dp)
    ) {
        EncabezadoPantalla(
            titulo = "Buscar dispositivo",
            alVolver = { controladorNavegacion.popBackStack() }
        )

        Text(text = "Activa una alerta de vibración y luces para encontrar tu teléfono")

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                FilaInterruptor("📳 Vibración", usarVibracion, !alertaActiva) { usarVibracion = it }
                FilaInterruptor("🔦 Linterna", usarLinterna, !alertaActiva) { usarLinterna = it }
                FilaInterruptor("💡 Pantalla", usarPantalla, !alertaActiva) { usarPantalla = it }
                FilaInterruptor("🔔 Sonido", usarSonido, !alertaActiva) { usarSonido = it }
            }
        }

        if (idCamaraConFlash == null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Este dispositivo no tiene linterna", fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón: activar / detener
        if (!alertaActiva) {
            Button(
                onClick = { alertaActiva = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text("Activar alerta", fontSize = 22.sp)
            }
        } else {
            Button(
                onClick = { alertaActiva = false },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text("Detener", fontSize = 22.sp)
            }
        }
    }
}

// Fila con un texto y un interruptor (Switch).
// "alCambiar" es una lambda que recibe el nuevo valor del interruptor.
@Composable
private fun FilaInterruptor(
    texto: String,
    activo: Boolean,
    habilitado: Boolean,
    alCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = texto, fontSize = 18.sp, modifier = Modifier.weight(1f))
        Switch(checked = activo, onCheckedChange = alCambiar, enabled = habilitado)
    }
}

// Busca una cámara que tenga flash. Devuelve su id, o null si no hay.
private fun buscarCamaraConFlash(camara: CameraManager?): String? {
    if (camara == null) return null
    return try {
        camara.cameraIdList.firstOrNull { id ->
            camara.getCameraCharacteristics(id).get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }
    } catch (e: Exception) {
        null
    }
}

// Enciende o apaga la linterna. No necesita permiso de cámara.
private fun cambiarLinterna(camara: CameraManager?, idCamara: String, encendida: Boolean) {
    try {
        camara?.setTorchMode(idCamara, encendida)
    } catch (e: Exception) {
        // Si la cámara está ocupada por otra app, simplemente no se enciende
    }
}
