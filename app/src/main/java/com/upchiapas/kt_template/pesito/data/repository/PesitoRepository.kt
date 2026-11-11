package com.upchiapas.kt_template.pesito.data.repository

import com.upchiapas.kt_template.pesito.data.model.CategoriaGasto
import com.upchiapas.kt_template.pesito.data.model.Gasto
import kotlinx.coroutines.flow.StateFlow

interface PesitoRepository {
    val gastosFlow: StateFlow<List<Gasto>>
    val limiteFlow: StateFlow<Double>
    fun obtenerCategoriasSemilla(): List<CategoriaGasto>
    fun guardarGasto(nuevoGasto: Gasto)
    fun eliminarGasto(gastoId: String)
    fun actualizarLimiteMensual(nuevoLimite: Double)
}
