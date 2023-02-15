package com.hezhihu89.task

import com.hezhihu89.utils.TaskContain
import org.gradle.api.Project

class MavenPublishingTask {

    fun apply(project: Project){
        project.tasks.create("push2LocalMaven"){
            it.group = TaskContain.TASK_GROUP
            it.dependsOn("publishDebugPublicationToAppLocalRepository")
        }
        project.tasks.create("push2Maven"){
            it.group = TaskContain.TASK_GROUP
            it.dependsOn("publishReleasePublicationToAppLocalRepository")
        }
    }

    companion object {
        fun create(project: Project) {
            MavenPublishingTask().apply(project)
        }
    }
}