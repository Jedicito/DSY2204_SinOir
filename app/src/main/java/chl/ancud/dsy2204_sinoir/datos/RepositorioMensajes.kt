package chl.ancud.dsy2204_sinoir.datos

import chl.ancud.dsy2204_sinoir.modelo.Mensaje
import java.util.UUID

// Repositorio de mensajes EN MEMORIA (fase 1, antes de Firebase).
// Guarda lo que se escucha en "Escribir" y lo que se dice en "Hablar".
object RepositorioMensajes {

    private val listaMensajes = mutableListOf<Mensaje>()

    // CREATE: se llama solo, cada vez que se reconoce o se dice un mensaje
    fun agregar(mensaje: Mensaje, alTerminar: (Boolean) -> Unit = {}) {
        val mensajeNuevo = mensaje.copy(id = UUID.randomUUID().toString())
        listaMensajes.add(mensajeNuevo)
        alTerminar(true)
    }

    // READ: mensajes de un usuario, del más nuevo al más antiguo
    fun obtenerPorUsuario(idUsuario: String, alTerminar: (List<Mensaje>) -> Unit) {
        val mensajesDelUsuario = listaMensajes
            .filter { it.idUsuario == idUsuario }
            .sortedByDescending { it.fecha }
        alTerminar(mensajesDelUsuario)
    }

    // DELETE: borra un mensaje del historial
    fun eliminar(idMensaje: String, alTerminar: (Boolean) -> Unit) {
        val seElimino = listaMensajes.removeAll { it.id == idMensaje }
        alTerminar(seElimino)
    }

    // Se usa al eliminar la cuenta
    fun eliminarTodosDelUsuario(idUsuario: String) {
        listaMensajes.removeAll { it.idUsuario == idUsuario }
    }
}
