package com.example

import com.example.plugins.configureDatabase
import com.example.plugins.configureHTTP
import com.example.plugins.configureMonitoring
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureDatabase()
        configureRouting()
        configureSecurity()
        configureMonitoring()
        configureSerialization()
        configureHTTP()
    }.start(wait = true)
}
