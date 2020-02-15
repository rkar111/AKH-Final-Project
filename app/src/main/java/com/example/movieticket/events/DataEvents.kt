package com.example.movieticket.events

import com.example.movieticket.data.vos.BookingsVO
import com.example.movieticket.data.vos.MoviesVO
import com.example.movieticket.data.vos.ShowTimeVO
import com.example.movieticket.data.vos.UserVO


class DataEvents {


    class MovieListLoadedEvent(val movieList: MutableList<MoviesVO>)
    class ShowTimeListLoadedEvent(val showTimeList: MutableList<ShowTimeVO>)
    class bookingListLoaded(val bookingsList: MutableList<BookingsVO>)

}
