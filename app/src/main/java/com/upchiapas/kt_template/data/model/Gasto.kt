package com.upchiapas.kt_template.data.model

import java.util.UUID

data class Gasto(
    val id: String = UUID.randomUUID().toString(),
    val concepto: String,
    val monto: Double,
    val categoriaId: String,
    val metodoPago: String = "Efectivo",
    val fechaMs: Long = System.currentTimeMillis()
)