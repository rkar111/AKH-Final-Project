package com.example.movieticket.data.vos

import com.google.gson.annotations.SerializedName

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

}