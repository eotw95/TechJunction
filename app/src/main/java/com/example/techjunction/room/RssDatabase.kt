package com.example.techjunction.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RssChannel::class, RssItem::class],
    version = 1,
    exportSchema = false
    )
abstract class RssDatabase: RoomDatabase() {
    companion object {
        var instance: RssDatabase? = null

        fun getInstance(context: Context): RssDatabase {
            instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    RssDatabase::class.java,
                    "rss-database"
                ).build()
            }
            return instance as RssDatabase
        }
    }

    abstract fun rssChannelDao(): RssChannelDao
    abstract fun rssItemDao(): RssItemDao
}