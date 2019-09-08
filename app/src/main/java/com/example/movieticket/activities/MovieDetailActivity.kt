package com.example.movieticket.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.example.movieticket.R
import com.example.movieticket.data.model.MovieListModel
import com.example.movieticket.data.vos.MoviesVO
import com.example.movieticket.data.vos.ShowTimeVO
import com.example.movieticket.delegates.ShowTimeItemDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_movie_detail.*


class MovieDetailActivity : BaseActivity() {


    lateinit var mMovie: MoviesVO
    private var mMovieList: MutableList<MoviesVO>? = null
    private var mShowTimeList: MutableList<ShowTimeVO>? = null

    private var mModel: MovieListModel? = null
    private var mDelegate: ShowTimeItemDelegate? = null
    private var mFirebaseUser: FirebaseUser? = null

    /*   private var qty: String? = null
       private var x: Int? = null
       private var total: Int? = null*/

    companion object {
        private val IE_MOVIE_ID = "movieId"
        fun newIntent(context: Context, movieId: String): Intent {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(IE_MOVIE_ID, movieId)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        val movieId = intent.getStringExtra(IE_MOVIE_ID)
        mMovie = MovieListModel.getInstance().getMovieById(movieId)
        Log.e("Movie", "Data" + mMovie)

        cv_time.setOnClickListener {
            val intent = SeatSelectionActivity.newIntent(this, mMovie.movieId!!)
            startActivity(intent)
        }

        mFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        mModel = MovieListModel.getInstance()

        bindData()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindData() {
        tv_movie_name.text = mMovie.movieName
        tv_director_holder.text = mMovie.movieDirector
        tv_duration_holder.text = mMovie.movieDuration
        tv_language_holder.text = mMovie.language
        tv_summary_holder.text = mMovie.movieDesc
        if (mMovie.moviePoster != null) {
            iv_poster.visibility = View.VISIBLE
            Glide.with(iv_poster.context)
                .load(mMovie.moviePoster)
                .into(iv_poster)
        } else {
            iv_poster.visibility = View.GONE
        }
        tv_cinema_name.text = mMovie.cinema!![0].cinemaName
        tv_imdb_rate.text = mMovie.imdbRate
        tv_show_time.text = mMovie.showTime!![0].showTime
    }


}