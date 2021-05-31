package com.aolig.jsenv


import com.aolig.jsenv.event.JSEvent
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import kotlin.reflect.KClass


class JSEnv(script: String, interfaces: List<KClass<out JSInterface>>) {
    private val jsActuator: JSActuator

    init {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .tag("JSEnv:")
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        jsActuator = JSActuator(script, interfaces)

        jsActuator.start()

        Logger.e("JSEnv is Ready!")
    }

}