package com.aolig.example

import android.app.Activity
import android.os.Bundle
import com.aolig.jsenv.JSConsole
import com.aolig.jsenv.JSEnv
import com.aolig.jsenv.JSTimer
import com.aolig.jsenv.event.JSEvent

class MainActivity() : Activity() {
    private var NODE_SCRIPT = """console.log(Event('click').type)""".trimMargin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 通过配置功能类列表实现JS环境内功能增减
        JSEnv(NODE_SCRIPT, listOf(JSConsole::class, JSTimer::class, JSEvent::class))
    }

    override fun onResume() {
        super.onResume()
//        finish()
    }
}