package com.upchiapas.kt_template.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upchiapas.kt_template.R
import com.upchiapas.kt_template.data.model.CategoriaGasto
import com.upchiapas.kt_template.data.model.Gasto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioGastoBottomSheet(
    categorias: List<CategoriaGasto>,
    gastoAEditar: Gasto?,
    presupuestoRestante: Double,
    onGuardar: (concepto: String, monto: Double, categoriaId: String, metodoPago: String, id: String?) -> Unit,
    onDismiss: () -> Unit
) {
    var concepto by remember { mutableStateOf(gastoAEditar?.concepto ?: "") }
    var montoText by remember { mutableStateOf(gastoAEditar?.monto?.let { String.format("%.2f", it) } ?: "45.00") }
    var categoriaIdSeleccionada by remember {
        mutableStateOf(gastoAEditar?.categoriaId ?: categorias.firstOrNull()?.id ?: "")
    }
    var sliderValue by remember { mutableFloatStateOf(gastoAEditar?.monto?.toFloat() ?: 45f) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            // Título e Ícono de Cerrar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.registrar_nuevo_gasto),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de Categoría (FilterChips)
            Text(
                text = stringResource(R.string.seleccionar_categoria),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categorias) { cat ->
                    FilterChip(
                        selected = categoriaIdSeleccionada == cat.id,
                        onClick = { categoriaIdSeleccionada = cat.id },
                        label = { Text(cat.nombre) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Concepto
            OutlinedTextField(
                value = concepto,
                onValueChange = { concepto = it },
                label = { Text(stringResource(R.string.concepto_gasto)) },
                placeholder = { Text("Indica el motivo o establecimiento") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Monto
            OutlinedTextField(
                value = montoText,
                onValueChange = { input ->
                    montoText = input
                    input.toFloatOrNull()?.let { v -> sliderValue = v.coerceIn(0f, 300f) }
                },
                label = { Text(stringResource(R.string.monto_gasto)) },
                prefix = { Text("$ ") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Text(
                text = stringResource(R.string.presupuesto_restante_fmt, presupuestoRestante),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Panel de Ajuste Rápido (Slider + Atajos de suma)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.ajuste_rapido),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "$${String.format("%.2f", sliderValue)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Slider(
                        value = sliderValue.coerceIn(0f, 300f),
                        onValueChange = { newValue ->
                            sliderValue = newValue
                            montoText = String.format("%.2f", newValue)
                        },
                        valueRange = 0f..300f
                    )

                    // Atajos: +$10, +$25, +$50, +$100
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(10, 25, 50, 100).forEach { incremento ->
                            OutlinedButton(
                                onClick = {
                                    val actual = montoText.toDoubleOrNull() ?: 0.0
                                    val nuevo = actual + incremento
                                    montoText = String.format("%.2f", nuevo)
                                    sliderValue = nuevo.toFloat().coerceIn(0f, 300f)
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("+$${incremento}", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de Guardar
            Button(
                onClick = {
                    val montoDouble = montoText.toDoubleOrNull() ?: 0.0
                    if (concepto.isNotBlank() && montoDouble > 0 && categoriaIdSeleccionada.isNotBlank()) {
                        onGuardar(
                            concepto,
                            montoDouble,
                            categoriaIdSeleccionada,
                            "Tarjeta Débito",
                            gastoAEditar?.id
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.guardar_transaccion),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}