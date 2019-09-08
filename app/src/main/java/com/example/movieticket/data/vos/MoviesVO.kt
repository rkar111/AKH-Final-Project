package com.example.movieticket.data.vos

import com.google.gson.annotations.SerializedName
import java.util.*

class MoviesVO {
    @SerializedName("movieDirector")
    var movieDirector: String? = null

    @SerializedName("movieDuration")
    var movieDuration: String? = null

    @SerializedName("movieId")
    var movieId: String? = null

    @SerializedName("movieName")
    var movieName: String? = null

    @SerializedName("moviePoster")
    var moviePoster: String? = null

    @SerializedName("movieDesc")
    var movieDesc: String? = null


    @SerializedName("language")
    var language: String? = null

    @SerializedName("imdbRate")
    var imdbRate: String? = null


    @SerializedName("cinema")
    var cinema: List<CinemasVO>? = null

    @SerializedName("tickets")
    var tickets: List<TicketVO>? = null

    @SerializedName("showTime")
    var showTime: List<ShowTimeVO>? = null


}