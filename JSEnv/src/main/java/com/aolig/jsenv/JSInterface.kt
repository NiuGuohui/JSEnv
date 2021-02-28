package com.aolig.jsenv

import com.eclipsesource.v8.V8


abstract class JSInterface(override val runtime: V8, override val actuator: JSActuator) :
    IJSInterface {

    fun checkThread() = runtime.locker.checkThread()
}