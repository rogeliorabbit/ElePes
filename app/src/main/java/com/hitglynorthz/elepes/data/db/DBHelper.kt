package com.hitglynorthz.elepes.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hitglynorthz.elepes.converters.Converter
import com.hitglynorthz.elepes.models.Library

@Database(entities = [Library::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class DBHelper: RoomDatabase() {

    abstract fun libraryDao(): LibraryDao

    companion object {

        @Volatile
        private var INSTANCE: DBHelper? = null

        fun getDatabase(context: Context): DBHelper {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBHelper::class.java,
                    "library_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}