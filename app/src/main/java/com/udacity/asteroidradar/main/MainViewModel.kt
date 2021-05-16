package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    enum class AsteroidFilter {
        WEEKLY,
        TODAY
    }

    private val asteroidFilter = MutableLiveData(AsteroidFilter.WEEKLY)

    private val database = getDatabase(application)

    private val asteroidsRepository = AsteroidsRepository(database)

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> get() = _pictureOfDay

    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()

            val picture = asteroidsRepository.getPictureOfDay()
            _pictureOfDay.postValue(picture)
        }
    }

    val asteroidlist = Transformations.switchMap(asteroidFilter) {
        when (it!!) {
            AsteroidFilter.TODAY -> asteroidsRepository.todayAsteroids
            AsteroidFilter.WEEKLY -> asteroidsRepository.weeklyAsteroids
            else -> asteroidsRepository.savedAsteroids
        }
    }

    fun setAsteroidFilter(filter: AsteroidFilter) {
        asteroidFilter.postValue(filter)
    }
}
