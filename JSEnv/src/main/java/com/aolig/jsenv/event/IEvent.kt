package com.aolig.jsenv.event

import com.eclipsesource.v8.V8Object

/**
 * Event接口(不包含标准中已废弃的和关联DOM的API)
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Event">Event</a>
 */
interface IEvent {
    /**
     * 表示事件是否可以取消
     */
    val cancelable: Boolean

    /**
     * 对事件当前注册的目标的引用。
     * 这是一个当前计划将事件发送到的对象。
     * 它是有可能在重定向的过程中被改变的。
     */
    var currentTarget: V8Object?

    /**
     * 对事件原始目标的引用，这里的原始目标指最初派发（dispatch）事件时指定的目标。
     */
    var target: V8Object?

    /**
     * 事件创建时的时间戳（精度为毫秒）。
     * 按照规范，这个时间戳是 Unix 纪元起经过的毫秒数，但实际上，在不同的浏览器中，对此时间戳的定义也有所不同。
     */
    var timeStamp: Long

    /**
     * 事件的类型，不区分大小写。
     */
    var type: String?

    /**
     * 当事件是由用户行为生成的时候，这个属性的值为 true ，
     * 而当事件是由脚本创建、修改、通过 EventTarget.dispatchEvent() 派发的时候，
     * 这个属性的值为 false 。
     */
    var isTrusted: Boolean


    /**
     * 表示 event.preventDefault() 方法是否取消了事件的默认行为。
     */
    var defaultPrevented: Boolean

    /**
     * 取消事件（如果该事件可取消）。
     */
    fun preventDefault()
}