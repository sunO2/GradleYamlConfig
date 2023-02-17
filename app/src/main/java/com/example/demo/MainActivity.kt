package com.example.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


@YamlParamsInject(yamlKey = "aliyunApiKey")
const val apiKey = "10000"

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Baidu()
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.text1).apply {
           text = "$apiKey $apiKey CC: ${Baidu.CC}"
        }
    }
}