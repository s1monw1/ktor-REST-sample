group 'de.swirtz.kotlin'
version '1.0-SNAPSHOT'

buildscript {
    ext.ktor_version = '0.9.0'
    ext.kotlin_version = '1.1.51'

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

apply plugin: 'kotlin'
apply plugin: 'application'

sourceCompatibility = 1.8

kotlin {
    experimental {
        coroutines "enable"
    }
}

compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    jcenter()
    mavenCentral()
    maven { url "http://dl.bintray.com/kotlin/ktor" }
    maven { url "https://dl.bintray.com/kotlin/kotlinx" }
}

dependencies {
    compile "org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlin_version"
    compile "org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.4"
    compile "io.ktor:ktor:$ktor_version"
    compile "io.ktor:ktor-gson:$ktor_version"
    compile "io.ktor:ktor-html-builder:$ktor_version"
    compile "io.ktor:ktor-server-netty:$ktor_version"
    compile "ch.qos.logback:logback-classic:1.2.1"
    compile group: 'org.slf4j', name: 'slf4j-api', version: '1.7.25'

    testCompile "io.ktor:ktor-server-test-host:$ktor_version"
    testCompile group: 'junit', name: 'junit', version: '4.12'
}

sourceSets {
    main.kotlin.srcDirs += 'src/main/kotlin'
    test.kotlin.srcDirs += 'src/test/kotlin'
}


archivesBaseName = 'ktorapp'
version = '1.0-SNAPSHOT'
group = 'de.swirtz.kotlin.webdev'
mainClassName = 'de.swirtz.kotlin.webdev.ktor.KtorServerKt'

jar {
    manifest {
        attributes 'Main-Class': "$mainClassName"
    }

    from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
}
