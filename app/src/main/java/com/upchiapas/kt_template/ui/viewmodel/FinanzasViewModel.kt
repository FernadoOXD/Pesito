package com.upchiapas.kt_template.ui.viewmodel

import android.R
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.upchiapas.kt_template.data.model.FinanzasUiState
import com.upchiapas.kt_template.data.model.Gasto
import com.upchiapas.kt_template.data.repository.FinanzasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FinanzasViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FinanzasRepository(application)

    private val _uiState = MutableStateFlow(FinanzasUiState())
    val  uiState: StateFlow<FinanzasUiState> = _uiState.asStateFlow()

    init {
        val categoriasSemilla = repository.obtenerCategoriasSemilla()
        _uiState.update { it.copy(categorias = categoriasSemilla) }

        viewModelScope.launch {
            combine(repository.gastoFlow, repository.limiteFlow) { gastos, limite ->
                Pair(gastos, limite)
            }.collect { (gastos, limite) ->
                recalcularEstado(gastos, limite)
            }
        }
    }

    private fun  recalcularEstado(gasto: List<Gasto>, limite: Double) {
        val total = gasto.sumOf { it.monto }
        val saldo = (limite - total).coerceAtLeast(0.0)
        val porcentaje = if (limite > 0) (total / limite).toFloat() else 0f

        val agrupado = gasto.groupBy { it.categoriaId }
        var mayorCatNombre = "N/A"
        var mayorCatMonto = 0.0

        agrupado.forEach { catId, lista ->
            val sumaCat = lista.sumOf {it.monto}
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
                listaGastos = gasto,
                gastoMayorConsumo = mayorCatNombre,
                montoMayorConsumo = mayorCatMonto
            )
        }
    }

    fun seleccionarCategoriaFiltro(categoriaId: String?) {
        _uiState.update { it.copy(categoriaSeleccionadaId = categoriaId) }
    }

    fun registrarOGuardarGasto(concepto: String, monto: Double,categoriaId: String, metodoPago: String, idExistente: String? = null) {
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