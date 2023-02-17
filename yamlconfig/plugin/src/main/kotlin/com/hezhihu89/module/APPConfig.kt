package com.hezhihu89.module

fun Map<String,IncludeModules>.containsLibrary(group: String,libraryName: String): Boolean{
    return this[group]?.modules?.containsKey(libraryName) ?: false
}

/**
 * 应用配置
 */
open class APPConfig(val appName: String,
                     val appVersion: String,
                     val versionCode: Int,
                     val applicationId: String){

    override fun toString(): String {
        return "APPConfig(appName='$appName', appVersion='$appVersion', versionCode=$versionCode, applicationId='$applicationId')"
    }
}

/**
 * 应用模块
 */
open class App(val app: APPConfig,
               val injectParams: Map<String,String>,
               val module: Map<String,String>,
               val library: Map<String,IncludeModules>) {

//
//    @Throws(NullPointerException::class)
//    override fun module(module: String): String {
//        val groups = module.split(":")
//        var query = module
//        if(groups.size > 2){
//            query = "${groups[0]}:${groups[1]}"
//        }
//        val version = this.module[query]
//        return if(null == version){
//            throw NullPointerException("模块 $query 没有配置")
//        }else {
//            "$query:$version"
//        }
//    }

    override fun toString(): String {
        return "App(app=$app, \nlibrary=$library, \nmodule=${module})"
    }
}
