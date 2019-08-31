package com.example.movieticket.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieticket.R
import com.example.movieticket.data.vos.MoviesVO
import com.example.movieticket.delegates.MovieListItemDelegate
import com.example.movieticket.viewholders.BaseViewHolder
import com.example.movieticket.viewholders.ItemMovieViewHolder

class MovieListAdapter(context: Context, private val mDelegate: MovieListItemDelegate) :
    BaseRecyclerAdapter<ItemMovieViewHolder, MoviesVO>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMovieViewHolder {
        val movieItemView = mLayoutInflator.inflate(R.layout.items_movies, parent, false)
        return ItemMovieViewHolder(movieItemView, mDelegate)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MoviesVO>, position: Int) {
        Log.e("data bind", position.toString())
        super.onBindViewHolder(holder, position)
    }
}