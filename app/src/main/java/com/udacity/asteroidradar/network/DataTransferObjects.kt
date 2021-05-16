package com.udacity.asteroidradar.network

import androidx.lifecycle.Transformations.map
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.DatabaseAsteroid

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

/**
 * AsteroidHolder holds a list of Asteroids.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "asteroids": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val near_earth_objects: List<NetworkAsteroid>)
data class NetworkPictureOfDayContainer(val picture_of_day: NetworkPictureOfDay)

@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

@JsonClass(generateAdapter = true)
data class NetworkPictureOfDay(
        val mediaType: String,
        val title: String,
        val url: String)
/**
 * Convert Network results to database objects
 */
fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return near_earth_objects.map {
        Asteroid(
            id=it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
}

fun List<Asteroid>.asDatabaseModel(): List<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid (
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}