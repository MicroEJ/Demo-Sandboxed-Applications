/*
 * Kotlin
 *
 * Copyright 2024-2025 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
plugins {
    id("com.microej.gradle.addon-library") version "1.1.0"
}

dependencies {
    implementation("ej.api:edc:1.3.7")
    implementation("ej.api:kf:1.7.0")

    implementation("ej.library.eclasspath:logging:1.2.1")
    implementation("ej.library.runtime:service:1.2.0")
}

