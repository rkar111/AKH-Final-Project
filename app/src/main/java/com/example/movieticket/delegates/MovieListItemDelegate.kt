package com.example.movieticket.delegates


import com.example.movieticket.data.vos.MoviesVO

interface MovieListItemDelegate {
    fun onTapMovieItem(moviesVO: MoviesVO)
}