package com.hezhihu89.config

interface IApp {
    @Throws(NullPointerException::class)
    fun module(module: String): String
}