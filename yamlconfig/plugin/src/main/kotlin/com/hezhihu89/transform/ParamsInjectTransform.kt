package com.hezhihu89.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.hezhihu89.module.App
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.objectweb.asm.*
import java.io.File
import java.io.FileOutputStream


class ParamsInjectTransform(val id: String,val project: Project,val appConfig: App): Transform() {

    override fun getName(): String {
        return "ParamsInjectTransform-$id"
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
        transformInvocation.outputProvider?.deleteAll()

        transformInputs.forEach{ transformInput ->
            transformInput.jarInputs.forEach { jarInput ->

                val dest = transformInvocation.outputProvider?.getContentLocation(
                    jarInput.file.absolutePath,
                    jarInput.contentTypes,
                    jarInput.scopes,
                    Format.JAR
                )
                FileUtils.copyFile(jarInput.file, dest)

//                val enumeration = JarFile(jarInput.file).entries()
//                while (enumeration.hasMoreElements()){
//                    //获取 jar 里面的内容
//                    val entry = enumeration.nextElement()
//                    println("jarInput File：" + entry.name)
//                }
            }

            transformInput.directoryInputs.forEach { directoryInput ->
                //获取目录里面的文件
                if(directoryInput.file.isDirectory){
                    FileUtils.listFiles(directoryInput.file, arrayOf("class"),true).forEach { file ->
                        val name = file.name
                        if (name.endsWith(".class") && name != ("R.class")
                            && !name.startsWith("R\$") && name != ("BuildConfig.class")
                        ){
                            val reader = ClassReader(file.readBytes())
                            val writer = ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
                            val visitor = CatClassVisitor(writer,appConfig)
                            reader.accept(visitor, ClassReader.EXPAND_FRAMES)

                            val code = writer.toByteArray()
                            val classPath = file.parentFile.absolutePath + File.separator + name
                            val fos = FileOutputStream(classPath)
                            fos.write(code)
                            fos.close()
                        }
                    }
                }
                val dest = transformInvocation.outputProvider?.getContentLocation(
                    directoryInput.name,
                    directoryInput.contentTypes,
                    directoryInput.scopes,
                    Format.DIRECTORY
                )
                FileUtils.copyDirectoryToDirectory(directoryInput.file, dest)
                println("copyDir ${directoryInput.file.path} to ${dest?.path}")
            }
        }

    }

}

class CatClassVisitor(writer: ClassWriter, private val appConfig: App) : ClassVisitor(Opcodes.ASM9,writer) {


    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        if("10000" == value){
            println("输出输出")
            super.visitField(access, "${name}_$name", descriptor, signature, "hezhihu89")
            return  super.visitField(access, name, descriptor, signature, "hezhihu89")
        }
        return InjectParamsAnnotationFieldAdapter(descriptor, appConfig){
            if(null == this){
                super.visitField(access, name, descriptor, signature, value)
            }else{
                super.visitField(access, name, descriptor, signature, this)
            }
        }
    }
}

/**
 * 用于特殊处理注解的方式
 */
class InjectParamsAnnotationFieldAdapter(
    private val pDescriptor: String?,
    private val appConfig: App,
    private val fieldVisitor: Any?.() -> FieldVisitor,
    ): FieldVisitor(Opcodes.ASM9) {

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        return when(descriptor){
            "Lcom/example/demo/YamlParamsInject;" ->
                YamlParamsInjectAnnotationVisitor{
                    val injectParams = appConfig.getInjectParams(this)
                    super.fv = fieldVisitor.invoke(injectParams)
                    super.visitAnnotation(descriptor, visible)
                }
            else -> {
                super.fv = fieldVisitor.invoke(null)
                return super.visitAnnotation(descriptor, visible)
            }
        }
    }

    private fun App.getInjectParams(key: String): Any?{
        injectParams[key]?.apply {
            return when(pDescriptor){
                "I" -> this.toInt()
                else -> this
            }
        }
        return null
    }
}

class YamlParamsInjectAnnotationVisitor(
    private val annotationValue: String.() -> AnnotationVisitor,
): AnnotationVisitor(Opcodes.ASM9) {
    override fun visit(name: String?, value: Any?) {
        av = annotationValue.invoke(value.toString())
        super.visit(name, value)
    }


}