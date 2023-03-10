package com.hezhihu89.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.hezhihu89.module.App
import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Paths
import java.io.File


object YamlUtils {
    fun getAppConfig(file: File): App {
        if(!file.exists()){
            file.createNewFile()
        }
        val appConfigPath = Paths.get(file.path)
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
    /**
     * 获取配置
     */
    fun getAppConfig(rootProject: Project): App {
        val file = rootProject.file("app.yaml")
        return getAppConfig(file)
    }


    /***
     * 获取library 路径
     * :module:module1
     */
    fun getLibraryProjectPath(groupPath: String?,libraryPath: String): String{
        if(null == groupPath || groupPath == "/"){//表示在根路径
            return libraryPath
        }
        return "$groupPath:$libraryPath"
    }

    /***
     * 获取library 文件夹资源 路径
     * /Users/User/Project/modules/module1
     */
    fun getLibraryPath(groupPath: String?,libraryPath: String): String{
        if(null == groupPath || groupPath == "/"){//表示在根路径
            return libraryPath
        }
        return "$groupPath/$libraryPath"
    }
}

object VersionContain {
    const val LIBRARY_VERSION_FILE = "version.properties"
    const val LIBRARY_GROUP = "libraryGroup"
    const val LIBRARY_NAME = "libraryName"
    const val LIBRARY_VERSION = "libraryVersion"
}

object TaskContain {
    const val TASK_GROUP = "yamlConfig"
}
