package chl.ancud.dsy2204_sinoir.modelo

// Datos del usuario.
// La contraseña ya NO se guarda aquí: en la fase con Firebase la maneja
// Firebase Authentication (cifrada), y guardarla también acá sería inseguro.
// Los valores por defecto ("") son necesarios para que Firestore pueda
// convertir los documentos en objetos Usuario más adelante.
data class Usuario(
    val id: String = "",
    val nombreUsuario: String = "",
    val correo: String = "",
    val tipoDiscapacidad: String = "",
    val tipoComunicacion: String = ""
)
