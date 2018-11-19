import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version
import org.jetbrains.kotlin.gradle.dsl.Coroutines

val kotlinVersion = "1.3.10"
val ktorVersion = "1.0.0"

plugins {
    kotlin("jvm") version "1.3.10"
    application
}

repositories {
    jcenter()
    mavenCentral()
    "http://dl.bintray.com/kotlin".let {
        maven { setUrl("$it/ktor") }
        maven { setUrl("$it/kotlinx") }
    }

}

dependencies {

    fun ktor(s: String = "", v: String = ktorVersion) = "io.ktor:ktor$s:$v"

    compile(kotlin("stdlib-jdk8", kotlinVersion))
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.10")
    compile(ktor())
    compile(ktor("-gson"))
    compile(ktor("-html-builder"))
    compile(ktor("-server-netty"))
    compile("ch.qos.logback:logback-classic:1.2.1")
    compile("org.slf4j:slf4j-api:1.7.25")

    testCompile(ktor("-server-test-host"))
    testCompile("junit:junit:4.12")
}

application {
    applicationName = "ktorapp"
    group = "de.swirtz.kotlin.webdev"
    mainClassName = "de.swirtz.kotlin.webdev.ktor.KtorServerKt"
}


tasks {
    withType<Jar> {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClassName))
        }
        val version = "1.0-SNAPSHOT"

        archiveName = "${application.applicationName}-$version.jar"
        from(configurations.compile.map { if (it.isDirectory) it else zipTree(it) })
    }

}
