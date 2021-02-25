package com.aolig.jsenv

import com.eclipsesource.v8.*
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.lang.Exception


class JSEnv {
    val runtime = V8.createV8Runtime("global")

    private var NODE_SCRIPT = """var a = new Promise((r)=>{r('1');console.log('inner')});
        console.log('333');
        a.then(e=>console.log(e));
        console.log('666');""".trimMargin()


    init {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .tag("JSEnv:::")
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        Logger.e("JSEnv is Running")

        Console(runtime).install()

    }

    fun exec(script: String) {
        runtime.locker.checkThread()

        try {
            runtime.executeScript(NODE_SCRIPT)
        } catch (err: Exception) {
            Logger.e(err.toString())
        }
    }
}