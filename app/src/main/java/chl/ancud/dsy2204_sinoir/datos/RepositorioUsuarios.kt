package chl.ancud.dsy2204_sinoir.datos

import chl.ancud.dsy2204_sinoir.modelo.Usuario
import java.util.UUID

// Repositorio de usuarios EN MEMORIA (fase 1, antes de Firebase).
//
// Todas las funciones reciben una lambda "alTerminar" en vez de devolver
// el resultado directo. Así es como funciona Firebase: la respuesta llega
// después, cuando el servidor contesta. Cuando cambiemos este archivo por
// la versión con Firebase, las pantallas casi no van a cambiar.
object RepositorioUsuarios {

    private val listaUsuarios = mutableListOf<Usuario>()

    // Simula a Firebase Authentication: guarda correo -> contraseña,
    // separado de los datos del usuario
    private val contrasenas = mutableMapOf<String, String>()

    // Usuario que tiene la sesión iniciada (null si no hay sesión)
    var usuarioActual: Usuario? = null
        private set

    // Usuario de prueba para no tener que registrarse cada vez.
    // Desaparece en la fase con Firebase.
    init {
        val usuarioPrueba = Usuario(
            id = "usuario_prueba",
            nombreUsuario = "cesar",
            correo = "cesar@sinoir.cl",
            tipoDiscapacidad = "Sordera total",
            tipoComunicacion = "Lengua de señas"
        )
        listaUsuarios.add(usuarioPrueba)
        contrasenas[usuarioPrueba.correo] = "123456"
    }

    // CREATE: registra un usuario nuevo. Devuelve un mensaje de error, o null si salió bien
    fun registrar(usuario: Usuario, contrasena: String, alTerminar: (String?) -> Unit) {
        val correoOcupado = listaUsuarios.any { it.correo == usuario.correo }
        if (correoOcupado) {
            alTerminar("Ya existe una cuenta con ese correo")
            return
        }
        val usuarioNuevo = usuario.copy(id = UUID.randomUUID().toString())
        listaUsuarios.add(usuarioNuevo)
        contrasenas[usuarioNuevo.correo] = contrasena
        alTerminar(null)
    }

    // READ: inicia sesión. Devuelve el usuario encontrado, o null si los datos no coinciden
    fun iniciarSesion(correo: String, contrasena: String, alTerminar: (Usuario?) -> Unit) {
        val usuarioEncontrado = listaUsuarios.find { it.correo == correo }
        if (usuarioEncontrado != null && contrasenas[correo] == contrasena) {
            usuarioActual = usuarioEncontrado
            alTerminar(usuarioEncontrado)
        } else {
            alTerminar(null)
        }
    }

    // En memoria no se puede enviar un correo de verdad, solo se revisa que exista.
    // Con Firebase, aquí se enviará el correo real de recuperación.
    fun recuperarContrasena(correo: String, alTerminar: (String?) -> Unit) {
        val existe = listaUsuarios.any { it.correo == correo }
        if (existe) {
            alTerminar(null)
        } else {
            alTerminar("No encontramos una cuenta con ese correo")
        }
    }

    // UPDATE: actualiza los datos del usuario
    fun actualizar(usuario: Usuario, alTerminar: (String?) -> Unit) {
        val posicion = listaUsuarios.indexOfFirst { it.id == usuario.id }
        if (posicion == -1) {
            alTerminar("No se encontró el usuario")
            return
        }
        listaUsuarios[posicion] = usuario
        usuarioActual = usuario
        alTerminar(null)
    }

    // DELETE: elimina la cuenta del usuario actual junto con sus mensajes y frases
    fun eliminarCuenta(alTerminar: (String?) -> Unit) {
        val usuario = usuarioActual
        if (usuario == null) {
            alTerminar("No hay una sesión iniciada")
            return
        }
        RepositorioMensajes.eliminarTodosDelUsuario(usuario.id)
        RepositorioFrases.eliminarTodasDelUsuario(usuario.id)
        listaUsuarios.removeAll { it.id == usuario.id }
        contrasenas.remove(usuario.correo)
        usuarioActual = null
        alTerminar(null)
    }

    fun cerrarSesion() {
        usuarioActual = null
    }
}
