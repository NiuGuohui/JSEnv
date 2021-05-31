package com.aolig.jsenv

import android.os.Handler
import com.eclipsesource.v8.*

/**
 * JS定时器实现
 */
class JSTimer(runtime: V8, actuator: JSActuator) : JSInterface(runtime, actuator) {
    // 定时器Id
    private var timerId = 0
    private val timerMap = mutableMapOf<Int, Runnable>()

    // 定时器验证结果
    private data class ValidateResult(val v8Function: V8Function, val time: Long)

    private inner class CallBack(private val body: (V8Function, Long, Runnable) -> Unit?) :
        JavaCallback {
        override fun invoke(obj: V8Object, v8Array: V8Array): Int {
            obj.close()
            // 验证参数并获取值
            val (v8Function, time) = validateParams(v8Array)
            v8Array.close()
            return timeout(time, object : Runnable {
                override fun run() {
                    body(v8Function, time, this)
                }
            })
        }

    }

    /**
     * setTimeout函数实现
     */
    private val setTimeout = CallBack { v8Function, _, _ -> actuator.postMessage(v8Function) }

    /**
     * setInterval函数实现
     */
    private val setInterval = CallBack { v8Function, time, runnable ->
        actuator.postMessage(v8Function)
        handler.postDelayed(runnable, time)
        Unit
    }

    /**
     * 清除定时器
     */
    private val clearTimer: JavaVoidCallback = JavaVoidCallback { obj, v8Array ->
        obj.close()
        val key = v8Array[0]
        if (key != null) {
            timerMap[key]?.let { handler.removeCallbacks(it) }
            timerMap.remove(key)
        }
        v8Array.close()
    }


    override fun install() {
        runtime.registerJavaMethod(setTimeout, "setTimeout")
        runtime.registerJavaMethod(setInterval, "setInterval")
        runtime.registerJavaMethod(clearTimer, "clearTimeout")
        runtime.registerJavaMethod(clearTimer, "clearInterval")
    }

    override fun release() {}


    /**
     * 定时触发指定任务
     */
    private fun timeout(time: Long, body: Runnable): Int {
        val taskId = timerId++
        // 通过执行器handler实现定时器，通知JS线程执行回调
        handler.postDelayed(body, time)
        timerMap[taskId] = body
        return taskId
    }

    /**
     * 参数验证
     */
    private fun validateParams(v8Array: V8Array): ValidateResult {
        val v8Function: V8Function
        val time: Long
        val paramsLength = v8Array.length()
        // 参数验证
        if (paramsLength == 0) {
            throw Exception("Failed to execute 'setTimeout': 1 argument required, but only 0 present.")
        } else {
            if (v8Array[0] is V8Function) {
                v8Function = v8Array.getObject(0) as V8Function
                time = if (paramsLength > 1 && v8Array[1] is Number)
                    v8Array.getInteger(1).toLong() else 0
            } else {
                throw Exception("Failed to execute 'setTimeout': arguments[0] is not a function.")
            }
        }
        return ValidateResult(v8Function, time)
    }
}