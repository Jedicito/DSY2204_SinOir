package chl.ancud.dsy2204_sinoir.modelo

// Un mensaje guardado en el historial.
// Equivale a la tabla: mensajes_xUsuario [id, idUsuario, texto, origen, fecha]
data class Mensaje(
    val id: String = "",
    val idUsuario: String = "",
    val texto: String = "",
    val origen: String = "",
    val fecha: Long = 0L
)

// Valores posibles del campo "origen"
object OrigenMensaje {
    const val ESCUCHADO = "escuchado" // viene de la pantalla Escribir (micrófono)
    const val DICHO = "dicho"         // viene de la pantalla Hablar (parlante)
}
