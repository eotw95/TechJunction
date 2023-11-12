package com.example.techjunction.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.techjunction.util.UserConverter

@Database(entities = [QiitaArticle::class], version = 1)
@TypeConverters(UserConverter::class)
abstract class QiitaArticleDatabase: RoomDatabase() {
    companion object {
        var instance: QiitaArticleDatabase? = null

        fun getInstance(context: Context): QiitaArticleDatabase {
            return instance ?: synchronized(this) {
                instance = Room.databaseBuilder(
                    context,
                    QiitaArticleDatabase::class.java,
                    "QiitaArticleDatabase"
                ).fallbackToDestructiveMigration().build()
            } as QiitaArticleDatabase
        }
    }
    abstract fun qiitaArticleDao(): QiitaArticleDao
}