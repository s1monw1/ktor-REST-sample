package de.swirtz.kotlin.webdev.ktor

import com.google.gson.Gson
import de.swirtz.kotlin.webdev.ktor.repo.Person
import de.swirtz.kotlin.webdev.ktor.repo.PersonRepo
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class KtorTest {

    private val content = """{"name":"Pan", "age":12}"""
    private val json = "application/json"

    private val gson = Gson()

    @After
    fun clear() = PersonRepo.clear()

    @Test
    fun firstGetTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun getAllPersonsTest() = withTestApplication(Application::main) {
        val person = savePerson(gson.toJson(Person("Bert", 40)))
        val person2 = savePerson(gson.toJson(Person("Alice", 25)))
        handleRequest(HttpMethod.Get, REST_ENDPOINT) {
            addHeader("Accept", json)
        }.response.let {
            assertEquals(HttpStatusCode.OK, it.status())
            val response = gson.fromJson(it.content, Array<Person>::class.java)
            response.forEach { r -> println(r) }
            response.find { r -> r.name == person.name } ?: fail()
            response.find { r -> r.name == person2.name } ?: fail()
        }
        assertEquals(2, PersonRepo.getAll().size)
    }


    @Test
    fun savePersonTest() = withTestApplication(Application::main) {
        val person = savePerson()
        handleRequest(HttpMethod.Get, "$REST_ENDPOINT/${person.id}") {
            addHeader("Accept", json)
        }.response.let {
            assertEquals(HttpStatusCode.OK, it.status())
        }
        assertEquals(1, PersonRepo.getAll().size)
    }

    @Test
    fun deletePersonTest() = withTestApplication(Application::main) {
        val person = savePerson()
        assertEquals(1, PersonRepo.getAll().size)
        handleRequest(HttpMethod.Delete, "$REST_ENDPOINT/${person.id}") {
            addHeader("Accept", json)
        }.response.let {
            assertEquals(HttpStatusCode.OK, it.status())
        }
        assertEquals(0, PersonRepo.getAll().size)

    }

    private fun TestApplicationEngine.savePerson(person: String = content): Person {
        val post = handleRequest(HttpMethod.Post, REST_ENDPOINT) {
            setBody(person)
            addHeader("Content-Type", json)
            addHeader("Accept", json)

        }
        with(post) {
            assertEquals(HttpStatusCode.OK, response.status())
            println(response.content)
            return gson.fromJson(response.content, Person::class.java)
        }
    }


}