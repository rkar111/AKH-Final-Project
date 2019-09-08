package com.example.movieticket.data.model

import android.content.Context
import android.util.Log
import com.example.movieticket.MovieTicketApp
import com.example.movieticket.R
import com.example.movieticket.data.vos.*
import com.example.movieticket.events.DataEvents
import com.example.movieticket.utils.PrefUtils
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import java.lang.Exception


class MovieListModel private constructor(context: Context) {


    var mDatabaseReference: DatabaseReference? = null
    lateinit var mMovieListDR: DatabaseReference
    lateinit var mTicketListDR: DatabaseReference
    lateinit var mBookingListDR: DatabaseReference
    val MOVIES: String = "Movies"


    var mFirebaseAuth: FirebaseAuth? = null
    var mFirebaseUser: FirebaseUser? = null

    val USERS: String = "Users"
    var mUser: UserVO? = null

    var moviesList = ArrayList<MoviesVO>()
    var bookingList = ArrayList<BookingsVO>()
    var showTimeList = ArrayList<ShowTimeVO>()

    companion object {

        private var INSTANCE: MovieListModel? = null


        fun getInstance(): MovieListModel {
            if (INSTANCE == null) {
                throw RuntimeException("MovieListModel is invoked before initializing")
            }
            /*val i = INSTANCE*/
            return INSTANCE!!
        }

        fun initMovieListModel(context: Context) {
            INSTANCE = MovieListModel(context)
        }

    }

    init {
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth!!.currentUser
        if (FirebaseAuth.getInstance().currentUser != null) {
            MovieTicketApp.userId = FirebaseAuth.getInstance().currentUser!!.uid
        }
    }

    fun loadMoviesList() {
        mMovieListDR = mDatabaseReference!!.child(MOVIES)
        var moviesVOS = ArrayList<MoviesVO>()
        mMovieListDR.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                moviesVOS.clear()
                if (dataSnapshot != null) {
                    Log.e("Count", "" + dataSnapshot.childrenCount)
                    dataSnapshot?.children?.forEach { snapShot: DataSnapshot ->
                        var movies: MoviesVO = snapShot.getValue(MoviesVO::class.java)!!
                        moviesVOS.add(movies)
                    }

                    var events: DataEvents.MovieListLoadedEvent =
                        DataEvents.MovieListLoadedEvent(moviesVOS)
                    EventBus.getDefault().post(events)
                    moviesList.clear()
                    moviesList.addAll(moviesVOS)

                    Log.e("MovieTicketApp", "Data" + moviesList.size)
                }

            }
        })
    }

    fun loadBookingList(userid: String) {
        mBookingListDR = mDatabaseReference!!.child("Users").child(userid!!).child("bookings")
        var BookingsVO = ArrayList<BookingsVO>()
        mBookingListDR.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                BookingsVO.clear()
                if (dataSnapShot != null) {
                    dataSnapShot?.children?.forEach { snapShot: DataSnapshot ->
                        /*val user = UserVO()
                        user.userId = snapShot.key.toString()
                        user.userName = snapShot.child("userName").value.toString()
                        user.email = snapShot.child("email").value.toString()
                        var bookingList = HashMap<String, BookingsVO>()
                        for (booking in snapShot.child("bookings").children) {
                            bookingList[booking.key.toString()] =
                                booking.getValue(BookingsVO::class.java)!!
                        }
                        user.bookings = bookingList
                        Log.e("MovieTicketApp", "booking data" + bookingList.size)*/
                        val bookings: BookingsVO =
                            snapShot.getValue(com.example.movieticket.data.vos.BookingsVO::class.java)!!
                        BookingsVO.add(bookings)
                    }
                }

                val events: DataEvents.bookingListLoaded =
                    DataEvents.bookingListLoaded(BookingsVO)
                EventBus.getDefault().post(events)
                bookingList.clear()
                bookingList = BookingsVO

                Log.e("MovieTicketApp", "user data" + bookingList.size)
            }

        })
    }

/* fun loadShowTimeList() {
     mShowTimeDR = mDatabaseReference!!.child("ShowTime")
     var showTimeVO = ArrayList<ShowTimeVO>()
     mShowTimeDR.addValueEventListener(object : ValueEventListener {
         override fun onCancelled(p0: DatabaseError) {

         }

         override fun onDataChange(dataSnapshot: DataSnapshot) {
             showTimeVO.clear()
             if (dataSnapshot != null) {
                 Log.e("Count", "" + dataSnapshot.childrenCount)
                 dataSnapshot?.children?.forEach { snapShot: DataSnapshot ->
                     var showTime: ShowTimeVO = snapShot.getValue(ShowTimeVO::class.java)!!
                     showTimeVO.add(showTime)
                 }

                 var events: DataEvents.ShowTimeListLoadedEvent =
                     DataEvents.ShowTimeListLoadedEvent(showTimeVO)
                 EventBus.getDefault().post(events)
                 showTimeList.clear()
                 showTimeList.addAll(showTimeVO)

                 Log.e("MovieTicketApp", "Data" + showTimeList.size)
             }

         }
     })
 }*/

    fun getMovieList(): MutableList<MoviesVO> {
        return moviesList
    }

    fun getMovieById(id: String?): MoviesVO {
        var movie = MoviesVO()
        for (movieVO in moviesList) {
            if (movieVO.movieId.equals(id)) {
                movie = movieVO
            }
        }
        return movie
    }

    /*  fun getUserById(id: String?): UserVO {
          var user = UserVO()
          for (userVo in bookingList) {
              if (userVo.userId.equals(id)) {
                  user = userVo
              }
          }
          return user
      }*/

