package com.upchiapas.kt_template.pesito.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upchiapas.kt_template.pesito.data.model.Gasto
import com.upchiapas.kt_template.pesito.data.repository.PesitoRepository
import com.upchiapas.kt_template.pesito.presentation.screens.PesitoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PesitoViewModel(
    private val repository: PesitoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PesitoUiState())
    val uiState: StateFlow<PesitoUiState> = _uiState.asStateFlow()

    init {
        val categoriasSemilla = repository.obtenerCategoriasSemilla()
        _uiState.update { it.copy(categorias = categoriasSemilla) }

        viewModelScope.launch {
            combine(repository.gastosFlow, repository.limiteFlow) { gastos, limite ->
                Pair(gastos, limite)
            }.collect { (gastos, limite) ->
                recalcularEstado(gastos, limite)
            }
        }
    }

    private fun recalcularEstado(gastos: List<Gasto>, limite: Double) {
        val total = gastos.sumOf { it.monto }
        val saldo = (limite - total).coerceAtLeast(0.0)
        val porcentaje = if (limite > 0) (total / limite).toFloat() else 0f

        val agrupado = gastos.groupBy { it.categoriaId }
        var mayorCatNombre = "N/A"
        var mayorCatMonto = 0.0

        agrupado.forEach { (catId, lista) ->
            val sumaCat = lista.sumOf { it.monto }
            if (sumaCat > mayorCatMonto) {
                mayorCatMonto = sumaCat
                mayorCatNombre = _uiState.value.categorias.find { it.id == catId }?.nombre ?: catId
            }
        }

        _uiState.update { actual ->
            actual.copy(
                isLoading = false,
                limiteMensualGlobal = limite,
                totalGastado = total,
                saldoDisponible = saldo,
                porcentajeConsumido = porcentaje,
                listaGastos = gastos,
                gastoMayorConsumo = mayorCatNombre,
                montoMayorConsumo = mayorCatMonto
            )
        }
    }

    fun seleccionarCategoriaFiltro(categoriaId: String?) {
        _uiState.update { it.copy(categoriaSeleccionadaId = categoriaId) }
    }

    fun registrarOGuardarGasto(
        concepto: String,
        monto: Double,
        categoriaId: String,
        metodoPago: String,
        idExistente: String? = null
    ) {
        val gasto = Gasto(
            id = idExistente ?: java.util.UUID.randomUUID().toString(),
            concepto = concepto,
            monto = monto,
            categoriaId = categoriaId,
            metodoPago = metodoPago
        )
        repository.guardarGasto(gasto)
    }

    fun eliminarGasto(gastoId: String) {
        repository.eliminarGasto(gastoId)
    }

    fun actualizarLimite(nuevoLimite: Double) {
        repository.actualizarLimiteMensual(nuevoLimite)
    }
}
