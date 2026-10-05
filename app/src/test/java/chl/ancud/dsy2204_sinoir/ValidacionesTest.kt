package chl.ancud.dsy2204_sinoir

import chl.ancud.dsy2204_sinoir.utils.contrasenaEsValida
import chl.ancud.dsy2204_sinoir.utils.correoEsValido
import chl.ancud.dsy2204_sinoir.utils.validarRegistro
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// Pruebas unitarias de las funciones de Validaciones.kt.
// Se ejecutan en el computador, sin emulador:
// clic derecho sobre este archivo > Run 'ValidacionesTest'
class ValidacionesTest {

    @Test
    fun correoConFormatoCorrecto_esValido() {
        assertTrue(correoEsValido("cesar@sinoir.cl"))
    }

    @Test
    fun correoSinArroba_noEsValido() {
        assertFalse(correoEsValido("cesar.sinoir.cl"))
    }

    @Test
    fun contrasenaDeMenosDeSeisCaracteres_noEsValida() {
        assertFalse(contrasenaEsValida("12345"))
    }

    @Test
    fun registroConDatosCorrectos_noDevuelveError() {
        val error = validarRegistro(
            nombreUsuario = "cesar",
            correo = "cesar@sinoir.cl",
            contrasena = "123456",
            confirmarContrasena = "123456",
            aceptaTerminos = true
        )
        assertNull(error)
    }

    @Test
    fun registroConContrasenasDistintas_devuelveError() {
        val error = validarRegistro(
            nombreUsuario = "cesar",
            correo = "cesar@sinoir.cl",
            contrasena = "123456",
            confirmarContrasena = "654321",
            aceptaTerminos = true
        )
        assertEquals("Las contraseñas no coinciden", error)
    }
}
