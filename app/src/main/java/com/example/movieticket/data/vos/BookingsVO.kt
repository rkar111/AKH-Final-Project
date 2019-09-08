package com.example.movieticket.data.vos

class BookingsVO(
    var bookingId: String? = null,
    var cinemaName: String? = null,
    var movieName: String? = null,
    var showTime: String? = null,
    var seatList: List<String>? = null

) {

    companion object {
        fun initBooking(
           /* bookingId: String?,*/
            cinemaName: String?,
            movieName: String,
            showTime: String?,
            seatList: List<String>
        ): BookingsVO {
            val bookingVO = BookingsVO()
            bookingVO.bookingId = (System.currentTimeMillis() / 1000).toInt().toString()
            bookingVO.cinemaName = cinemaName
            bookingVO.movieName = movieName
            bookingVO.showTime = showTime
            bookingVO.seatList = seatList

            return bookingVO
        }
    }
}