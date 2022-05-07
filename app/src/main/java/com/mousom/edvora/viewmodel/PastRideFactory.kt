package com.mousom.edvora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mousom.edvora.data.repository.BaseRepository

class PastRideFactory(private val repository: BaseRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PastRideViewModel(repository) as T
    }
}