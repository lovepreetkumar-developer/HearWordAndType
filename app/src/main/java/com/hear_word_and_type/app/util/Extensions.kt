package com.hear_word_and_type.app.util

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

/* load json from assets folder*/
@Throws(java.lang.Exception::class)
fun loadFileFromAsset(context: Context, fileName: String?): String? {
    val json: String
    val `is` = context.assets.open(fileName!!)
    val size = `is`.available()
    val buffer = ByteArray(size)
    `is`.read(buffer)
    `is`.close()
    json = String(buffer, StandardCharsets.UTF_8)
    return json
}

