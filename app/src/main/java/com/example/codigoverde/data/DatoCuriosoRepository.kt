package com.example.codigoverde.data

import android.content.Context
import com.example.codigoverde.model.DatoCurioso
import org.json.JSONArray
import org.json.JSONException

/**
 * Carga los datos curiosos desde assets/datos_curiosos.json.
 * El nombre del plato ("plato") coincide con [com.example.codigoverde.model.Alimento.opcion].
 */
object DatoCuriosoRepository {

    private const val ARCHIVO = "datos_curiosos.json"

    // Cache en memoria: el archivo es chico y no cambia durante la ejecución
    private var cache: Map<String, DatoCurioso>? = null

    /** Devuelve los datos indexados por nombre de plato. Nunca lanza: ante un error devuelve un mapa vacío. */
    fun cargarDatos(context: Context): Map<String, DatoCurioso> {
        cache?.let { return it }

        val datos = try {
            val json = context.assets.open(ARCHIVO).bufferedReader(Charsets.UTF_8).use { it.readText() }
            parsear(json)
        } catch (e: java.io.IOException) {
            emptyMap()
        }
        cache = datos
        return datos
    }

    /** Parsea el contenido JSON. Ignora entradas incompletas. Separado de [cargarDatos] para poder testearlo. */
    fun parsear(json: String): Map<String, DatoCurioso> {
        return try {
            val array = JSONArray(json)
            val mapa = LinkedHashMap<String, DatoCurioso>()
            for (i in 0 until array.length()) {
                val item = array.optJSONObject(i) ?: continue
                val plato = item.optString("plato").trim()
                val dato = item.optString("dato").trim()
                if (plato.isNotEmpty() && dato.isNotEmpty()) {
                    mapa[plato] = DatoCurioso(plato, dato)
                }
            }
            mapa
        } catch (e: JSONException) {
            emptyMap()
        }
    }
}
