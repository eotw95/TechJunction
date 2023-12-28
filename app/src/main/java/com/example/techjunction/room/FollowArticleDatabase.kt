package com.example.techjunction.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FollowArticle::class], version = 1, exportSchema = false)
abstract class FollowArticleDatabase: RoomDatabase() {
    companion object {
        var instanse: FollowArticleDatabase? = null

        fun getInstance(context: Context): FollowArticleDatabase {
            return instanse ?: synchronized(this) {
                val tmpInstance = Room.databaseBuilder(
                    context,
                    FollowArticleDatabase::class.java,
                    "FollowArticleDatabase"
                ).build()
                instanse = tmpInstance
                return instanse as FollowArticleDatabase
            }
        }
    }

    abstract fun FollowArticleDao(): FollowArticleDao
}