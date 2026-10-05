package chl.ancud.dsy2204_sinoir.datos

import chl.ancud.dsy2204_sinoir.modelo.Usuario
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

// Repositorio de usuarios con FIREBASE.
// - Firebase Authentication: crea las cuentas y valida correo/contraseña.
// - Firestore: guarda el resto de los datos en la colección "usuarios".
//   El id de cada documento es el mismo uid que entrega Authentication.
//
// Las funciones son las mismas que en la versión en memoria: reciben una
// lambda "alTerminar" que se ejecuta cuando Firebase responde.
object RepositorioUsuarios {

    private const val COLECCION_USUARIOS = "usuarios"

    // Firebase.auth y Firebase.firestore son extensiones de Kotlin (antes "KTX")
    private val autenticacion = Firebase.auth
    private val baseDatos = Firebase.firestore

    // Usuario que tiene la sesión iniciada (null si no hay sesión)
    var usuarioActual: Usuario? = null
        private set

    init {
        // Los correos que envía Firebase (recuperar contraseña) llegan en español
        autenticacion.setLanguageCode("es")
    }

    // CREATE: crea la cuenta en Authentication y guarda los datos en Firestore.
    // Devuelve un mensaje de error, o null si salió bien.
    fun registrar(usuario: Usuario, contrasena: String, alTerminar: (String?) -> Unit) {
        autenticacion.createUserWithEmailAndPassword(usuario.correo, contrasena)
            .addOnSuccessListener { resultado ->
                val uid = resultado.user?.uid
                if (uid == null) {
                    alTerminar("No se pudo crear la cuenta")
                    return@addOnSuccessListener
                }
                val usuarioNuevo = usuario.copy(id = uid)
                baseDatos.collection(COLECCION_USUARIOS).document(uid).set(usuarioNuevo)
                    .addOnSuccessListener {
                        // Firebase deja la sesión iniciada al crear la cuenta.
                        // Se cierra para que el usuario ingrese desde el Login.
                        autenticacion.signOut()
                        alTerminar(null)
                    }
                    .addOnFailureListener {
                        alTerminar("La cuenta se creó, pero no se pudieron guardar tus datos")
                    }
            }
            .addOnFailureListener { error ->
                alTerminar(traducirError(error))
            }
    }

    // READ: inicia sesión y luego trae los datos del usuario desde Firestore.
    // Devuelve el usuario, o null si algo falló.
    fun iniciarSesion(correo: String, contrasena: String, alTerminar: (Usuario?) -> Unit) {
        autenticacion.signInWithEmailAndPassword(correo, contrasena)
            .addOnSuccessListener { resultado ->
                val uid = resultado.user?.uid
                if (uid == null) {
                    alTerminar(null)
                    return@addOnSuccessListener
                }
                baseDatos.collection(COLECCION_USUARIOS).document(uid).get()
                    .addOnSuccessListener { documento ->
                        val usuario = documento.toObject(Usuario::class.java)
                        usuarioActual = usuario
                        alTerminar(usuario)
                    }
                    .addOnFailureListener {
                        alTerminar(null)
                    }
            }
            .addOnFailureListener {
                alTerminar(null)
            }
    }

    // Envía el correo de recuperación de contraseña.
    // Por seguridad, Firebase NO avisa si el correo existe o no.
    fun recuperarContrasena(correo: String, alTerminar: (String?) -> Unit) {
        autenticacion.sendPasswordResetEmail(correo)
            .addOnSuccessListener { alTerminar(null) }
            .addOnFailureListener { error -> alTerminar(traducirError(error)) }
    }

    // UPDATE: guarda los cambios del usuario en Firestore
    fun actualizar(usuario: Usuario, alTerminar: (String?) -> Unit) {
        baseDatos.collection(COLECCION_USUARIOS).document(usuario.id).set(usuario)
            .addOnSuccessListener {
                usuarioActual = usuario
                alTerminar(null)
            }
            .addOnFailureListener {
                alTerminar("No se pudieron guardar los cambios")
            }
    }

    // DELETE: elimina la cuenta y todos sus datos.
    // Firebase exige un inicio de sesión reciente para borrar una cuenta,
    // por eso se pide la contraseña y se vuelve a autenticar primero.
    // Orden: 1) reautenticar  2) borrar mensajes  3) borrar frases
    //        4) borrar documento del usuario  5) borrar la cuenta
    fun eliminarCuenta(contrasena: String, alTerminar: (String?) -> Unit) {
        val cuenta = autenticacion.currentUser
        val usuario = usuarioActual
        if (cuenta == null || usuario == null) {
            alTerminar("No hay una sesión iniciada")
            return
        }

        val credencial = EmailAuthProvider.getCredential(usuario.correo, contrasena)
        cuenta.reauthenticate(credencial)
            .addOnSuccessListener {
                RepositorioMensajes.eliminarTodosDelUsuario(usuario.id) {
                    RepositorioFrases.eliminarTodasDelUsuario(usuario.id) {
                        baseDatos.collection(COLECCION_USUARIOS).document(usuario.id).delete()
                            .addOnSuccessListener {
                                cuenta.delete()
                                    .addOnSuccessListener {
                                        usuarioActual = null
                                        alTerminar(null)
                                    }
                                    .addOnFailureListener {
                                        alTerminar("No se pudo eliminar la cuenta")
                                    }
                            }
                            .addOnFailureListener {
                                alTerminar("No se pudieron borrar tus datos")
                            }
                    }
                }
            }
            .addOnFailureListener {
                alTerminar("La contraseña no es correcta")
            }
    }

    fun cerrarSesion() {
        autenticacion.signOut()
        usuarioActual = null
    }

    // Convierte los errores de Firebase en mensajes entendibles para el usuario.
    // "when" con "is" revisa de qué tipo es el error.
    private fun traducirError(error: Exception): String {
        return when (error) {
            is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con ese correo"
            is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil"
            is FirebaseAuthInvalidCredentialsException -> "El correo no tiene un formato válido"
            is FirebaseNetworkException -> "No hay conexión a internet"
            else -> "Ocurrió un error, intenta de nuevo"
        }
    }
}
