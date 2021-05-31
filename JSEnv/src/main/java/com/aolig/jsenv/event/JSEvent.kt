package com.aolig.jsenv.event

import com.aolig.jsenv.JSActuator
import com.aolig.jsenv.JSInterface
import com.eclipsesource.v8.JavaCallback
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object

class JSEvent(runtime: V8, actuator: JSActuator) : JSInterface(runtime, actuator) {

    private val eventConstructor = JavaCallback { _, p1 ->
        val p = V8Object(runtime)
        p.setPrototype(Event(p1[0].toString()).create(runtime))
        p
    }

    private val eventTargetConstructor = JavaCallback { _, p1 ->
        val p = V8Object(runtime)
        p.setPrototype(EventTarget().create(runtime))
        p
    }


    override fun install() {
        runtime.registerJavaMethod(eventConstructor, "Event")
        runtime.registerJavaMethod(eventTargetConstructor, "EventTarget")
    }

    override fun release() {
    }
}