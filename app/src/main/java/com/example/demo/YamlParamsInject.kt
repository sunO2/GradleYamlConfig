package com.example.demo


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class YamlParamsInject(val yamlKey: String){
}
