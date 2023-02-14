package com.hezhihu89.module

const val NOT_SET_DEFAULT = "NOT_SET"

open class IncludeModule(val path: String = NOT_SET_DEFAULT,val version: String?,val include: Boolean?) {

    override fun toString(): String {
        return "IncludeModule(path='$path', version='$version', include=$include)"
    }
}

open class IncludeModules(val path: String,
                          val version: String,
                          val include: Boolean = false,
                          val modules: Map<String,IncludeModule>) {

    override fun toString(): String {
        return "IncludeModules(path='$path', version='$version', include=$include, modules=$modules)"
    }
}
