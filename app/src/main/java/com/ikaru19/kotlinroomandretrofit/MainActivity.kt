package com.ikaru19.kotlinroomandretrofit

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var postDatabase : PostRoomDatabase? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        postDatabase = PostRoomDatabase.getDatabase(this)!!


        // get post data
        val postServices = DataRepository.create()
        postServices.getPosts().enqueue(object : Callback<List<PostModel>> {

            override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {


                if (response.isSuccessful) {
                    ClearAllData(this@MainActivity).execute()
                    Toast.makeText(this@MainActivity,"From Internet",Toast.LENGTH_LONG).show()
                    val data = response.body()
                    tv_hasil.append("")
                    data?.map {
                        Log.d("MYRETRO", "datanya ${it.body}")
                        tv_hasil.append("datanya ${it.body}" + "\n")
                        InsertTask(this@MainActivity,it).execute()
                    }
                }
            }

            override fun onFailure(call: Call<List<PostModel>>, error: Throwable) {
                Log.e("MYRETRO", "errornya ${error.message}")
                Toast.makeText(this@MainActivity,"No Internet trying get data offline",Toast.LENGTH_LONG).show()
                GetDataFromDB(this@MainActivity).execute()

            }
        })
    }

    private class InsertTask(var context: MainActivity,var postModel: PostModel): AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            context.postDatabase!!.postDao().insert(postModel)
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!){
                Toast.makeText(context,"Added to database",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private class GetDataFromDB(var context: MainActivity) : AsyncTask<Void,Void,List<PostModel>>(){
        override fun doInBackground(vararg params: Void?): List<PostModel> {
            return context.postDatabase!!.postDao().getAllPosts()
        }

        override fun onPostExecute(result: List<PostModel>?) {
            if (result!!.size > 0){
                Toast.makeText(context,"From Local DB",Toast.LENGTH_SHORT).show()
                context.tv_hasil.setText("")
                for (i in 0..result.size - 1){
                    context.tv_hasil.append(result[i].body + "\n")
                }
            }else{
                Toast.makeText(context,"You need to connect internet first",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private class ClearAllData(var context: MainActivity) : AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            context.postDatabase!!.postDao().deleteAll()
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!){
                Toast.makeText(context,"Clear Database",Toast.LENGTH_SHORT).show()
            }
        }

    }
}
