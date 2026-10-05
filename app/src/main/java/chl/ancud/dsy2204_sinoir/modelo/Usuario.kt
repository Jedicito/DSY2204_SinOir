package chl.ancud.dsy2204_sinoir.modelo

// Datos del usuario.
// La contraseña ya NO se guarda aquí: en la fase con Firebase la maneja
// Firebase Authentication (cifrada), y guardarla también acá sería inseguro.
// Para que Firestore pueda convertir un documento en un objeto Usuario,
// los campos deben ser "var" y tener valores por defecto ("").
data class Usuario(
    var id: String = "",
    var nombreUsuario: String = "",
    var correo: String = "",
    var tipoDiscapacidad: String = "",
    var tipoComunicacion: String = ""
)
