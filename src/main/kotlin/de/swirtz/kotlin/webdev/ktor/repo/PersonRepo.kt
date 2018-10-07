package de.swirtz.kotlin.webdev.ktor.repo

import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

object PersonRepo  {

    private val idCounter = AtomicInteger()
    private val persons = CopyOnWriteArraySet<Person>()

    fun add(p: Person): Person {
        if (persons.contains(p)) {
            return persons.find { it == p }!!
        }
        p.id = idCounter.incrementAndGet()
        persons.add(p)
        return p
    }


    fun get(id: String) = persons.find { it.id.toString() == id } ?: throw IllegalArgumentException("No entitiy found for $id")

    fun get(id: Int) = get(id.toString())

    fun getAll() = persons.toList()

    fun remove(p: Person) {
        if (!persons.contains(p)) {
            throw IllegalArgumentException("Person not stored in repo.")
        }
        persons.remove(p)
    }

    fun remove(id: String) = persons.remove(get(id))

    fun remove(id: Int) = persons.remove(get(id))

    fun clear() = persons.clear()

}