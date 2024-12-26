package com.hear_word_and_type.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.hear_word_and_type.app.util.Constants

class PreferenceProvider(context: Context) {

    private val appContext = context.applicationContext

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun clear(): Boolean {
        return preferences.edit().clear().commit()
    }

    fun setCurrentWordIndex(index: Int): Boolean {
        return preferences.edit().putInt(
            Constants.CURRENT_WORD_INDEX,
            index
        ).commit()
    }

    fun getCurrentWordIndex(): Int {
        return preferences.getInt(Constants.CURRENT_WORD_INDEX, 0)
    }
}