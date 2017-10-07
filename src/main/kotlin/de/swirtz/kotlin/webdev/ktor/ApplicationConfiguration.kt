package de.swirtz.kotlin.webdev.ktor

import de.swirtz.kotlin.webdev.ktor.repo.Person
import de.swirtz.kotlin.webdev.ktor.repo.PersonRepo
import kotlinx.html.*
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.ApplicationCall
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CORS
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.html.respondHtml
import org.jetbrains.ktor.request.receive
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.delete
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.post
import org.jetbrains.ktor.routing.routing
import java.time.Duration

const val REST_ENDPOINT = "/persons"


fun Application.main() {
    install(DefaultHeaders)
    install(CORS) {
        maxAge = Duration.ofDays(1)
    }
    install(GsonSupport) {
        setPrettyPrinting()
    }

    routing {
        get("$REST_ENDPOINT/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
            LOG.debug("Get Person entity with Id=$id")
            call.respond(PersonRepo.get(id))
        }
        get("$REST_ENDPOINT/") {
            LOG.debug("Get all Person entities")
            call.respond(PersonRepo.getAll())
        }
        delete("$REST_ENDPOINT/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
            LOG.debug("Delete Person entity with Id=$id")
            call.respondSuccessJson(PersonRepo.remove(id))
        }
        delete("$REST_ENDPOINT/") {
            LOG.debug("Delete all Person entities")
            PersonRepo.clear()
            call.respondSuccessJson()
        }
        post("$REST_ENDPOINT/") {
            val receive = call.receive<Person>()
            println("Received Post Request: $receive")
            call.respondSuccessJson(PersonRepo.add(receive))
        }
        get("/") {
            call.respondHtml {
                head {
                    title("ktor Example Application")
                }
                body {
                    h1 { +"Hello DZone Readers" }
                    p {
                        +"How are you doing?"
                    }
                }
            }
        }
    }
}

private suspend fun ApplicationCall.respondSuccessJson(value: Boolean = true) = respond("""{"success": "$value"}""")