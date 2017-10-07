package de.swirtz.kotlin.webdev.ktor

import com.google.gson.Gson
import de.swirtz.kotlin.webdev.ktor.repo.Person
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

    private val content = """{"name":"Pan", "age":12}"""
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
        val person = saveContent()
        handleRequest(HttpMethod.Get, "$REST_ENDPOINT/${person.id}") {
            addHeader("Accept", json)
        }.response.let {
            assertEquals(HttpStatusCode.OK, it.status())
        }
        assertEquals(1, PersonRepo.getAll().size)
    }

    @Test
    fun deletePersonTest() = withTestApplication(Application::main) {
        val person = saveContent()
        assertEquals(1, PersonRepo.getAll().size)
        handleRequest(HttpMethod.Delete, "$REST_ENDPOINT/${person.id}") {
            addHeader("Accept", json)
        }.response.let {
            assertEquals(HttpStatusCode.OK, it.status())
        }
        assertEquals(0, PersonRepo.getAll().size)

    }

    private fun TestApplicationHost.saveContent() :Person{
        val post = handleRequest(HttpMethod.Post, REST_ENDPOINT) {
            body = content
            addHeader("Content-Type", json)
            addHeader("Accept", json)

        }
        with(post) {
            assertEquals(HttpStatusCode.OK, response.status())
            println(response.content)
            return Gson().fromJson(response.content, Person::class.java)
        }
    }


}