package com.hezhihu89.settings

import com.hezhihu89.module.App
import com.hezhihu89.module.IncludeModule
import com.hezhihu89.module.IncludeModules
import com.hezhihu89.utils.VersionContain
import com.hezhihu89.utils.YamlUtils
import org.gradle.api.Plugin
import org.gradle.api.initialization.ProjectDescriptor
import org.gradle.api.initialization.Settings
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStreamWriter
import java.util.Date
import java.util.Properties

class YamlSettingsPlugin: Plugin<Settings> {
    override fun apply(target: Settings) {
        target.rootProject

        val appConfig: App = YamlUtils.getAppConfig(File(target.rootProject.projectDir,"app.yaml"))
        appConfig.library.forEach {
            val group = it.key
            val groupModules = it.value
            val groupPath = groupModules.path
            groupModules.modules.forEach { modules ->
                val moduleParams: IncludeModule = modules.value
                if(moduleParams.include ?: groupModules.include) {
                    val module = modules.key
                    val includeModuleProjectName = "$groupPath:$module"
                    val projectDir =
                        File(target.rootProject.projectDir, "/$groupPath/${moduleParams.path}")
                    target.include(includeModuleProjectName)
                    target.project(":$includeModuleProjectName").apply {
                        this.projectDir = projectDir
                        createConfig(module,group,moduleParams,groupModules)
                    }
                }
            }
        }
    }

    private fun ProjectDescriptor.createConfig(
        libraryName: String,
        libraryGroup: String,
        moduleParams: IncludeModule,
        groupModules: IncludeModules
    ){
        if(!projectDir.exists()){return}

        val versionPropertiesFile = File(projectDir,VersionContain.LIBRARY_VERSION_FILE)
        if(!versionPropertiesFile.exists()){
            versionPropertiesFile.createNewFile()
        }

        val pInputStream: InputStream = versionPropertiesFile.inputStream()
        val libraryVersion = Properties()
        try {
            libraryVersion.load(pInputStream)
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            pInputStream.close()
        }
        val version = moduleParams.version ?: groupModules.version
        libraryVersion.setProperty(VersionContain.LIBRARY_GROUP,libraryGroup)
        libraryVersion.setProperty(VersionContain.LIBRARY_VERSION,version)
        libraryVersion.setProperty(VersionContain.LIBRARY_NAME,libraryName)
        val pOutputStream = OutputStreamWriter(FileOutputStream(versionPropertiesFile),Charsets.UTF_8)
        try {
            libraryVersion.store(pOutputStream," last modify ${Date()}")
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            pOutputStream.close()
        }
    }
}