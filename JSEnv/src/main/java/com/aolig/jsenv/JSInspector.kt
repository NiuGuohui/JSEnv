package com.aolig.jsenv

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.inspector.DebuggerConnectionListener
import com.eclipsesource.v8.inspector.V8Inspector
import com.eclipsesource.v8.inspector.V8InspectorDelegate
import com.orhanobut.logger.Logger


class JSInspector(runtime: V8, actuator: JSActuator) : JSInterface(runtime, actuator) {

    override fun install() {
        val ins = V8Inspector.createV8Inspector(runtime, object : V8InspectorDelegate {
            override fun onResponse(message: String?) {
                Logger.d(message)
            }

            override fun waitFrontendMessageOnPause() {
                Logger.d("wait")
            }

        })
        ins.addDebuggerConnectionListener(object : DebuggerConnectionListener {
            override fun onDebuggerConnected() {
                Logger.d("链接")
            }

            override fun onDebuggerDisconnected() {
                Logger.d("断开")
            }
        })
    }

    override fun release() {

    }
}