package com.hezhihu89.module

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
open class App(val app: APPConfig,val library: Map<String,String>){
    override fun toString(): String {
        return "App(app=$app, \n library=$library)"
    }
}
