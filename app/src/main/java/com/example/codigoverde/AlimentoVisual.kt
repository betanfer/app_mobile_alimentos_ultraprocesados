package com.example.codigoverde

import android.content.Context

/**
 * Asocia cada alimento (por su número de fila en menu_alimentos_puntuado.txt)
 * con su imagen en res/drawable y un nombre corto para mostrar en la tarjeta.
 * Si la imagen todavía no existe, se usa el logo como imagen de reemplazo.
 */
object AlimentoVisual {

    private val datos = mapOf(
        1 to ("img_01_desayuno_tostadas_palta" to "Tostadas con palta"),
        2 to ("img_02_desayuno_porridge_avena" to "Porridge de avena"),
        3 to ("img_03_desayuno_yogur_griego" to "Yogur griego"),
        4 to ("img_04_desayuno_omelette_claras" to "Omelette de claras"),
        5 to ("img_05_desayuno_licuado_verde" to "Licuado verde"),
        6 to ("img_06_desayuno_medialunas_dulce_leche" to "Medialunas c/ dulce de leche"),
        7 to ("img_07_desayuno_donas_glaseadas" to "Donas glaseadas"),
        8 to ("img_08_desayuno_tostado_jamon_queso" to "Tostado jamón y queso"),
        9 to ("img_09_desayuno_panqueques_jarabe" to "Panqueques con jarabe"),
        10 to ("img_10_desayuno_cereales_azucarados" to "Cereales azucarados"),
        11 to ("img_11_almuerzo_pollo_ensalada" to "Pollo con ensalada"),
        12 to ("img_12_almuerzo_merluza_calabaza" to "Merluza con calabaza"),
        13 to ("img_13_almuerzo_ensalada_legumbres" to "Ensalada de legumbres"),
        14 to ("img_14_almuerzo_quinoa_brocoli" to "Bowl de quinoa"),
        15 to ("img_15_almuerzo_budin_espinaca" to "Budín de espinaca"),
        16 to ("img_16_almuerzo_hamburguesa_papas" to "Hamburguesa con papas"),
        17 to ("img_17_almuerzo_milanesa_napolitana" to "Milanesa napolitana"),
        18 to ("img_18_almuerzo_ravioles_estofado" to "Ravioles con estofado"),
        19 to ("img_19_almuerzo_nuggets_barbacoa" to "Nuggets con barbacoa"),
        20 to ("img_20_almuerzo_sandwich_milanesa" to "Sándwich de milanesa"),
        21 to ("img_21_merienda_fruta_mantequilla_mani" to "Fruta con pasta de maní"),
        22 to ("img_22_merienda_muffin_avena_banana" to "Muffin de avena"),
        23 to ("img_23_merienda_galletas_arroz_queso" to "Galletas de arroz"),
        24 to ("img_24_merienda_frutillas_nueces" to "Frutillas con nueces"),
        25 to ("img_25_merienda_hummus_bastones_vegetales" to "Hummus con vegetales"),
        26 to ("img_26_merienda_torta_chocolate" to "Torta de chocolate"),
        27 to ("img_27_merienda_galletitas_rellenas" to "Galletitas rellenas"),
        28 to ("img_28_merienda_bizcochitos_mate" to "Bizcochitos con mate"),
        29 to ("img_29_merienda_alfajor_triple" to "Alfajor triple"),
        30 to ("img_30_merienda_churros_dulce_leche" to "Churros"),
        31 to ("img_31_cena_wok_lomo_vegetales" to "Wok de lomo"),
        32 to ("img_32_cena_salmon_esparragos" to "Salmón con espárragos"),
        33 to ("img_33_cena_calabaza_rellena" to "Calabaza rellena"),
        34 to ("img_34_cena_sopa_crema_zapallo" to "Sopa crema de zapallo"),
        35 to ("img_35_cena_tortilla_zapallitos" to "Tortilla de zapallitos"),
        36 to ("img_36_cena_pizza_muzzarella" to "Pizza de muzzarella"),
        37 to ("img_37_cena_empanadas_fritas" to "Empanadas fritas"),
        38 to ("img_38_cena_papas_cheddar" to "Papas con cheddar"),
        39 to ("img_39_cena_tacos" to "Tacos"),
        40 to ("img_40_cena_panchos" to "Panchos")
    )

    fun imagenRes(context: Context, alimento: Alimento): Int {
        val nombre = datos[alimento.numero]?.first ?: return R.drawable.logo_codigo_verde
        val id = context.resources.getIdentifier(nombre, "drawable", context.packageName)
        return if (id != 0) id else R.drawable.logo_codigo_verde
    }

    fun nombreCorto(alimento: Alimento): String =
        datos[alimento.numero]?.second ?: alimento.opcion
}
