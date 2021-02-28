package com.aolig.jsenv


import android.os.Handler
import android.os.Looper
import android.os.Message
import com.aolig.jsenv.actuator.MessageTask
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.V8Function
import com.orhanobut.logger.Logger
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * 通过Android内置的Looper实现V8内部的事件循环机制，事件循环模型采用回调函数形式触发
 * 消息队列内的所有消息均是V8Function类型的回调函数
 */
class JSActuator(
    private var script: String?,
    private val interfaceMap: List<KClass<out IJSInterface>>
) : Thread() {
    // 消息处理Handler
    private lateinit var mHandler: Handler

    // 执行器线程looper
    lateinit var looper: Looper

    // v8执行环境
    private lateinit var runtime: V8

    companion object {
        val runtimeGlobal = "global"
    }

    /**
     * 向队列追加执行方法
     */
    fun postMessage(fn: V8Function, params: () -> V8Array? = { null }) {
        val m = Message.obtain()
        m.obj = MessageTask(fn, params)
        mHandler.sendMessage(m)
    }

    override fun run() {
        if (Looper.myLooper() == null) Looper.prepare()
        looper = Looper.myLooper()!!
        // 消息处理handler
        mHandler = Handler(looper) {
            try {
                checkThread()
                val task = it.obj as MessageTask
                task.fn.call(runtime, task.params())
            } catch (e: Exception) {
                Logger.e(e.toString())
            }
            true
        }
        // 创建执行环境
        runtime = V8.createV8Runtime(runtimeGlobal)
        // 通过反射，将外部自定义的功能类全部注入环境
        interfaceMap.forEach {
            try {
                (it.primaryConstructor?.call(runtime, this) as JSInterface).install()
            } catch (e: java.lang.Exception) {
                Logger.e(e, "Custom ${it.simpleName} class is invalid")
            }
        }
        // 对于初次执行，会先执行其预置的脚本
        if (script != null) {
            exec(script!!)
            script = null
        }
        Looper.loop()
    }

    /**
     * 检查当前线程
     */
    private fun checkThread() {
        runtime.locker.checkThread()
    }

    /**
     * 执行一段JavaScript代码
     */
    fun exec(script: String) {
        try {
            runtime.executeScript(script)
        } catch (err: Exception) {
            Logger.e(err.toString())
        }
    }

    /**
     * 安全结束执行器
     */
    fun terminate() = looper.quitSafely()

    /**
     * 强制结束执行器
     */
    fun shutdown() = looper.quit()
}