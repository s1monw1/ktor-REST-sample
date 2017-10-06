package de.swirtz.kotlin.webdev.ktor

import com.google.gson.Gson
import com.sun.org.apache.bcel.internal.generic.GETFIELD
import de.swirtz.kotlin.webdev.ktor.repo.PersonRepo
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.http.HttpMethod
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.testing.TestApplicationHost
import org.jetbrains.ktor.testing.handleRequest
import org.jetbrains.ktor.testing.withTestApplication
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals

class KtorTest {

    private val personId = 123
    private val content = """{"id":$personId, "name":"Pan", "age":12}"""
    private val json = "application/json"
    @After
    fun clear() = PersonRepo.clear()

    @Test
    fun firstGetTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun savePersonTest() = withTestApplication(Application::main) {
        saveContent()
        handleRequest(HttpMethod.Get, "$REST_ENDPOINT/$personId") {
            addHeader("Accept", json)
        }.response.let {
            assertEquals(HttpStatusCode.OK, it.status())
        }
        assertEquals(1, PersonRepo.getAll().size)
    }

    @Test
    fun deletePersonTest() = withTestApplication(Application::main) {
        saveContent()
        assertEquals(1, PersonRepo.getAll().size)
        handleRequest(HttpMethod.Delete, "$REST_ENDPOINT/$personId") {
            addHeader("Accept", json)
        }.response.let {
            assertEquals(HttpStatusCode.OK, it.status())
        }
        assertEquals(0, PersonRepo.getAll().size)

    }

    private fun TestApplicationHost.saveContent() {
        val post = handleRequest(HttpMethod.Post, REST_ENDPOINT) {
            body = content
            addHeader("Content-Type", json)
        }
        with(post) {
            assertEquals(HttpStatusCode.OK, response.status())
            println(response.content)
        }
    }


}