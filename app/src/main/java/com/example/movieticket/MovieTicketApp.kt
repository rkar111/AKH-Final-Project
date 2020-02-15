package com.example.movieticket

import android.app.Application
import com.example.movieticket.data.model.MovieListModel

class MovieTicketApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MovieListModel.initMovieListModel(applicationContext)
    }
    companion object {
        var userId = "1"
    }
}