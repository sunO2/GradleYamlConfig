package com.hezhihu89.config

import com.hezhihu89.module.App
import com.hezhihu89.module.IncludeModules
import com.hezhihu89.utils.YamlUtils
import org.gradle.api.Project

class ReplaceModule2Library(private val project: Project): Project by project {

    fun apply(app: App) = afterEvaluate(myApply(app))

    /**
     * 配置模块
     * 替换module版本 module:v1 to module:v2
     * 替换 module to library
     */
    private fun myApply(app: App): Project.() -> Unit = {
        val includeLibrary: Map<String, IncludeModules> = app.library
        val dependencyModule: Map<String,String> = app.module
        configurations.all { cf ->
            /// module:v1 to module:v2
            dependencyModule.forEach {
                cf.resolutionStrategy.force("${it.key}:${it.value}")
            }
            cf.resolutionStrategy.dependencySubstitution.apply {
                includeLibrary.forEach{ libs ->
                    fun group(group: String, modules: IncludeModules){
                        modules.modules.forEach { mod ->
                            fun using(libModule: String){
                                val rowModule = module("$group:$libModule")
                                val replaceLibrary = project(":${
                                    YamlUtils.getLibraryProjectPath(modules.path, libModule)
                                }")
                                substitute(rowModule).using(replaceLibrary)
                            }
                            if(mod.value.include ?: modules.include){
                                using(mod.key)
                            }
                        }
                    }
                    group(libs.key,libs.value)
                }
            }
        }
    }



    companion object{
        fun create(project: Project): ReplaceModule2Library {
            return ReplaceModule2Library(project)
        }
    }
}