package chl.ancud.dsy2204_sinoir.datos

import chl.ancud.dsy2204_sinoir.modelo.FraseFrecuente
import java.util.UUID

// Repositorio de frases frecuentes EN MEMORIA (fase 1, antes de Firebase).
// Tiene el CRUD completo: agregar, obtener, actualizar y eliminar.
object RepositorioFrases {

    private val listaFrases = mutableListOf<FraseFrecuente>()

    // Frases de ejemplo para el usuario de prueba
    init {
        val frasesDePrueba = listOf(
            "Soy sordo, por favor escríbame",
            "Necesito ayuda",
            "¿Me puede repetir más despacio?"
        )
        frasesDePrueba.forEach { texto ->
            listaFrases.add(
                FraseFrecuente(
                    id = UUID.randomUUID().toString(),
                    idUsuario = "usuario_prueba",
                    frase = texto
                )
            )
        }
    }

    // CREATE
    fun agregar(frase: FraseFrecuente, alTerminar: (Boolean) -> Unit) {
        val fraseNueva = frase.copy(id = UUID.randomUUID().toString())
        listaFrases.add(fraseNueva)
        alTerminar(true)
    }

    // READ
    fun obtenerPorUsuario(idUsuario: String, alTerminar: (List<FraseFrecuente>) -> Unit) {
        val frasesDelUsuario = listaFrases.filter { it.idUsuario == idUsuario }
        alTerminar(frasesDelUsuario)
    }

    // UPDATE
    fun actualizar(frase: FraseFrecuente, alTerminar: (Boolean) -> Unit) {
        val posicion = listaFrases.indexOfFirst { it.id == frase.id }
        if (posicion == -1) {
            alTerminar(false)
            return
        }
        listaFrases[posicion] = frase
        alTerminar(true)
    }

    // DELETE
    fun eliminar(idFrase: String, alTerminar: (Boolean) -> Unit) {
        val seElimino = listaFrases.removeAll { it.id == idFrase }
        alTerminar(seElimino)
    }

    // Se usa al eliminar la cuenta
    fun eliminarTodasDelUsuario(idUsuario: String) {
        listaFrases.removeAll { it.idUsuario == idUsuario }
    }
}
