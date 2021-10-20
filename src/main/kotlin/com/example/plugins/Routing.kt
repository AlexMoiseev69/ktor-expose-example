package com.example.plugins

import com.example.persistence.User
import com.example.persistence.Users
import com.example.persistence.UsersDto
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

fun Application.configureRouting() {
    install(Locations) {
    }


    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/users") {
            val userDto = call.receive<UsersDto>()
            val newId = UUID.randomUUID()
            transaction {
                Users.insert {
                    it[id] = newId
                    it[age] = userDto.age
                    it[firstname] = userDto.firstname
                    it[lastname] = userDto.lastname
                }
            }
            call.respond(status = HttpStatusCode.Created, newId.toString())
        }
        get("/users") {
            val users: ArrayList<User> = arrayListOf()
            transaction {
                Users.selectAll().map {
                    users.add(
                        User(
                            id = it[Users.id].toString(),
                            firstName = it[Users.firstname],
                            lastName = it[Users.lastname],
                            age = it[Users.age]
                        )
                    )
                }
            }
            call.respond(users)
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
