package com.hezhihu89.utils

import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logging


object YLog {
    val log get() = YLog

    fun d(message: Any,tag: String = "YLog"){
        println("$tag: $message")
    }
}