package com.hezhihu89

import com.hezhihu89.module.APPConfig
import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        println("参数：${target.extensions.findByType(APPConfig::class.java)}")
    }
}