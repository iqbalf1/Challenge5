package com.binar.chapter5.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binar.chapter5.model.MovieModel
import com.binar.chapter5.model.Result
import com.binar.chapter5.network.MovieApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SecondViewModel : ViewModel() {
    private val mId = MutableLiveData<Int>()

    private val movies: MutableLiveData<List<Result>> by lazy {
        MutableLiveData<List<Result>>().also {
            getAllMovies()
        }
    }


    private fun getAllMovies() {
        MovieApi.retrofitService.allMovies().enqueue(object : Callback<MovieModel> {
            override fun onResponse(
                call: Call<MovieModel>,
                response: Response<MovieModel>
            ) {
                movies.value = response.body()?.results
            }

            override fun onFailure(call: Call<MovieModel>, t: Throwable) {
                Log.d("Tag", t.message.toString())
            }

        })
    }

    fun getMovies(): LiveData<List<Result>> {
        return movies;
    }


}