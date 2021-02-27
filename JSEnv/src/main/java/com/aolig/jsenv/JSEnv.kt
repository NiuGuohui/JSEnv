package com.aolig.jsenv

import com.eclipsesource.v8.*
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.lang.Exception


class JSEnv(script: String) {
    val jsActuator = JSActuator(script)

    init {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(0)
            .tag("JSEnv:")
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        jsActuator.start()

        Logger.e("JSEnv is Ready!")

    }

}