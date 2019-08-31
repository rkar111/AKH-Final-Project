package com.example.movieticket.delegates

import com.example.movieticket.data.vos.MoviesVO

interface MovieListItemDelegate {
    fun onTapMovieListItem(moviesVO: MoviesVO)
    fun onTapBookTicket()
}