package com.upchiapas.kt_template.pesito.presentation.screens

import com.upchiapas.kt_template.pesito.data.model.CategoriaGasto
import com.upchiapas.kt_template.pesito.data.model.Gasto

data class PesitoUiState(
    val isLoading: Boolean = true,
    val limiteMensualGlobal: Double = 2000.0,
    val totalGastado: Double = 0.0,
    val saldoDisponible: Double = 0.0,
    val porcentajeConsumido: Float = 0f,
    val categorias: List<CategoriaGasto> = emptyList(),
    val categoriaSeleccionadaId: String? = null,
    val listaGastos: List<Gasto> = emptyList(),
    val gastoMayorConsumo: String = "N/A",
    val montoMayorConsumo: Double = 0.0
)
