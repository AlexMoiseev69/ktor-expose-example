package com.example.persistence

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = uuid("id")
    val firstname = text("firstname")
    val lastname = text("lastname")
    val age = integer("age")

    override val primaryKey = PrimaryKey(id, name = "PK_User_ID")
}

@Serializable
data class User(
    val id: String = "",
    val firstName: String,
    val lastName: String,
    val age: Int
)

@Serializable
class UsersDto(val firstname: String, val lastname: String, val age: Int)
