package chl.ancud.dsy2204_sinoir.utils

// Valida el formato de un correo: que no esté vacío.
// Que tenga un @ y al menos un punto después del @.
fun correoEsValido(correo: String): Boolean {
    return correo.contains("@") && correo.substringAfter("@").contains(".")
}

// Valida datos del registro. Devuelve un mensaje de error si falla.
// Null si está correcto.
fun validarRegistro(
    nombreUsuario: String,
    correo: String,
    contrasena: String,
    confirmarContrasena: String,
    aceptaTerminos: Boolean
): String? {
    if (nombreUsuario == "" || correo == "" || contrasena == "") {
        return "Debes completar todos los campos"
    }
    if (!correoEsValido(correo)) {
        return "El correo no tiene un formato válido"
    }
    if (contrasena != confirmarContrasena) {
        return "Las contraseñas no coinciden"
    }
    if (!aceptaTerminos) {
        return "Debes aceptar los términos y condiciones"
    }
    return null
}