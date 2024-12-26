package com.hear_word_and_type.app.data.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Words")
data class WordModel(
    var index: Int,
    var bookmarked: Boolean,
    @PrimaryKey()
    @ColumnInfo(name = "word")
    var word: String
) : Parcelable {
    constructor() : this(
        0,
        false,
        ""
    )
}