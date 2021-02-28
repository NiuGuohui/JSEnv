package com.aolig.jsenv

import com.eclipsesource.v8.V8

interface IJSInterface {
    val runtime: V8
    val actuator: JSActuator
    fun install()

    fun release()
}