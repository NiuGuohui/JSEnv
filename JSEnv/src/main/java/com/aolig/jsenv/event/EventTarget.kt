package com.aolig.jsenv.event

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Function
import com.eclipsesource.v8.V8Object

open class EventTarget : IEventTarget {
    private val queue = mutableListOf<V8Function>()

    override fun addEventListener(type: String, listener: V8Function, once: Boolean) {

    }

    override fun removeEventListener(type: String, listener: V8Function) {

    }

    override fun dispatchEvent(event: IEvent): Boolean {
        return true
    }

    fun create(runtime: V8): V8Object {
        return V8Object(runtime).add("addEventListener", V8Function(runtime) { target, params ->
            val type = params[0] as String
            val callback = params[1] as V8Function
            val once = params[2] as Boolean
            addEventListener(type, callback, once)
        })
    }
}