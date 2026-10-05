package chl.ancud.dsy2204_sinoir.modelo

// Un mensaje guardado en el historial.
// Equivale a la tabla: mensajes_xUsuario [id, idUsuario, texto, origen, fecha]
// Campos "var" con valores por defecto: así Firestore puede llenarlos.
data class Mensaje(
    var id: String = "",
    var idUsuario: String = "",
    var texto: String = "",
    var origen: String = "",
    var fecha: Long = 0L
)

// Valores posibles del campo "origen"
object OrigenMensaje {
    const val ESCUCHADO = "escuchado" // viene de la pantalla Escribir (micrófono)
    const val DICHO = "dicho"         // viene de la pantalla Hablar (parlante)
}
