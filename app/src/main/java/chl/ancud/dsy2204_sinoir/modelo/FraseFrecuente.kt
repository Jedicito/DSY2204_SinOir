package chl.ancud.dsy2204_sinoir.modelo

// Frase frecuente de un usuario (pantalla Hablar).
// Equivale a la tabla: frases_frecuentes_xUsuario [id, idUsuario, frase]
// Campos "var" con valores por defecto: así Firestore puede llenarlos.
data class FraseFrecuente(
    var id: String = "",
    var idUsuario: String = "",
    var frase: String = ""
)
