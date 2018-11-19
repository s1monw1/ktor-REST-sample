package de.swirtz.kotlin.webdev.ktor

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val LOG: Logger = LoggerFactory.getLogger("ktor-app")

const val portArgName = "--server.port"
const val defaultPort = 8080

fun main(args: Array<String>) {
    val portConfigured = args.isNotEmpty() && args[0].startsWith(portArgName)

    val port = if (portConfigured) {
        LOG.debug("Custom port configured: ${args[0]}")
        args[0].split("=").last().trim().toInt()
    } else defaultPort
    embeddedServer(Netty, port, module = Application::main).start(wait = true)
}