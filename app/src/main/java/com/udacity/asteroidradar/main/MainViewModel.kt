package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidRepository(database)

    val asteroid = asteroidsRepository.asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()

    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid


    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            asteroidsRepository.refreshAsteroids()
            _pictureOfDay.value = Network.service.getPictureOfDay(Constants.API_KEY)
            Log.e("TAG", _pictureOfDay.value.toString())
        } catch (e: Exception) {
            Log.e("TAG", e.message.toString())
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsDone() {
        _navigateToSelectedAsteroid.value = null
    }

}