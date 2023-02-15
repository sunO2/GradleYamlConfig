package com.hezhihu89.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project
import java.util.jar.JarFile

class ParamsInjectTransform(val project: Project): Transform() {

    override fun getName(): String {
        return "ParamsInjectTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
       return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }


    override fun transform(transformInvocation: TransformInvocation) {
        val transformInputs = transformInvocation.inputs
        transformInputs.forEach{ transformInput ->
            transformInput.jarInputs.forEach { jarInput ->
                println("jarInput：" + jarInput)
                val enumeration = JarFile(jarInput.file).entries()
                while (enumeration.hasMoreElements()){
                    //获取 jar 里面的内容
                    val entry = enumeration.nextElement()
                    println("jarInput File：" + entry.name)
                }
            }

            transformInput.directoryInputs.forEach { directoryInput ->
                println("directoryInputs：$directoryInput")
                //获取目录里面的文件
                directoryInput.file.listFiles()?.forEach { file ->
                    println("directoryInputs File：" + file.name)
                }
            }
        }

    }


}