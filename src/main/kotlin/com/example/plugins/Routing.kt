package com.example.plugins

import com.example.persistence.Cities
import com.example.persistence.City
import com.example.persistence.User
import com.example.persistence.Users
import com.example.persistence.UsersDto
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

fun Application.configureRouting() {
    routing {
        post("/users") {
            val userDto = call.receive<UsersDto>()
            val newId = UUID.randomUUID()
            transaction {
                Users.insert {
                    it[id] = newId
                    it[age] = userDto.age
                    it[firstname] = userDto.firstname
                    it[lastname] = userDto.lastname
                    it[cityId] = userDto.cityId
                }
            }
            call.respond(status = HttpStatusCode.Created, newId.toString())
        }
        get("/users") {
            val users: ArrayList<User> = arrayListOf()
            transaction {
                Users.leftJoin(Cities).slice(Users.id, Users.age, Users.firstname, Users.lastname, Cities.name).selectAll().map {
                    users.add(
                        User(
                            id = it[Users.id].toString(),
                            firstName = it[Users.firstname],
                            lastName = it[Users.lastname],
                            age = it[Users.age],
                            cityName = it[Cities.name]
                        )
                    )
                }
            }
            call.respond(users)
        }
        get("/cities") {
            val cities: ArrayList<City> = arrayListOf()
            transaction {
                Cities.selectAll().map {
                    cities.add(
                        City(
                            id = it[Cities.id].toString(),
                            name = it[Cities.name]
                        )
                    )
                }
            }
            call.respond(cities)
        }
        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }

        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
