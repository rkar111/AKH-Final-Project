package com.example.movieticket.activities

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieticket.R
import com.example.movieticket.adapters.MovieListAdapter
import com.example.movieticket.data.model.MovieListModel
import com.example.movieticket.data.vos.MoviesVO
import com.example.movieticket.delegates.MovieListItemDelegate
import com.example.movieticket.events.DataEvents


import kotlinx.android.synthetic.main.activity_movie_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MovieListActivity : BaseActivity(), MovieListItemDelegate {


    private lateinit var mAdapter: MovieListAdapter
    private var mMovieModel: MovieListModel? = null
    private var mMovieList: List<MoviesVO>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        setSupportActionBar(toolbar)

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        mMovieList = ArrayList()
        mMovieModel = MovieListModel.getInstance()

        rv_movies.layoutManager = LinearLayoutManager(applicationContext)
        mAdapter = MovieListAdapter(applicationContext, this)
        rv_movies.adapter = mAdapter


    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        mMovieModel!!.loadMoviesList()
        Log.e("bind data", mMovieList!!.size.toString())
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onTapMovieListItem(moviesVO: MoviesVO) {

    }

    override fun onTapBookTicket() {

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnMovieListLoaded(event: DataEvents.MovieListLoadedEvent) {
        mMovieList = event.movieList
        mAdapter.setNewData(mMovieList!!)

    }
}
