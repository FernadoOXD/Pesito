package com.upchiapas.kt_template.pesito.di

import android.content.Context
import com.upchiapas.kt_template.pesito.data.datasource.PesitoLocalDataSource
import com.upchiapas.kt_template.pesito.data.repository.PesitoRepository
import com.upchiapas.kt_template.pesito.data.repository.PesitoRepositoryImpl

class PesitoModule(private val context: Context) {

    private val localDataSource: PesitoLocalDataSource by lazy {
        PesitoLocalDataSource(context)
    }

    val repository: PesitoRepository by lazy {
        PesitoRepositoryImpl(localDataSource, context)
    }
}
