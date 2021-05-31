package com.aolig.jsenv.event

import com.eclipsesource.v8.V8Function

/**
 * EventTarget接口，由可以接收事件、并且可以创建侦听器的对象实现。
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/EventTarget">EventTarget</a>
 */
interface IEventTarget {
    /**
     * 在EventTarget上注册特定事件类型的事件处理程序。
     * @param type 表示监听事件类型的字符串。
     * @param listener 当所监听的事件类型触发时，会接收到一个事件通知（实现了 Event 接口的对象）对象。listener 必须是一个函数。
     * @param once 表示 listener 在添加之后最多只调用一次。如果是 true， listener 会在其被调用之后自动移除。
     */
    fun addEventListener(type: String, listener: V8Function, once: Boolean)

    /**
     * EventTarget中删除事件侦听器。
     * @param type 一个字符串，表示需要移除的事件类型，如 "click"。
     * @param listener 需要从目标事件移除的函数。
     */
    fun removeEventListener(type: String, listener: V8Function)

    /**
     * 将事件分派到此EventTarget。
     * @param event 要被派发的事件对象。
     * @return 当该事件是可取消的(cancelable为true)并且至少一个该事件的事件处理方法调用了Event.preventDefault()，则返回值为false；否则返回true。
     */
    fun dispatchEvent(event: IEvent): Boolean
}