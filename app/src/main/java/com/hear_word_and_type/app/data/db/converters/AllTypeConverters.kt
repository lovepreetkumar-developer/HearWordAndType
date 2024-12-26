package com.hear_word_and_type.app.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hear_word_and_type.app.data.db.entities.WordModel

class AllTypeConverters {

    companion object {
        private val gson = Gson()

        /*** ProductPriceModel TypeConverts */
        @TypeConverter
        @JvmStatic
        fun stringToWordModel(data: String?): List<WordModel?>? {
            if (data == null) {
                return emptyList<WordModel>()
            }
            val listType = object : TypeToken<List<WordModel?>?>() {}.type
            return gson.fromJson<List<WordModel?>>(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun wordModelToString(someObjects: List<WordModel?>?): String? {
            return gson.toJson(someObjects)
        }
    }
}