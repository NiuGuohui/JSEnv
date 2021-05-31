# JSEnv
基于J2V8封装的包含常用BOM Api的JavaScript运行环境

## 架构说明

### 事件循环(EventLoop)
JSEnv的事件循环模型是借助Android子线程的Looper实现，
所有的异步（在Native层上）均是通过JavaScriptCallBack实现的，
由于J2V8的Api只能在同一个线程内调用，在其他线程只能通过这个线程的handler来执行JSFunction或向JS环境注入内容等等，
故若需要增加自定义的内容，则功能类必须继承JSInterface抽象类，然后在实例化JSEnv对象时注入其中。
（参考JSTimer等类的实现）

### API的实现
<table>
    <tr>
        <td>console[log、info、warn、error]</td>
        <td> :white_check_mark: </td>
    </tr>
    <tr>
        <td>setTimeout、setInterval</td>
        <td> :white_check_mark: </td>
    </tr>
</table>

### 接口/内置对象
<table>
    <tr>
        <td>Event</td>
        <td> :x: </td>
    </tr>
</table>