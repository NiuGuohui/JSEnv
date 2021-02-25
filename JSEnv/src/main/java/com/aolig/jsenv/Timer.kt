package com.aolig.jsenv

import com.eclipsesource.v8.JavaCallback
import com.eclipsesource.v8.V8

class Timer(runtime: V8) : JSInterface(runtime) {
    // 定时器Id
    private val timerId = 0

    // setTimeout函数实现
    private val setTimeout: JavaCallback = JavaCallback { _, v8Array ->

    }

    override fun install() {

    }

    override fun release() {
        TODO("Not yet implemented")
    }
}