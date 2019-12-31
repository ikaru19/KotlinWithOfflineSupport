package com.ikaru19.kotlinroomandretrofit

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.content.SharedPreferences


class PostRepository(context: Context){
    var context : Context
    var postDatabase : PostRoomDatabase? = null
    var posts : List<PostModel>? = null
    val postServices = DataRepository.create()
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "firsttimer"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)


    init {
        this.context = context
        postDatabase = PostRoomDatabase.getDatabase(context)!!
    }


    fun getPostFromInternet(){
        postServices.getPosts().enqueue(object : Callback<List<PostModel>> {
            override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {
                if (response.isSuccessful) {
                    ClearAllData().execute()
                    Toast.makeText(context,"From Internet", Toast.LENGTH_LONG).show()
                    print("ambil internet")
                    val data = response.body()
                    data?.map {
                        InsertTask(it).execute()
                    }
                    if (sharedPref.getBoolean(PREF_NAME,true)){
                        showDialog()
                        val editor=sharedPref.edit()
                        editor.putBoolean(PREF_NAME,false)
                        editor.commit()
                    }
                }
            }

            override fun onFailure(call: Call<List<PostModel>>, error: Throwable) {
                Log.e("MYRETRO", "errornya ${error.message}")
                Toast.makeText(context,"No Internet trying get data offline", Toast.LENGTH_LONG).show()

            }
        })
    }


    private inner class InsertTask(var postModel: PostModel): AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            postDatabase!!.postDao().insert(postModel)
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!){
                Toast.makeText(context,"Added to database",Toast.LENGTH_SHORT).show()
            }
        }

    }

    inner class GetDataFromDB : AsyncTask<Void, Void, List<PostModel>>(){
        override fun doInBackground(vararg params: Void?): List<PostModel> {
            posts = postDatabase!!.postDao().getAllPosts()
            return postDatabase!!.postDao().getAllPosts()
        }

        override fun onPostExecute(result: List<PostModel>?) {
            if (result!!.size > 0){

                posts = result
            }else{
            Toast.makeText(context,"You need to connect internet first",Toast.LENGTH_SHORT).show()
        }
        }
    }

    private inner class ClearAllData : AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            postDatabase!!.postDao().deleteAll()
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!){
                Toast.makeText(context,"Clear Database",Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun showDialog(){
            val builder = AlertDialog.Builder(context)

            // Set the alert dialog title
            builder.setTitle("Update Data")

            // Display a message on alert dialog
            builder.setMessage("Update your data and restart ?")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("YES"){dialog, which ->
                // Do something when user press the positive button
                Toast.makeText(context,"Ok, we change the app background.",Toast.LENGTH_SHORT).show()
                (context as Activity).finish()
                context.startActivity(Intent(context, MainActivity::class.java))
                (context as Activity).finishAffinity()
                // Change the app background color

            }


            // Display a negative button on alert dialog
            builder.setNegativeButton("No"){dialog,which ->
                Toast.makeText(context,"You are not update your application",Toast.LENGTH_SHORT).show()
            }




            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()

    }



}