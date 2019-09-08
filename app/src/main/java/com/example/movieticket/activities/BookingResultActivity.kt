package com.example.movieticket.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.movieticket.R
import com.example.movieticket.data.model.MovieListModel
import com.example.movieticket.data.vos.BookingsVO
import com.example.movieticket.data.vos.MoviesVO
import com.example.movieticket.data.vos.UserVO
import com.example.movieticket.events.DataEvents
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_booking_resullt.*
import kotlinx.android.synthetic.main.activity_movie_list.toolbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class BookingResultActivity : BaseActivity() {

    companion object {
        private val IE_MOVIE_ID = "movieId"
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, BookingResultActivity::class.java)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_resullt)
        setSupportActionBar(toolbar)

        Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT)
            .show()

        button_ok.setOnClickListener {
            val intent = MovieListActivity.newIntent(this)
            startActivity(intent)
        }
    }


}