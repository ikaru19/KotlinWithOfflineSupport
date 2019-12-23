package com.ikaru19.kotlinroomandretrofit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface PostDao {

    @Insert
    fun insert(postModel: PostModel)

    @Query("DELETE FROM posts")
    fun deleteAll()

    @Query("SELECT * FROM posts")
    fun getAllPosts() : List<PostModel>
}