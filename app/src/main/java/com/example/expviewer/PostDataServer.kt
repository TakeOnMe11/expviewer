package com.example.expviewer

import android.content.Context

class PostDataServer (val context: Context) {
    private var expDB: ExpDB
    init {
        expDB = ExpDB(context)
    }

    fun postData() {

    }

}