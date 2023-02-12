package com.hezhihu89.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.hezhihu89.module.App
import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Paths


object YamlUtils {

    /**
     * 获取配置
     */
    fun getAppConfig(rootProject: Project): App {
        val file = rootProject.file("app.yaml")
        if(!file.exists()){
            file.createNewFile()
        }
        val appConfigPath = Paths.get(rootProject.projectDir.path,"app.yaml")
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule())
        return try {
            Files.newBufferedReader(appConfigPath).use {
                mapper.readValue(it, App::class.java)
            }
        }catch (e: Exception){
            throw e
        }
    }

}