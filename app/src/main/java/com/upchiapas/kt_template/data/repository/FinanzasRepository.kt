package com.upchiapas.kt_template.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.upchiapas.kt_template.data.local.LocalAssetDataSource
import com.upchiapas.kt_template.data.model.CategoriaGasto
import com.upchiapas.kt_template.data.model.Gasto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class FinanzasRepository ( private val context: Context) {
    private val assetDataSource = LocalAssetDataSource(context)
    private val pref: SharedPreferences= context.getSharedPreferences("finanzas_prefs", Context.MODE_PRIVATE)

    private val _gastoFlow = MutableStateFlow<List<Gasto>>(emptyList())
    val gastoFlow: StateFlow<List<Gasto>> = _gastoFlow.asStateFlow()

    private val _limiteFlow = MutableStateFlow(2000.0)
    val limiteFlow: StateFlow<Double> = _limiteFlow.asStateFlow()

    init {
        cargarLimitePersistido()
        cargarGastosPersistidos()
    }

    fun obtenerCategoriasSemilla(): List<CategoriaGasto> {
        return assetDataSource.cargarCategoriasSemilla()
    }

    fun guardarGasto(nuevoGasto: Gasto) {
        val listaActual = _gastoFlow.value.toMutableList()
        val index = listaActual.indexOfFirst { it.id == nuevoGasto.id }
        if (index != -1) {
            listaActual[index] = nuevoGasto
        }else{
            listaActual.add(0, nuevoGasto)
        }
        _gastoFlow.value = listaActual
        persistirGasto(listaActual)
    }

    fun eliminarGasto(gastoId: String) {
        val listaActual = _gastoFlow.value.filterNot { it.id == gastoId }
        _gastoFlow.value = listaActual
        persistirGasto(listaActual)
    }

    fun actualizarLimiteMensual(nuevoLimite: Double) {
        _limiteFlow.value = nuevoLimite
        pref.edit().putFloat("limite_mensual", nuevoLimite.toFloat()).apply()
    }

    private fun cargarLimitePersistido() {
        val limiteGuardado = pref.getFloat("limite_mensual", 2000.0f).toDouble()
        _limiteFlow.value = limiteGuardado
    }

    private fun persistirGasto(lista: List<Gasto>) {
        val jsonArray = JSONArray()
        lista.forEach { gasto ->
            val obj = JSONObject().apply {
                put("id", gasto.id)
                put("concepto", gasto.concepto)
                put("monto", gasto.monto)
                put("categoriaId", gasto.categoriaId)
                put("metodoPago", gasto.metodoPago)
                put("fechaMs", gasto.fechaMs)
            }
            jsonArray.put(obj)
        }
        pref.edit().putString("gasto_json", jsonArray.toString()).apply()
    }

    private fun cargarGastosPersistidos() {
        val jsonString = pref.getString("gasto_json", null) ?: return
        val lista = mutableListOf<Gasto>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                lista.add(
                    Gasto(
                        id = obj.getString("id"),
                        concepto = obj.getString("concepto"),
                        monto = obj.getDouble("monto"),
                        categoriaId = obj.getString("categoriaId"),
                        metodoPago = obj.optString("metodoPago", "Efectivo"),
                        fechaMs = obj.getLong("fechaMs")
                    )
                )
            }
            _gastoFlow.value = lista
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}