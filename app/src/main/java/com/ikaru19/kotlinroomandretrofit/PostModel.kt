package com.ikaru19.kotlinroomandretrofit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "posts")
data class PostModel(@ColumnInfo(name = "userId")val userId:Int,
                     @PrimaryKey @ColumnInfo(name = "id")val id:Int,
                     @ColumnInfo(name = "title")val title:String,
                     @ColumnInfo(name = "body")val body:String)