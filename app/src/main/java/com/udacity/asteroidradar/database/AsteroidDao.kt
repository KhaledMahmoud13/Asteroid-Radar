package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    @Query("select * from asteroids WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroids(startDate: String, endDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)
}