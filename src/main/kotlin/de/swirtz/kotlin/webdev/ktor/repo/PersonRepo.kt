package de.swirtz.kotlin.webdev.ktor.repo

import java.util.concurrent.atomic.AtomicInteger

object PersonRepo {

    private val idCounter = AtomicInteger()
    private val persons = mutableSetOf<Person>()

    private fun <R> personsSynchronized(block: () -> R): R = synchronized(persons) {
        return block()
    }

    fun add(p: Person): Person = personsSynchronized {
        if (persons.contains(p)) {
            persons.find { it == p }!!
        }
        p.id = idCounter.incrementAndGet()
        persons.add(p)
        p
    }

    fun get(id: String) = personsSynchronized {
        persons.find { it.id.toString() == id } ?: throw IllegalArgumentException("No entitiy found for $id")
    }

    fun get(id: Int) = personsSynchronized { get(id.toString()) }

    fun getAll() = personsSynchronized { persons.toList() }

    fun remove(p: Person) = personsSynchronized {
        if (!persons.contains(p)) {
            throw IllegalArgumentException("Person not stored in repo.")
        }
        persons.remove(p)
    }

    fun remove(id: String) = personsSynchronized { persons.remove(get(id)) }

    fun remove(id: Int) = personsSynchronized { persons.remove(get(id)) }

    fun clear() = personsSynchronized { persons.clear() }

}