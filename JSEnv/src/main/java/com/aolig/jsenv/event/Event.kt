package com.aolig.jsenv.event

import com.aolig.jsenv.Utils
import com.eclipsesource.v8.*

class Event(override var type: String?) : IEvent {
    override val cancelable = false
    override var currentTarget: V8Object? = null
    override var target: V8Object? = null
    override var timeStamp: Long = Utils.getCurrentTimeStamp()
    override var isTrusted = false
    override var defaultPrevented = false

    override fun preventDefault() {
        this.defaultPrevented = true
    }

    fun create(runtime: V8): V8Object {
        return V8Object(runtime).add("type", type)
            .add("cancelable", cancelable)
            .add("currentTarget", currentTarget)
            .add("target", target)
            .add("timeStamp", timeStamp.toInt())
            .add("isTrusted", isTrusted)
            .add("defaultPrevented", defaultPrevented)
            .add("preventDefault", V8Function(runtime) { _, _ ->

            })
    }
}