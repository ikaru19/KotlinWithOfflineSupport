package com.ikaru19.kotlinroomandretrofit


import retrofit2.Call
import retrofit2.http.GET

interface PostServices {

    @GET("posts")
    fun getPosts(): Call<List<PostModel>>
}