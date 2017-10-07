package de.swirtz.kotlin.webdev.ktor

import de.swirtz.kotlin.webdev.ktor.repo.Person
import de.swirtz.kotlin.webdev.ktor.repo.PersonRepo
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals

class PersonRepoTest {

    @After
    fun clear() = PersonRepo.clear()

    @Test
    fun getTest() {
        assertSize(0)
        val p = Person(1, "P1", 40)
        PersonRepo.add(p)
        assertSize(1)
        assertEquals(p, PersonRepo.get(1))
        assertEquals(p, PersonRepo.get("1"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun getNonExistingTest() {
        PersonRepo.get(1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getNonExistingByStringTest() {
        PersonRepo.get("1")
    }

    /**
     * Saving the same object twice will have no effect
     */
    @Test
    fun saveTest() {
        assertSize(0)
        PersonRepo.add(Person(1, "P1", 40))
        assertSize(1)
        PersonRepo.add(Person(1, "P1", 40))
        assertSize(1)
    }

    @Test
    fun deleteTest() {
        PersonRepo.add(Person(1, "P1", 40))
        assertSize(1)
        PersonRepo.remove(1)
        assertSize(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun deleteNonExistingTest() {
        PersonRepo.remove(1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun deleteNonExistingByStringTest() {
        PersonRepo.remove("1")
    }

    @Test
    fun deleteByObjectTest() {
        val p = Person(1, "P1", 40)
        PersonRepo.add(p)
        assertSize(1)
        PersonRepo.remove(p)
        assertSize(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun deleteNonExistingByObjectTest() {
        PersonRepo.remove(Person(1, "", 1))
    }

    private fun assertSize(int: Int) {
        assertEquals(int, PersonRepo.getAll().size)
    }

}