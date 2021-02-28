package com.aolig.jsenv.actuator

import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.V8Function

/**
 * 保存的队列中的回调函数信息和参数信息
 */
data class MessageTask(val fn: V8Function, val params: () -> V8Array?)
