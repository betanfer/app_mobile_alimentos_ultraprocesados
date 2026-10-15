package com.example.codigoverde

import com.example.codigoverde.data.DatoCuriosoRepository
import com.example.codigoverde.model.Alimento
import com.example.codigoverde.model.DatoCurioso
import com.example.codigoverde.viewmodel.ResultadoViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class DatoCuriosoTest {

    private fun alimento(opcion: String) = Alimento(
        comida = "Desayuno", tipo = "Saludable", opcion = opcion,
        nivelSaludable = 2, saciedad = 2, vitalidad = 2
    )

    private val datos = mapOf(
        "Plato A" to DatoCurioso("Plato A", "Dato A"),
        "Plato B" to DatoCurioso("Plato B", "Dato B")
    )

    @Test
    fun parsear_leeEntradasValidasEIgnoraIncompletas() {
        val json = """
            [
              {"plato": "Plato A", "dato": "Dato A con acentos: ñ á"},
              {"plato": "Sin dato"},
              {"dato": "Sin plato"},
              {"plato": "Plato B", "dato": "Dato B"}
            ]
        """.trimIndent()

        val mapa = DatoCuriosoRepository.parsear(json)

        assertEquals(2, mapa.size)
        assertEquals("Dato A con acentos: ñ á", mapa["Plato A"]?.dato)
        assertEquals("Dato B", mapa["Plato B"]?.dato)
    }

    @Test
    fun parsear_jsonInvalidoDevuelveMapaVacio() {
        assertTrue(DatoCuriosoRepository.parsear("esto no es json").isEmpty())
    }

    @Test
    fun elegir_devuelveDatoDeUnAlimentoElegido() {
        val elegidos = listOf(alimento("Plato A"), alimento("Plato B"))
        repeat(20) { semilla ->
            val dato = ResultadoViewModel.elegirDatoCurioso(elegidos, datos, Random(semilla))
            assertNotNull(dato)
            assertTrue(dato!!.plato in setOf("Plato A", "Plato B"))
        }
    }

    @Test
    fun elegir_noDevuelveDatosDeAlimentosNoElegidos() {
        val elegidos = listOf(alimento("Plato A"))
        repeat(20) { semilla ->
            assertEquals("Plato A", ResultadoViewModel.elegirDatoCurioso(elegidos, datos, Random(semilla))?.plato)
        }
    }

    @Test
    fun elegir_ignoraAlimentosSinDato() {
        val elegidos = listOf(alimento("Plato inexistente"), alimento("Plato B"))
        assertEquals("Plato B", ResultadoViewModel.elegirDatoCurioso(elegidos, datos, Random(1))?.plato)
    }

    @Test
    fun elegir_sinAlimentosDevuelveNull() {
        assertNull(ResultadoViewModel.elegirDatoCurioso(emptyList(), datos))
    }
}
