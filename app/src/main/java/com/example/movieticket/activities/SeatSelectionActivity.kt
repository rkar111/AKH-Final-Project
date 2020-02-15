package com.example.movieticket.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.movieticket.R
import com.example.movieticket.data.model.MovieListModel
import com.example.movieticket.data.vos.*
import com.example.movieticket.events.DataEvents
import kotlinx.android.synthetic.main.activity_seat_selection.*
import kotlinx.android.synthetic.main.activity_seat_selection.toolbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SeatSelectionActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener {


    var mMovie: MoviesVO? = null
    private var mModel: MovieListModel? = null
    var mticketList: List<TicketVO>? = null
    var updatedTicketList = ArrayList<TicketVO>()
    var checkBoxList = ArrayList<CheckBox>()
    var checkBoxCheckList=ArrayList<CheckBox>()
    var booleanArray = ArrayList<Boolean>()
    var bookedSeatList = ArrayList<String>()
    var bookingList = ArrayList<BookingsVO>()


    companion object {
        private val IE_MOVIE_ID = "movieId"
        fun newIntent(context: Context, moviesId: String?): Intent {
            val intent = Intent(context, SeatSelectionActivity::class.java)
            intent.putExtra(IE_MOVIE_ID, moviesId)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_selection)
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        mModel = MovieListModel.getInstance()

        val movieId = intent.getStringExtra(IE_MOVIE_ID)
        mMovie = MovieListModel.getInstance().getMovieById(movieId)
        Log.e("app", "Name" + mMovie!!.movieName)


        mticketList = mMovie!!.tickets
        checkBoxList.add(seat1)
        checkBoxList.add(seat2)
        checkBoxList.add(seat3)
        checkBoxList.add(seat4)
        checkBoxList.add(seat5)
        checkBoxList.add(seat6)
        checkBoxList.add(seat7)
        checkBoxList.add(seat8)
        checkBoxList.add(seat9)
        checkBoxList.add(seat10)

        bindData()

        for (j in 0 until 10) {
            checkBoxList[j].setButtonDrawable(R.drawable.seat)
        }

        for (i in mticketList!!.indices) {
            Log.e("Available", "mTicket" + mticketList!![i].available.toString())
            if (mticketList!![i].available!!) {
                checkBoxList[i].buttonDrawable = getDrawable(R.drawable.seat)
                checkBoxList[i].isEnabled = true
            } else if (!mticketList!![i].available!!) {
                checkBoxList[i].setButtonDrawable(getDrawable(R.drawable.gray_box))
                checkBoxList[i].isEnabled = false
            }
        }

        button2.setOnClickListener {
         /*   for (f in 0 until 10)
                if (checkBoxList[f].isChecked != true)  {
                    Toast.makeText(applicationContext, "Please Select Seat", Toast.LENGTH_SHORT)
                        .show()
                } else {*/
                    for (b in 0 until 10) {
                        Log.e("Check", "$b Box")
                        //booleanArray[b] = !(checkBoxList[b].isChecked || !checkBoxList[b].isEnabled)
                        booleanArray.add(
                            b,
                            !(checkBoxList[b].isChecked || !checkBoxList[b].isEnabled)
                        )
                        if (checkBoxList[b].isChecked) {
                            bookedSeatList.add(mticketList!![b].seatId.toString())
                        }
                    }
                    for (c in 0 until 10) {
                        mticketList!![c].available = booleanArray[c]
                        if (!booleanArray[c]) {
                            mticketList!![c].buyerId =
                                MovieListModel.getInstance()
                                    .getAccountIdFromPref(applicationContext)
                        }
                    }


                    mModel!!.addBooking(
                        // (mMovie!!.movieId.toString() + mMovie!!.showTime!![0].showTimeId.toString()),
                        MovieListModel.getInstance().getAccountIdFromPref(applicationContext),
                        mMovie!!.cinema!![0].cinemaName!!,
                        mMovie!!.movieName!!,
                        mMovie!!.showTime!![0].showTime!!,
                        bookedSeatList
                    )
                    mModel!!.updateSeatByMovieId(mMovie!!.movieId!!, mticketList!!)

                    val intent = BookingResultActivity.newIntent(
                        this
                    )
                    startActivity(intent)


        }

    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

    }

    fun bindData() {
        tv_movie_name.text = mMovie!!.movieName
        tv_show_time.text = mMovie!!.showTime!![0].showTime
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


}