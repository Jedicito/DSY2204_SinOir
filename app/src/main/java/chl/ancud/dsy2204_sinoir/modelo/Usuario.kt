package chl.ancud.dsy2204_sinoir.modelo

// Clase de datos que representa a un usuario de la app
data class Usuario(
    val nombreUsuario: String,
    val correo: String,
    val contrasena: String,
    val tipoComunicacion: String
)
