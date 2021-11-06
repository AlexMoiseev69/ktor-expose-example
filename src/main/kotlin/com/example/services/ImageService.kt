package com.example.services

import com.drew.imaging.ImageMetadataReader
import com.drew.lang.GeoLocation
import com.drew.metadata.exif.GpsDirectory
import java.io.File


object ImageService {

    fun getPhotoLocation(): List<Pair<Double, Double>> {
        val metadata = ImageMetadataReader.readMetadata(File("src/main/resources/images/delisions.jpeg"))
        val gpsDirectories = metadata.getDirectoriesOfType(
            GpsDirectory::class.java
        )
        val photoLocations = ArrayList<GeoLocation>()
        for (gpsDirectory in gpsDirectories) {
            // Try to read out the location, making sure it's non-zero
            val geoLocation = gpsDirectory.geoLocation
            if (geoLocation != null && !geoLocation.isZero) {
                // Add to our collection for use below
                photoLocations.add(geoLocation)
                break
            }
        }
        return photoLocations.map { Pair(it.latitude, it.longitude) }
    }

}
