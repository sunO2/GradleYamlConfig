package com.hezhihu89.task

import com.hezhihu89.utils.TaskContain
import org.gradle.api.Project

class MavenPublishingTask {

    fun apply(project: Project){
        project.tasks.create("push2LocalMaven"){
            it.group = TaskContain.TASK_GROUP
            it.description = "用于上传开发版本的maven SNAPSHOT包"
            it.dependsOn("publishDebugPublicationToAppLocalRepository")
        }
        project.tasks.create("push2Maven"){
            it.group = TaskContain.TASK_GROUP
            it.description = "用于上传正式版本的maven包"
            it.dependsOn("publishReleasePublicationToAppLocalRepository")
        }
    }

    companion object {
        fun create(project: Project) {
            MavenPublishingTask().apply(project)
        }
    }
}