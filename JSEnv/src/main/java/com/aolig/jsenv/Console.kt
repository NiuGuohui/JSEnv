package com.aolig.jsenv

import com.eclipsesource.v8.JavaCallback
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.V8Object
import com.eclipsesource.v8.utils.V8ObjectUtils
import com.orhanobut.logger.Logger

class Console(runtime: V8) : JSInterface(runtime) {
    private val consoleV8Obj = V8Object(runtime)

    private val log: JavaCallback = JavaCallback { _, v8Array ->
        // TODO("实现自动解析v8对象，并转化为json展示")
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
    }

    override fun release() {
        checkThread()
        if (!consoleV8Obj.isReleased) consoleV8Obj.close()
    }

    private fun print(v8Array: V8Array): Array<String> {
        val length = v8Array.length()
        val o = V8ObjectUtils.getValue(v8Array, 0)
        val objects = arrayOfNulls<String>(length)
        if (o is String && o.matches(Regex("%[a-zA-Z0-9]"))) {
            for (i in 1 until length) {
                objects[i - 1] = V8ObjectUtils.getValue(v8Array, i) as String
            }
        } else {
            for (i in 0 until length) {
                objects[i] = V8ObjectUtils.getValue(v8Array, i) as String
            }
        }
        return objects.filterNotNull().toTypedArray()
    }
}