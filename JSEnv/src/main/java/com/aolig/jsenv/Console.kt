package com.aolig.jsenv

import com.eclipsesource.v8.*
import com.eclipsesource.v8.utils.V8ObjectUtils
import com.orhanobut.logger.Logger
import kotlin.reflect.typeOf

/**
 * JS Console对象实现
 */
class Console(runtime: V8, actuator: JSActuator) : JSInterface(runtime, actuator) {
    private val consoleV8Obj = V8Object(runtime)

    private val log: JavaCallback = JavaCallback { _, v8Array ->
        val args = this.print(v8Array)
        Logger.d(args[0], *args.sliceArray(1 until args.size))
        null
    }

    private val info: JavaCallback = JavaCallback { _, v8Array ->
        val args = this.print(v8Array)
        Logger.i(args[0], *args.sliceArray(1 until args.size))
        null
    }

    private val error: JavaCallback = JavaCallback { _, v8Array ->
        val args = this.print(v8Array)
        Logger.e(args[0], *args.sliceArray(1 until args.size))
        null
    }
    private val warn: JavaCallback = JavaCallback { _, v8Array ->
        val args = this.print(v8Array)
        Logger.w(args[0], *args.sliceArray(1 until args.size))
        null
    }

    override fun install() {
        checkThread()
        runtime.add("console", consoleV8Obj)
        consoleV8Obj.registerJavaMethod(log, "log")
        consoleV8Obj.registerJavaMethod(info, "info")
        consoleV8Obj.registerJavaMethod(error, "error")
        consoleV8Obj.registerJavaMethod(warn, "warn")

        consoleV8Obj.close()
    }

    override fun release() {
        checkThread()
        if (!consoleV8Obj.isReleased) consoleV8Obj.close()
    }

    private fun print(v8Array: V8Array): Array<String> {
        val length = v8Array.length()
        val o = V8ObjectUtils.getValue(v8Array, 0)
        val objects = arrayOfNulls<String>(length)
        // 如果第一位是String的话，且包含%sdif之一的话，表示为formatString打印
        if (o is String && o.matches(Regex("%[sdif]"))) {
            for (i in 1 until length) {
                objects[i - 1] = V8ObjectUtils.getValue(v8Array, i) as String
            }
        } else {// 此时console.log的参数内容可能是各种其他类型
            for (i in 0 until length) {
                val value = V8ObjectUtils.getValue(v8Array, i)
                // 判断参数内容是什么
                if (value is V8Object) {
                    when (val type = value.v8Type) {
                        1, 2, 3, 4 -> objects[i] = value.toString()
                        else -> {
                            objects[i] = "[Object ${V8Value.getStringRepresentation(type)}]"
                        }
                    }
                } else {// 基本类型直接展示
                    objects[i] = value.toString()
                }
            }
        }
        return objects.filterNotNull().toTypedArray()
    }
}