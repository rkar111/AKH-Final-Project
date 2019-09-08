package com.example.movieticket.adapters

import android.content.Context
import android.view.ViewGroup
import com.example.movieticket.R
import com.example.movieticket.data.vos.BookingsVO
import com.example.movieticket.viewholders.BaseViewHolder
import com.example.movieticket.viewholders.ItemMyBookingViewHolder

class MyBookingListAdapter(context: Context) :
    BaseRecyclerAdapter<ItemMyBookingViewHolder, BookingsVO>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMyBookingViewHolder {
        val myBookingItemView = mLayoutInflator.inflate(R.layout.item_my_booking, parent, false)
        return ItemMyBookingViewHolder(myBookingItemView)
    }
}