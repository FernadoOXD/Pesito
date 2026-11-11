package com.upchiapas.kt_template.pesito.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.upchiapas.kt_template.pesito.data.repository.PesitoRepository

class PesitoViewModelFactory(
    private val repository: PesitoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PesitoViewModel::class.java)) {
            return PesitoViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel class not found: ${modelClass.name}")
    }
}
