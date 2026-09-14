package chl.ancud.dsy2204_sinoir.datos

import chl.ancud.dsy2204_sinoir.modelo.Usuario

// Repositorio simple que guarda a los usuarios en un arreglo de tamaño 5,
object RepositorioUsuarios {

    val listaUsuarios = arrayOfNulls<Usuario>(5)

    // Cantidad de usuarios que ya se registraron
    private var cantidadRegistrados = 0

    // Se deja un usuario de prueba para poder probar el login sin
    // tener que registrarse primero
    init {
        listaUsuarios[0] = Usuario(
            nombreUsuario = "cesar",
            correo = "cesar@correo.com",
            contrasena = "12345",
            tipoComunicacion = "Lengua de señas"
        )
        cantidadRegistrados = 1
    }

    // Agrega un usuario nuevo al arreglo si todavía queda espacio
    fun agregarUsuario(usuario: Usuario): Boolean {
        if (cantidadRegistrados >= listaUsuarios.size) {
            return false
        }
        listaUsuarios[cantidadRegistrados] = usuario
        cantidadRegistrados++
        return true
    }

    // Busca un usuario según su nombre de usuario y contraseña (para el login)
    fun buscarUsuario(nombreUsuario: String, contrasena: String): Usuario? {
        for (usuario in listaUsuarios) {
            if (usuario != null &&
                usuario.nombreUsuario == nombreUsuario &&
                usuario.contrasena == contrasena
            ) {
                return usuario
            }
        }
        return null
    }

    // Busca un usuario por su correo (para recuperar contraseña)
    fun buscarUsuarioPorCorreo(correo: String): Usuario? {
        for (usuario in listaUsuarios) {
            if (usuario != null && usuario.correo == correo) {
                return usuario
            }
        }
        return null
    }

    // Devuelve la lista de usuarios que ya están registrados (sin los espacios vacíos)
    fun obtenerUsuariosRegistrados(): List<Usuario> {
        return listaUsuarios.filterNotNull()
    }

    fun quedanCupos(): Boolean {
        return cantidadRegistrados < listaUsuarios.size
    }
}
