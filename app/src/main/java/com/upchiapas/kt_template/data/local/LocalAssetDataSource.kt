package com.upchiapas.kt_template.data.local

import android.content.Context
import com.upchiapas.kt_template.R
import com.upchiapas.kt_template.data.model.CategoriaGasto
import org.json.JSONArray

class LocalAssetDataSource(private val context: Context) {

    fun cargarCategoriasSemilla(): List<CategoriaGasto> {
        val categoriasList = mutableListOf<CategoriaGasto>()
        try {
            val inputStream = context.resources.openRawResource(R.raw.categorias_base)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                categoriasList.add(
                    CategoriaGasto(
                        id = obj.getString("id"),
                        nombre = obj.getString("nombre"),
                        limitePresupuesto = obj.getDouble("limitePresupuesto")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return categoriasList
    }
}