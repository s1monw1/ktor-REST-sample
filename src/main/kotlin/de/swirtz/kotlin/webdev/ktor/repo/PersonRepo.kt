package de.swirtz.kotlin.webdev.ktor.repo

import de.swirtz.kotlin.webdev.ktor.Person
import java.util.concurrent.CopyOnWriteArrayList

object PersonRepo {

    private val persons = CopyOnWriteArrayList<Person>()

    fun add(p: Person) = persons.add(p)

    fun get(id: String) = persons.find { it.id.toString() == id } ?: throw IllegalArgumentException("No entitiy found for $id")

    fun getAll() = persons.toList()

    fun remove(p: Person) = persons.remove(p)

    fun remove(id: String) = persons.remove(get(id))


    fun clear() = persons.clear()

}