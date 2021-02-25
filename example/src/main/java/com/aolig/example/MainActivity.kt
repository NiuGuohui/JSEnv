package com.aolig.example

import android.app.Activity
import android.os.Bundle
import com.aolig.jsenv.JSEnv

class MainActivity() : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JSEnv()
    }

    override fun onResume() {
        super.onResume()
//        finish()
    }
}