package com.hear_word_and_type.app.data.respositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.hear_word_and_type.app.data.db.AppDatabase
import com.hear_word_and_type.app.data.db.entities.WordModel
import com.hear_word_and_type.app.util.Coroutines

class MainRepository(context: Context) {

    private var appDatabase: AppDatabase = AppDatabase(context)

    fun insertWords(list: MutableList<WordModel>) {
        Coroutines.io {
            appDatabase.getUserDao().insertWords(list)
        }
    }

    fun getAllWords(): LiveData<List<WordModel>> {
        return appDatabase.getUserDao().getAllWords()
    }

    fun getWord(index: Int): LiveData<WordModel> {
        return appDatabase.getUserDao().getWord(index)
    }

    fun getAllBookmarkedWords(bookmarked: Boolean): LiveData<List<WordModel>> {
        return appDatabase.getUserDao().getAllBookmarkedWords(bookmarked)
    }

    fun updateBookmark(value: Boolean, index: Int) {
        Coroutines.io {
            appDatabase.getUserDao().updateBookmark(value, index)
        }
    }
}