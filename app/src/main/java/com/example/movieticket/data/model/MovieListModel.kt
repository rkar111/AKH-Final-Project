package com.example.movieticket.data.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movieticket.data.vos.MoviesVO
import com.example.movieticket.events.DataEvents
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus

class MovieListModel private constructor(context: Context) {


    var mDatabaseReference: DatabaseReference? = null
    lateinit var mMovieListDR: DatabaseReference
    val MOVIES: String = "Movies"
    var moviesList = ArrayList<MoviesVO>()


    companion object {

        private var INSTANCE: MovieListModel? = null


        fun getInstance(): MovieListModel {
            if (INSTANCE == null) {
                throw RuntimeException("MovieListModel is invoked before initializing")
            }
            /*val i = INSTANCE*/
            return INSTANCE!!
        }

        fun initMovieListModel(context: Context) {
            INSTANCE = MovieListModel(context)
        }

    }

    init {
        mDatabaseReference = FirebaseDatabase.getInstance().reference
    }

    fun loadMoviesList() {
        mMovieListDR = mDatabaseReference!!.child(MOVIES)
        val moviesVOS = ArrayList<MoviesVO>()
        mMovieListDR.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot != null) {
                    dataSnapshot?.children?.forEach { snapShot: DataSnapshot ->
                        var movies: MoviesVO = snapShot.getValue(MoviesVO::class.java)!!
                        moviesVOS.add(movies)
                    }

                    var events: DataEvents.MovieListLoadedEvent =
                        DataEvents.MovieListLoadedEvent(moviesVOS)
                    EventBus.getDefault().post(events)
                    moviesList = moviesVOS
                    Log.e("MovieTicketApp", "Data" + moviesList.size)
                }

            }
        })
    }

    fun getMovieList(): List<MoviesVO> {
        return moviesList
    }


}