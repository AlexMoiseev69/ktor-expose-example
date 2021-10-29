package com.example.plugins

import com.example.persistence.Cities
import com.example.persistence.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDatabase() {
    val config = HikariConfig("/hikari.properties")
    config.schema = "public"
    val ds = HikariDataSource(config)
    Database.connect(ds)
    transaction {
        SchemaUtils.drop(Cities, Users)
        SchemaUtils.create(Cities, Users)

        val cityNames = listOf("Paris", "Moscow", "Helsinki")
        Cities.batchInsert(cityNames) { name ->
            this[Cities.name] = name
        }
    }
}
