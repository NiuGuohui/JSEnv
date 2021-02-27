package com.aolig.jsenv

import com.eclipsesource.v8.V8


abstract class JSInterface(protected val runtime: V8) : IJSInterface {

    override abstract fun install()

    override fun release() {}

    fun checkThread() = runtime.locker.checkThread()
}