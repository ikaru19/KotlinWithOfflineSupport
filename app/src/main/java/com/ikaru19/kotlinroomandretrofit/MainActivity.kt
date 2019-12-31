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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var myrepo = PostRepository(this)
        myrepo.getPostFromInternet()
        var posts = myrepo.GetDataFromDB().execute().get()


            if (posts!!.size > 0){
                Toast.makeText(this,"From Local DB",Toast.LENGTH_SHORT).show()
                tv_hasil.setText("")
                for (i in 0..posts.size - 1){
                   tv_hasil.append(posts[i].id.toString() + "\n")
                }
            }else{
                Toast.makeText(this,"You need to connect internet first",Toast.LENGTH_SHORT).show()
            }

    }

}
