package chl.ancud.dsy2204_sinoir.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Convierte una fecha en milisegundos a texto, por ejemplo "03/10/2026 18:45"
fun formatearFecha(milisegundos: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.format(Date(milisegundos))
}
