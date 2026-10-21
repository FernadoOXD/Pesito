package com.upchiapas.kt_template.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upchiapas.kt_template.R

@Composable
fun ResumenCard(
    limite: Double,
    totalGastado: Double,
    saldoDisponible: Double,
    porcentaje: Float,
    gastoMayorCat: String,
    onEditarLimiteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorProgreso = when {
        porcentaje > 0.9f -> Color(0xFFD32F2F)
        porcentaje > 0.7f -> Color(0xFFF57C00)
        else -> Color(0xFF388E3C)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.presupuesto_mensual),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = stringResource(R.string.limite_formato, limite),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                FilledTonalButton(onClick = onEditarLimiteClick) {
                    Text(text = stringResource(R.string.editar_limite))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = stringResource(R.string.total_gastado), style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "$${String.format("%.2f", totalGastado)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = stringResource(R.string.saldo_disponible), style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "$${String.format("%.2f", saldoDisponible)}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF1B5E20),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { porcentaje.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = colorProgreso
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.consumido_porcentaje, porcentaje * 100),
                style = MaterialTheme.typography.labelSmall,
                color = colorProgreso
            )
        }
    }
}