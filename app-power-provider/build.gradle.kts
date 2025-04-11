/*
 * Kotlin
 *
 * Copyright 2024-2025 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */

import com.microej.gradle.tasks.BuildFeatureTask
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder

plugins {
    id("com.microej.gradle.application") version "1.1.0"
}

microej {
    applicationEntryPoint = "com.microej.demo.sandbox.provider.PowerProviderEntryPoint"
}

// Local variables used for Kernel dependency management
val defaultKernelGroup: String = property("kernelGroup").toString()
val defaultKernelModule: String = property("kernelModule").toString()
val defaultKernelVersion: String = property("kernelVersion").toString()

dependencies {
    implementation("ej.api:edc:1.3.7")
    implementation("ej.api:kf:1.7.0")

    implementation("ej.library.eclasspath:logging:1.2.1")
    implementation("ej.library.runtime:service:1.2.0")

    implementation(project(":sharedinterface"))

    // Run all the applications together
    microejApplication(project(":app-gui"))
    microejApplication(project(":app-mqtt-publisher"))

    // Kernel dependency
    microejVee("$defaultKernelGroup:$defaultKernelModule:$defaultKernelVersion")
}

val boardIP = property("ipAddress")
val boardPort = property("port")

val buildFeatureTask = tasks.withType(BuildFeatureTask::class).named("buildFeature")
tasks.register("localDeploy") {
    dependsOn("buildFeature")
    group = "microej"

    doLast {
        // Locate app file and metadata
        val applicationFOPath = buildFeatureTask.get().featureFile.get().asFile.absolutePath

        println("Deploying to board at $boardIP:$boardPort")
        // Construct install HTTP request
        val url = URL("http://$boardIP:$boardPort/api/app/install?${buildUrlParams()}")
        val connection = url.openConnection() as HttpURLConnection
        val boundary = "Boundary-" + System.currentTimeMillis()
        setupConnection(connection, boundary)
        connection.outputStream.use { outputStream ->
            writeFileField(outputStream, boundary, "binary", File(applicationFOPath))
            outputStream.write("--$boundary--\r\n".toByteArray())
        }

        handleResponse(connection)
    }
}

// Helper to build URL parameters
fun buildUrlParams() = mapOf(
    "force" to "true",
    "start" to "true",
    "name" to microej.applicationEntryPoint.get().substringAfterLast(".")
).entries.joinToString("&") { "${it.key.encode()}=${it.value.encode()}" }

// Extension function to encode URL parameters
fun String.encode(): String = URLEncoder.encode(this, "UTF-8")

// Set up the connection
fun setupConnection(connection: HttpURLConnection, boundary: String) {
    connection.apply {
        requestMethod = "POST"
        setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
        doOutput = true
    }
}

// Helper to write file field
fun writeFileField(outputStream: OutputStream, boundary: String, name: String, file: File) {
    outputStream.write("--$boundary\r\nContent-Disposition: form-data; name=\"$name\"; filename=\"${file.name}\"\r\n".toByteArray())
    outputStream.write("Content-Type: ${URLConnection.guessContentTypeFromName(file.name)}\r\n\r\n".toByteArray())
    file.inputStream().use { it.copyTo(outputStream) }
    outputStream.write("\r\n".toByteArray())
}

// Handle response from server
fun handleResponse(connection: HttpURLConnection) {
    connection.run {
        try {
            if (responseCode in 200..299) {
                println("Deployment Successful! Response Code: $responseCode")
                println("Server Response: ${inputStream.bufferedReader().use { it.readText() }}")
            } else {
                System.err.println("Deployment Failed. Response Code: $responseCode")
                System.err.println("Error Details: ${errorStream.bufferedReader().use { it.readText() }}")
            }
        } catch (e: Exception) {
            System.err.println("An error occurred while handling the server response: ${e.message}")
            e.printStackTrace()
        } finally {
            disconnect()
        }
    }
}