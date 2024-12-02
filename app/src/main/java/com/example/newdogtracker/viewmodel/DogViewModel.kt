package com.example.newdogtracker.viewmodel

import Dog
import androidx.lifecycle.*
import com.example.newdogtracker.repository.DogRepository
import kotlinx.coroutines.launch

class DogViewModel(private val repository: DogRepository) : ViewModel() {

    // LiveData for observing the list of dogs
    val allDogs: LiveData<List<Dog>> = repository.allDogs.asLiveData()

    // Add a new dog
    fun insert(dog: Dog) {
        viewModelScope.launch {
            repository.insert(dog)
        }
    }

    // Delete a dog
    fun delete(dog: Dog) {
        viewModelScope.launch {
            repository.delete(dog)
        }
    }

    fun getDogById(id: Int): LiveData<Dog?> {
        val result = MutableLiveData<Dog?>()
        viewModelScope.launch {
            result.postValue(repository.getDogById(id))
        }
        return result
    }
}

class DogViewModelFactory(private val repository: DogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
