package com.example.movieticket.events

import com.example.movieticket.data.vos.MoviesVO


/**
 * Created by Hein Htet Oo on 11/28/2017.
 */

class DataEvents {


    class MovieListLoadedEvent(val movieList: List<MoviesVO>)


}
