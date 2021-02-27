package com.aolig.jsenv

import android.os.Handler
import android.os.Looper
import com.eclipsesource.v8.JavaCallback
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Function
import com.eclipsesource.v8.V8Object
import com.orhanobut.logger.Logger
import kotlin.random.Random

class JSTimer(runtime: V8, val JSActuator: JSActuator) : JSInterface(runtime) {
    // 定时器Id
    private var timerId = 0

    /**
     * setTimeout函数实现
     */
    private val setTimeout: JavaCallback = JavaCallback { obj, v8Array ->
        obj.close()
        val v8Function = v8Array.getObject(0) as V8Function
        val time = v8Array.getInteger(1).toLong()
        val taskId = timerId++
        // 通过主线程Looper的handler实现定时器，通知JS线程执行回调
        val h = Handler(Looper.getMainLooper())
        h.postDelayed({ JSActuator.postMessage(v8Function) }, time)
        v8Array.close()
        taskId
    }

    override fun install() {
        runtime.registerJavaMethod(setTimeout, "setTimeout")
    }

    override fun release() {}
}