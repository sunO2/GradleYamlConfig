package com.hezhihu89.maven

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.LibraryPublishing
import com.hezhihu89.task.MavenPublishingTask
import com.hezhihu89.utils.VersionContain
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.ObjectConfigurationAction
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import java.util.*

/**
 * 扩展推送方法
 */
private fun Project.publishing(action: LibraryPublishing.() -> Unit){
    afterEvaluate {
        extensions.findByType(LibraryExtension::class.java)?.publishing {
            action.invoke(this)
        }
    }
}

fun Project.publishingConfig(configure: Action<PublishingExtension>) {
    afterEvaluate {
        (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("publishing", configure)
    }
}



class ProjectMavenConfig(project: Project): Project by project {

    /**
     * 设置maven 仓库地址
     */
    private val mavenStore: MavenArtifactRepository.() -> Unit = {
        name = "Yaml"
        setUrl("${rootProject.projectDir}/localRepo")
    }

    /**
     *  添加maven publish 插件
     */
    private val mavenPublishPlugin: ObjectConfigurationAction.() -> Unit = {
        plugin("maven-publish")
    }

    /**
     * 设置推送maven 资源
     */
    private val publishingSources: LibraryPublishing.() -> Unit = {
        singleVariant("debug"){
            withSourcesJar()
        }
        singleVariant("release"){
            withSourcesJar()
        }
        publishingConfig(publishConfig)
    }

    /**
     * 配置 maven-push
     */
    private val publishConfig: PublishingExtension.() -> Unit = {
        repositories.maven(mavenStore)
        versionProperties?.apply{
            val group = getProperty(VersionContain.LIBRARY_GROUP)
            val name = getProperty(VersionContain.LIBRARY_NAME)
            val version = getProperty(VersionContain.LIBRARY_VERSION)
            publications.create("release",MavenPublication::class.java).apply {
                this.groupId = group
                this.artifactId = name
                this.version = version
                afterEvaluate { components.findByName("release") }
            }
            publications.create("debug",MavenPublication::class.java).apply {
                this.groupId = group
                this.artifactId = name
                this.version = "$version-SNAPSHOT"
                afterEvaluate { components.findByName("debug") }
            }
        }
    }

    /**
     * 获取版本控制配置表
     */
    private val versionProperties:Properties?  by lazy {
        val versionPropertiesFile = file(VersionContain.LIBRARY_VERSION_FILE)
        if(!versionPropertiesFile.exists()){ return@lazy null }
        val inputStream = versionPropertiesFile.inputStream()
        val versionProperties = Properties()
        try {
            versionProperties.load(inputStream)
        }catch (e: Exception){
            e.printStackTrace()
            return@lazy null
        }finally {
            inputStream.close()
        }
        return@lazy versionProperties
    }


    /***
     * 配置插件
     */
    fun apply(){
        versionProperties?.apply {
            apply(mavenPublishPlugin)
            publishing(publishingSources)
            MavenPublishingTask.create(this@ProjectMavenConfig)
        }
    }



    companion object{
        fun create(project: Project): ProjectMavenConfig{
           return ProjectMavenConfig(project)
        }
    }
}