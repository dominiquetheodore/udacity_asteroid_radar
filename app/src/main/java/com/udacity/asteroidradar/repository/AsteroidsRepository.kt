package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.SEVEN_DAYS_LATER
import com.udacity.asteroidradar.api.TODAY_DATE
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await

class AsteroidsRepository(private val database: AsteroidsDatabase) {
    val savedAsteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    val todayAsteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getTodayAsteroids(TODAY_DATE)) {
        it.asDomainModel()
    }

    val weeklyAsteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getWeeklyAsteroids(TODAY_DATE, SEVEN_DAYS_LATER)) {
        it.asDomainModel()
    }

    suspend fun getPictureOfDay(): PictureOfDay {
        return withContext(Dispatchers.IO) {
            val response = Network.asteroidradar.getPictureOfTheDay()
            response
        }
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidList = Network.asteroidradar.getAsteroids().await()
            val asteroids = parseAsteroidsJsonResult(JSONObject(asteroidList))
                .asDatabaseModel()
                .toTypedArray()
            database.asteroidDao.insertAll(*asteroids)
        }
    }
}
