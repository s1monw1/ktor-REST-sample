package de.swirtz.kotlin.webdev.ktor

import de.swirtz.kotlin.webdev.ktor.repo.Person
import de.swirtz.kotlin.webdev.ktor.repo.PersonRepo
import io.ktor.application.*
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.pipeline.*
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import java.time.Duration
import java.text.DateFormat

const val REST_ENDPOINT = "/persons"


fun Application.main() {

    install(DefaultHeaders)
    install(CORS) {
        maxAge = Duration.ofDays(1)
    }
    install(ContentNegotiation){
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    routing {
        get("$REST_ENDPOINT/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("Get Person entity with Id=$id")
                call.respond(PersonRepo.get(id))
            }
        }
        get(REST_ENDPOINT) {
            errorAware {
                LOG.debug("Get all Person entities")
                call.respond(PersonRepo.getAll())
            }
        }
        delete("$REST_ENDPOINT/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("Delete Person entity with Id=$id")
                call.respondSuccessJson(PersonRepo.remove(id))
            }
        }
        delete(REST_ENDPOINT) {
            errorAware {
                LOG.debug("Delete all Person entities")
                PersonRepo.clear()
                call.respondSuccessJson()
            }
        }
        post(REST_ENDPOINT) {
            errorAware {
                val receive = call.receive<Person>()
                println("Received Post Request: $receive")
                call.respond(PersonRepo.add(receive))
            }
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

private suspend fun <R> PipelineContext<*, ApplicationCall>.errorAware(block: suspend () -> R): R? {
    return try {
        block()
    } catch (e: Exception) {
        call.respondText("""{"error":"$e"}""", ContentType.parse("application/json"), HttpStatusCode.InternalServerError)
        null
    }
}

private suspend fun ApplicationCall.respondSuccessJson(value: Boolean = true) = respond("""{"success": "$value"}""")