package com.example.movieticket.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieticket.R
import com.example.movieticket.adapters.MovieListAdapter
import com.example.movieticket.adapters.MyBookingListAdapter
import com.example.movieticket.data.model.MovieListModel
import com.example.movieticket.data.vos.BookingsVO
import com.example.movieticket.data.vos.MoviesVO
import com.example.movieticket.data.vos.UserVO
import com.example.movieticket.events.DataEvents
import kotlinx.android.synthetic.main.activity_my_booking.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MyBookingActivity : BaseActivity() {

    private var mBookingsList: MutableList<BookingsVO>? = null

    private lateinit var mAdapter: MyBookingListAdapter
    private var mMovieModel: MovieListModel? = null

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, MyBookingActivity::class.java)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_booking)
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        mMovieModel = MovieListModel.getInstance()
        mAdapter = MyBookingListAdapter(applicationContext)

        rv_my_booking.layoutManager = LinearLayoutManager(applicationContext)
        rv_my_booking.adapter = mAdapter
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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        mMovieModel!!.loadBookingList(mMovieModel!!.getAccountIdFromPref(applicationContext)!!)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        mAdapter.clearData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnBookingListListLoaded(event: DataEvents.bookingListLoaded) {
        mBookingsList = event.bookingsList
        mAdapter.setNewData(mBookingsList!!)
    }
}