package com.aolig.jsenv


import com.aolig.jsenv.actuator.MessageTask
import com.eclipsesource.v8.*
import com.orhanobut.logger.Logger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class JSActuator(private var script: String?) : Thread() {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()

    // v8执行环境
    var runtime: V8? = null

    // 在队列中所有的触发均以回调函数形式进行保存和触发
    private val messageQueue = mutableListOf<MessageTask>()

    /**
     * 向队列追加执行方法
     */
    fun postMessage(fn: V8Function, params: V8Array? = null) {
        lock.withLock {
            messageQueue.add(MessageTask(fn, params))
            condition.signal()
        }
    }

    override fun run() {
        lock.withLock {
            runtime = V8.createV8Runtime("global")
            // 注入console
            Console(runtime!!).install()
            // 注入定时器
            JSTimer(runtime!!, this).install()

            // 对于初次执行，会先执行其预置的脚本
            if (script != null) {
                exec(script!!)
                script = null
            }
        }
        try {
            while (true) {
                checkThread()
                lock.withLock {
                    // 如果消息队列没有消息，转交monitor
                    if (isEmpty()) condition.await()
                    else {
                        val cb = messageQueue.removeAt(0)
                        cb.fn.call(runtime, cb.params)
                        cb.fn.close()
                        cb.params?.close()
                    }
                }
            }
        } catch (e: Exception) {
            Logger.e(e, "MessageLoop has Error")
        }
    }

    /**
     * 队列是否为空
     */
    private fun isEmpty(): Boolean {
        lock.withLock { return messageQueue.isEmpty() }
    }

    /**
     * 检查当前线程
     */
    private fun checkThread() {
        runtime!!.locker.checkThread()
    }

    /**
     * 执行一段JavaScript代码
     */
    fun exec(script: String) {
        try {
            lock.withLock { runtime!!.executeScript(script) }
        } catch (err: Exception) {
            Logger.e(err.toString())
        }
    }
}