/*  fun getShowTimeById(id: Int?): ShowTimeVO {
      var showTime = ShowTimeVO()
      for (showTimeVO in showTimeList) {
          if (showTimeVO.showTimeId == id) {
              showTime = showTimeVO
          }
      }
      return showTime
  }*/


    fun addBooking(
        /* bookingId: String?,*/
        userId: String?,
        cinemaName: String,
        movieName: String,
        showTime: String,
        seatList: List<String>
    ) {
        val bookingVO = BookingsVO.initBooking(
            cinemaName, movieName, showTime, seatList
        )
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(userId!!).child("bookings").child(bookingVO.bookingId!!)
            .setValue(bookingVO)
    }

    fun updateSeatByMovieId(movieId: String, updatedTicketList: List<TicketVO>) {
        FirebaseDatabase.getInstance().reference.child("Movies").child(movieId).child("tickets")
            .setValue(updatedTicketList)
    }


    fun isUserSignIn(): Boolean {
        return mUser != null
    }

    fun firebaseAuthWithGoogle(
        acct: GoogleSignInAccount,
        delegate: SignInWithGoogleAccountDelegate
    ) {
        Log.d("App", "firebaseAuthWithGoogle" + acct.id)
        delegate.showProgressDialog("Authenticating...")

        var credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mFirebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        Log.e("TicketApp", "signinwtihgoogle:success")
                        checkExistingUser(acct, delegate)

                    } else {
                        Log.e("TicketApp", "signinwithgoogle:fail")
                        delegate.onFailureSignIn(task.exception!!.message!!)
                    }
                    delegate.hideProgressDialog()
                }

            })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    delegate.onFailureSignIn(p0.message!!)
                }
            })
    }

    private fun checkExistingUser(
        signInAccount: GoogleSignInAccount,
        delegate: SignInWithGoogleAccountDelegate
    ) {
        //String formattedEmail = FirebaseUtils.replaceDotsWithCommas(signInAccount.getEmail());
        val singleAccountDR = mDatabaseReference!!.child(signInAccount.id!!)
        singleAccountDR.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserVO::class.java)
                if (user != null) {
                    delegate.onSuccessSignIn(user)
                    mUser = user
                } else {
                    val photoUri = signInAccount.photoUrl
                    val photoUrl = photoUri?.toString()

                    val newUser = UserVO(
                        signInAccount.id, signInAccount.displayName,
                        signInAccount.email
                    )
                    registerNewUser(newUser)
                    delegate.onSuccessSignIn(newUser)
                    mUser = newUser
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                delegate.onFailureSignIn(databaseError.message)
            }
        })
    }

    fun syncUserInfo(accountId: String?, delegate: SyncUserInfoDelegate) {
        val singleAccountDR = mDatabaseReference!!.child(accountId!!)
        singleAccountDR.keepSynced(true)
        singleAccountDR.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserVO::class.java)
                if (user != null) {
                    delegate.syncUserInfo(user)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    fun registerNewUser(user: UserVO) {
        mDatabaseReference!!.child("Users").child(user.userId.toString()).setValue(user)
    }

    fun saveSignInStateInPref(context: Context, flag: Boolean) {
        val editor = PrefUtils.getSharedPrefs(context).edit()
        editor.putBoolean(
            context.getString(R.string.sign_in_state_key), flag
        )
        editor.apply()
    }

    fun saveAccountIdInPref(context: Context, userVO: UserVO) {
        val editor = PrefUtils.getSharedPrefs(context).edit()
        editor.putString(
            context.getString(R.string.account_id_key),
            userVO.userId
        )
        editor.apply()
    }

    fun getSignInStateIdFromPref(context: Context): Boolean {
        val sharedPreferences = PrefUtils.getSharedPrefs(context)
        return sharedPreferences.getBoolean(
            context.getString(R.string.sign_in_state_key), false
        )
    }

    fun getAccountIdFromPref(context: Context): String? {
        val sharedPreferences = PrefUtils.getSharedPrefs(context)
        return sharedPreferences.getString(
            context.getString(R.string.account_id_key),
            null
        )
    }

    fun loadUserFromPref(id: String) {
        mDatabaseReference!!.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserVO::class.java)
                if (user != null) {
                    mUser = user
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    interface SignInWithGoogleAccountDelegate {
        fun onSuccessSignIn(user: UserVO)

        fun onFailureSignIn(msg: String)

        fun showProgressDialog(msg: String)

        fun hideProgressDialog()
    }

    interface SyncUserInfoDelegate {
        fun syncUserInfo(user: UserVO)
    }

}