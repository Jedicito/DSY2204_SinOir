package chl.ancud.dsy2204_sinoir.datos

import chl.ancud.dsy2204_sinoir.modelo.FraseFrecuente
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

// Repositorio de frases frecuentes con FIRESTORE.
// Colección "frases_frecuentes_xUsuario": cada documento es una frase
// con el idUsuario de su dueño.
// Tiene el CRUD completo: agregar, obtener, actualizar y eliminar.
object RepositorioFrases {

    private const val COLECCION_FRASES = "frases_frecuentes_xUsuario"

    private val baseDatos = Firebase.firestore

    // CREATE
    fun agregar(frase: FraseFrecuente, alTerminar: (Boolean) -> Unit) {
        val referencia = baseDatos.collection(COLECCION_FRASES).document()
        referencia.set(frase.copy(id = referencia.id))
            .addOnSuccessListener { alTerminar(true) }
            .addOnFailureListener { alTerminar(false) }
    }

    // READ
    fun obtenerPorUsuario(idUsuario: String, alTerminar: (List<FraseFrecuente>) -> Unit) {
        baseDatos.collection(COLECCION_FRASES)
            .whereEqualTo("idUsuario", idUsuario)
            .get()
            .addOnSuccessListener { resultado ->
                alTerminar(resultado.toObjects(FraseFrecuente::class.java))
            }
            .addOnFailureListener {
                alTerminar(emptyList())
            }
    }

    // UPDATE
    fun actualizar(frase: FraseFrecuente, alTerminar: (Boolean) -> Unit) {
        baseDatos.collection(COLECCION_FRASES).document(frase.id).set(frase)
            .addOnSuccessListener { alTerminar(true) }
            .addOnFailureListener { alTerminar(false) }
    }

    // DELETE
    fun eliminar(idFrase: String, alTerminar: (Boolean) -> Unit) {
        baseDatos.collection(COLECCION_FRASES).document(idFrase).delete()
            .addOnSuccessListener { alTerminar(true) }
            .addOnFailureListener { alTerminar(false) }
    }

    // Se usa al eliminar la cuenta: borra todas las frases del usuario en un lote
    fun eliminarTodasDelUsuario(idUsuario: String, alTerminar: () -> Unit) {
        baseDatos.collection(COLECCION_FRASES)
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
