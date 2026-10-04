package chl.ancud.dsy2204_sinoir.modelo

// Frase frecuente de un usuario (pantalla Hablar).
// Equivale a la tabla: frases_frecuentes_xUsuario [id, idUsuario, frase]
data class FraseFrecuente(
    val id: String = "",
    val idUsuario: String = "",
    val frase: String = ""
)
