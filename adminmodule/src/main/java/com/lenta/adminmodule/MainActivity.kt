package com.lenta.adminmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firedata.StoreObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    companion object {
        var obj: StoreObject? = null
    }
}