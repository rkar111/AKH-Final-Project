package com.example.movieticket.delegates

import com.example.movieticket.data.vos.ShowTimeVO

interface ShowTimeItemDelegate {
    fun onTapShowTimeItem(showTimeVO: ShowTimeVO)
}