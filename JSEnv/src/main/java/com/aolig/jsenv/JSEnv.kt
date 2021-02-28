package com.aolig.jsenv


import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


class JSEnv(script: String) {
    private val jsActuator: JSActuator

    init {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .tag("JSEnv:")
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        // 通过配置功能类列表实现JS环境内功能增减
        val interfaces = listOf(Console::class, JSTimer::class,)

        jsActuator = JSActuator(script, interfaces)

        jsActuator.start()

        Logger.e("JSEnv is Ready!")

    }

}