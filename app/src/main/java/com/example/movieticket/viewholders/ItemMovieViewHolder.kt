package com.example.movieticket.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieticket.data.vos.MoviesVO
import com.example.movieticket.delegates.MovieListItemDelegate
import kotlinx.android.synthetic.main.items_movies.view.*

class ItemMovieViewHolder(itemView: View, private val mDelegate: MovieListItemDelegate) :
    BaseViewHolder<MoviesVO>(itemView) {

    override fun setData(data: MoviesVO) {

        mData = data
        itemView.tv_movie_name.text = data.movieName
        itemView.tv_movie_info.text = data.movieDuration
        if (data.moviePoster != null) {
            itemView.iv_poster.visibility = View.VISIBLE
            Glide.with(itemView.iv_poster.context)
                .load(data.moviePoster)
                .into(itemView.iv_poster)
        } else {
            itemView.iv_poster.visibility = View.GONE
        }

    }

    override fun onClick(v: View?) {

    }

}