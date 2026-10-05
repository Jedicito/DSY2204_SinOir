package chl.ancud.dsy2204_sinoir.datos

import chl.ancud.dsy2204_sinoir.modelo.Mensaje
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

// Repositorio de mensajes con FIRESTORE.
// Colección "mensajes_xUsuario": cada documento es un mensaje con el
// idUsuario de su dueño (como una tabla con llave foránea).
// Guarda lo que se escucha en "Escribir" y lo que se dice en "Hablar".
object RepositorioMensajes {

    private const val COLECCION_MENSAJES = "mensajes_xUsuario"

    private val baseDatos = Firebase.firestore

    // CREATE: se llama solo, cada vez que se reconoce o se dice un mensaje.
    // document() sin parámetros crea un id nuevo automáticamente.
    fun agregar(mensaje: Mensaje, alTerminar: (Boolean) -> Unit = {}) {
        val referencia = baseDatos.collection(COLECCION_MENSAJES).document()
        referencia.set(mensaje.copy(id = referencia.id))
            .addOnSuccessListener { alTerminar(true) }
            .addOnFailureListener { alTerminar(false) }
    }

    // READ: mensajes de un usuario, del más nuevo al más antiguo.
    // whereEqualTo funciona como un WHERE idUsuario = ...
    // El orden se hace aquí con sortedByDescending, así Firestore
    // no necesita un índice especial.
    fun obtenerPorUsuario(idUsuario: String, alTerminar: (List<Mensaje>) -> Unit) {
        baseDatos.collection(COLECCION_MENSAJES)
            .whereEqualTo("idUsuario", idUsuario)
            .get()
            .addOnSuccessListener { resultado ->
                val mensajes = resultado.toObjects(Mensaje::class.java)
                    .sortedByDescending { it.fecha }
                alTerminar(mensajes)
            }
            .addOnFailureListener {
                alTerminar(emptyList())
            }
    }

    // DELETE: borra un mensaje del historial
    fun eliminar(idMensaje: String, alTerminar: (Boolean) -> Unit) {
        baseDatos.collection(COLECCION_MENSAJES).document(idMensaje).delete()
            .addOnSuccessListener { alTerminar(true) }
            .addOnFailureListener { alTerminar(false) }
    }

    // Se usa al eliminar la cuenta: busca todos los mensajes del usuario
    // y los borra juntos en un "lote" (batch)
    fun eliminarTodosDelUsuario(idUsuario: String, alTerminar: () -> Unit) {
        baseDatos.collection(COLECCION_MENSAJES)
            .whereEqualTo("idUsuario", idUsuario)
            .get()
            .addOnSuccessListener { resultado ->
                val lote = baseDatos.batch()
                for (documento in resultado.documents) {
                    lote.delete(documento.reference)
                }
                lote.commit().addOnCompleteListener { alTerminar() }
            }
            .addOnFailureListener {
                alTerminar()
            }
    }
}
