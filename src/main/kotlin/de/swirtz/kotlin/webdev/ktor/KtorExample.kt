package de.swirtz.kotlin.webdev.ktor

import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.netty.Netty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val LOG: Logger = LoggerFactory.getLogger("ktor-app")

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080, module = Application::main).start(wait = true)
}


