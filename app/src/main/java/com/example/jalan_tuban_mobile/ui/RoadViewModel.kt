package com.example.jalan_tuban_mobile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.jalan_tuban_mobile.model.Road
import com.example.jalan_tuban_mobile.repository.RoadRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RoadViewModel(private val repository: RoadRepository): ViewModel() {
    val allRoads: LiveData<List<Road>> = repository.allroads.asLiveData()

    fun insert(road: Road) = viewModelScope.launch {
        repository.insertroad(road)
    }

    fun delete(road: Road) = viewModelScope.launch {
        repository.deleteroad(road)
    }

    fun update(road: Road) = viewModelScope.launch {
        repository.updateroad(road)
    }
}

class RoadViewModelFactory(private val repository: RoadRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((RoadViewModel::class.java))){
            return RoadViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}