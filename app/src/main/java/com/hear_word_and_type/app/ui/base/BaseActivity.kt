package com.hear_word_and_type.app.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.hear_word_and_type.app.R

abstract class BaseActivity : AppCompatActivity() {

    protected var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        this.context = this
        onViewReady(savedInstanceState, intent)

        try {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goBackAnimation()
    }

    @CallSuper
    open fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {
    }

    abstract fun getContentView(): Int

    protected open fun goBack() {
        onBackPressed()
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out)
    }

    protected open fun finishActivityWithBackAnim() {
        finish()
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out)
    }

    protected open fun goNextAnimation() {
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out)
    }

    protected open fun goBackAnimation() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out)
    }

}