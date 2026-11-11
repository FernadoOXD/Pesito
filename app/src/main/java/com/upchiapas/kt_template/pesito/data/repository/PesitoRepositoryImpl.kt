package com.upchiapas.kt_template.pesito.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.upchiapas.kt_template.pesito.data.datasource.PesitoLocalDataSource
import com.upchiapas.kt_template.pesito.data.model.CategoriaGasto
import com.upchiapas.kt_template.pesito.data.model.Gasto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class PesitoRepositoryImpl(
    private val dataSource: PesitoLocalDataSource,
    context: Context
) : PesitoRepository {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("pesito_prefs", Context.MODE_PRIVATE)

    private val _gastosFlow = MutableStateFlow<List<Gasto>>(emptyList())
    override val gastosFlow: StateFlow<List<Gasto>> = _gastosFlow.asStateFlow()

    private val _limiteFlow = MutableStateFlow(2000.0)
    override val limiteFlow: StateFlow<Double> = _limiteFlow.asStateFlow()

    init {
        cargarLimitePersistido()
        cargarGastosPersistidos()
    }

    override fun obtenerCategoriasSemilla(): List<CategoriaGasto> {
        return dataSource.cargarCategoriasSemilla()
    }

    override fun guardarGasto(nuevoGasto: Gasto) {
        val listaActual = _gastosFlow.value.toMutableList()
        val index = listaActual.indexOfFirst { it.id == nuevoGasto.id }
        if (index != -1) {
            listaActual[index] = nuevoGasto
        } else {
            listaActual.add(0, nuevoGasto)
        }
        _gastosFlow.value = listaActual
        persistirGastos(listaActual)
    }

    override fun eliminarGasto(gastoId: String) {
        val listaActual = _gastosFlow.value.filterNot { it.id == gastoId }
        _gastosFlow.value = listaActual
        persistirGastos(listaActual)
    }

    override fun actualizarLimiteMensual(nuevoLimite: Double) {
        _limiteFlow.value = nuevoLimite
        prefs.edit().putFloat("limite_mensual", nuevoLimite.toFloat()).apply()
    }

    private fun cargarLimitePersistido() {
        val limiteGuardado = prefs.getFloat("limite_mensual", 2000.0f).toDouble()
        _limiteFlow.value = limiteGuardado
    }

    private fun persistirGastos(lista: List<Gasto>) {
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
        prefs.edit().putString("gastos_json", jsonArray.toString()).apply()
    }

    private fun cargarGastosPersistidos() {
        val jsonString = prefs.getString("gastos_json", null) ?: return
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
            _gastosFlow.value = lista
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
