package com.aolig.example

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.aolig.jsenv.JSEnv

class MainActivity() : Activity() {
    private var NODE_SCRIPT = """console.log('213%s321','Niu');
        setTimeout(()=>{
        var a = new Promise((r)=>{r(1);
        console.log('inner')});
        console.log(333);
        a.then(e=>console.log(e));
        console.log(666);
        setTimeout(()=>{console.log('2level')},1000)
        },1000)""".trimMargin()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JSEnv(NODE_SCRIPT)
    }

    override fun onResume() {
        super.onResume()
//        finish()
    }
}