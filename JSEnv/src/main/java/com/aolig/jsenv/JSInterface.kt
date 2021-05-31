package com.aolig.jsenv

import android.os.Handler
import com.eclipsesource.v8.V8


abstract class JSInterface(override val runtime: V8, final override val actuator: JSActuator) :
    IJSInterface {
    // 执行器handler
    protected val handler = Handler(actuator.looper)

    fun checkThread() = runtime.locker.checkThread()
}