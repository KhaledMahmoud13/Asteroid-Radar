package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.seventhDay
import com.udacity.asteroidradar.api.today
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        database.asteroidDao.getAsteroids(today(), seventhDay()).map {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = parseAsteroidsJsonResult(
                JSONObject(
                    Network.service.getAsteroids(
                        today(),
                        seventhDay(),
                        Constants.API_KEY
                    )
                )
            )
            database.asteroidDao.insertAll(*asteroids.asDomainModel())
        }
    }
}