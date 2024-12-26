package com.hear_word_and_type.app.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hear_word_and_type.app.data.db.entities.WordModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWords(words: MutableList<WordModel>)

    @Query("SELECT * FROM Words WHERE `bookmarked`=:bookmarked")
    fun getAllBookmarkedWords(bookmarked: Boolean): LiveData<List<WordModel>>

    @Query("SELECT * FROM Words")
    fun getAllWords(): LiveData<List<WordModel>>

    @Query("SELECT * FROM Words WHERE `index`=:index")
    fun getWord(index: Int): LiveData<WordModel>

    @Query("UPDATE Words SET bookmarked=:value WHERE `index`=:index")
    fun updateBookmark(value: Boolean, index: Int)
}