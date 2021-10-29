package com.example.persistence

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = uuid("id")
    val firstname = text("firstname")
    val lastname = text("lastname")
    val age = integer("age")
    val cityId = (integer("city_id") references Cities.id).nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_User_ID")
}

object Cities : Table() {
    val id = integer("id").autoIncrement() // Column<Int>
    val name = varchar("name", 50) // Column<String>

    override val primaryKey = PrimaryKey(id, name = "PK_Cities_ID")
}
@Serializable
data class City(
    val id: String,
    val name: String,
)
@Serializable
data class User(
    val id: String = "",
    val firstName: String,
    val lastName: String,
    val age: Int,
    val cityName: String
)

@Serializable
class UsersDto(val firstname: String, val lastname: String, val age: Int, val cityId: Int?)
