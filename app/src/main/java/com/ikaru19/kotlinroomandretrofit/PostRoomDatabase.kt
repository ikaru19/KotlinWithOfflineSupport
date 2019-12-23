package com.ikaru19.kotlinroomandretrofit

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(PostModel::class), version = 1)
abstract class PostRoomDatabase : RoomDatabase(){
    abstract fun postDao() : PostDao
    companion object{
        private var INSTANCE : PostRoomDatabase? = null

        fun getDatabase(context: Context): PostRoomDatabase?{
            if (INSTANCE == null){
                synchronized(PostRoomDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        PostRoomDatabase::class.java,"posts.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}