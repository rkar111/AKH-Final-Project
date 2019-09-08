package com.example.movieticket.data.vos

import com.google.gson.annotations.SerializedName

class CinemasVO {

    @SerializedName("cinemaId")
    var cinemaId: Int? = null

    @SerializedName("cinemaName")
    var cinemaName: String? = null
}