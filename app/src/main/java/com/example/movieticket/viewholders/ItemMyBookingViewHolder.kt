package com.example.movieticket.viewholders

import android.view.View
import com.example.movieticket.data.vos.BookingsVO
import kotlinx.android.synthetic.main.item_my_booking.view.*

class ItemMyBookingViewHolder(itemView: View) :
    BaseViewHolder<BookingsVO>(itemView) {
    override fun setData(data: BookingsVO) {
        mData = data
        itemView.tv_booking_id.text = data.bookingId
        itemView.tv_movie_my_booking.text = data.movieName
        itemView.tv_cinema_my_booking.text = data.cinemaName
        itemView.tv_show_time_my_booking.text = data.showTime
    }

    override fun onClick(p0: View?) {

    }
}