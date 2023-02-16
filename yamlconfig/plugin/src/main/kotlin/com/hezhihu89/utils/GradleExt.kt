package com.hezhihu89.utils

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.LibraryPublishing
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension

//fun Project.`publishing`(action: LibraryPublishing.() -> Unit){
//    extensions.findByType(LibraryExtension::class.java)?.publishing {
//        action.invoke(this)
//    }
//}
//
//fun Project.`publishingConfig`(configure: Action<PublishingExtension>): Unit =
//    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("publishing", configure)
