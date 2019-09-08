package com.example.movieticket.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieticket.R
import com.example.movieticket.adapters.MovieListAdapter
import com.example.movieticket.components.SmartScrollListener
import com.example.movieticket.data.model.MovieListModel
import com.example.movieticket.data.vos.MoviesVO
import com.example.movieticket.data.vos.UserVO
import com.example.movieticket.delegates.MovieListItemDelegate
import com.example.movieticket.events.DataEvents
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient


import kotlinx.android.synthetic.main.activity_movie_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Suppress("DEPRECATION")
class MovieListActivity : BaseActivity(), MovieListItemDelegate,
    GoogleApiClient.OnConnectionFailedListener {

    private var mBookingsList: MutableList<UserVO>? = null

    private var RC_GOOGLE_SIGN_IN: Int = 1234
    val Google_Sign_In_Id_Token: String =
        "257025576717-jmmphija9bvja67es33psgqri0ieege1.apps.googleusercontent.com"

    var mProgressDialog: ProgressDialog? = null
    private var flag: Boolean = false

    private lateinit var mAdapter: MovieListAdapter
    private var mMovieModel: MovieListModel? = null
    private var mMovieList: MutableList<MoviesVO>? = null
    private lateinit var mGoogleApiClient: GoogleApiClient

    private var mSmartScrollListener: SmartScrollListener? = null

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, MovieListActivity::class.java)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setTitle(R.string.app_name)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_ticket)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }


        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        mMovieList = ArrayList()
        mMovieModel = MovieListModel.getInstance()



        rv_movies.layoutManager = LinearLayoutManager(applicationContext)
        mAdapter = MovieListAdapter(applicationContext, this)

        mSmartScrollListener =
            SmartScrollListener(object : SmartScrollListener.OnSmartScrollListener {
                override fun onListEndReach() {
                    // Snackbar.make(rv_movies, "End of Data", Snackbar.LENGTH_SHORT).show()
                }
            })
        rv_movies.addOnScrollListener(mSmartScrollListener!!)
        rv_movies.setEmptyView(vpEmptyNews)


        //mMovieModel!!.getMovieList()
        displayMovieList(mMovieList!!, false)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Google_Sign_In_Id_Token)
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(applicationContext)
            .enableAutoManage(this /*FragmentActivity*/, this /*OnConnectionFailedListener*/)
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build()

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        val userId = mMovieModel!!.getAccountIdFromPref(this)
        if (userId != null) {
            mMovieModel!!.loadUserFromPref(userId)
        }
        flag =
            MovieListModel.getInstance().getSignInStateIdFromPref(applicationContext)
        if (flag) {
            mMovieModel!!.syncUserInfo(mMovieModel!!.getAccountIdFromPref(this),
                object : MovieListModel.SyncUserInfoDelegate {
                    override fun syncUserInfo(user: UserVO) {

                    }

                })
        }
        mMovieModel!!.loadMoviesList()


        // Log.e("bind data", mMovieList!!.size.toString())
    }


    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        mAdapter.clearData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            processGoogleSignInResult(googleSignInResult)
        }
    }


    override fun onTapMovieItem(moviesVO: MoviesVO) {
        if (flag) {
            val intent = MovieDetailActivity.newIntent(this, moviesVO.movieId!!)
            startActivity(intent)
        } else {
            Snackbar.make(rv_movies, "You need to Sign In First!", Snackbar.LENGTH_LONG)
                .setAction("Sign In", object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        signInWithGoogle()
                    }
                }).show()
        }

    }


    private fun displayMovieList(moviesList: MutableList<MoviesVO>, isToAppend: Boolean) {
        if (isToAppend) {
            mAdapter.clearData()
            mAdapter.appendNewData(moviesList)
            rv_movies.adapter = mAdapter
        } else {
            rv_movies.adapter = mAdapter
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private fun processGoogleSignInResult(signInResult: GoogleSignInResult) {
        if (signInResult.isSuccess) {
            // Google Sign-In was successful, authenticate with Firebase
            val account = signInResult.signInAccount
            MovieListModel.getInstance().firebaseAuthWithGoogle(
                account!!,
                object : MovieListModel.SignInWithGoogleAccountDelegate {
                    override fun onSuccessSignIn(user: UserVO) {
                        MovieListModel.getInstance().saveSignInStateInPref(applicationContext, true)
                        flag = true
                        MovieListModel.getInstance().saveAccountIdInPref(applicationContext, user)
                    }

                    override fun onFailureSignIn(msg: String) {

                    }

                    override fun showProgressDialog(msg: String) {
                        showProgress(msg)
                    }

                    override fun hideProgressDialog() {
                        hideProgress()
                    }

                })
        } else {
            // Google Sign-In failed
            Snackbar.make(rv_movies, "Your Google sign-in failed.", Snackbar.LENGTH_LONG).show()
        }
    }


    override fun onConnectionFailed(p0: ConnectionResult) {

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
        /*return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)*/

        val id = item.itemId

        if (id == R.id.action_my_account)

            if (flag) {
                startUserAccountActivity()
            } else {
                Snackbar.make(rv_movies, "You need to Sign In First!", Snackbar.LENGTH_LONG)
                    .setAction("Sign In", object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            signInWithGoogle()
                        }
                    }).show()
            }

        return true
    }

    fun showProgress(msg: String) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setMessage(msg)
            mProgressDialog!!.setIndeterminate(true)
        }

        mProgressDialog!!.show()
    }

    fun hideProgress() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.dismiss()
        }
    }

    private fun startUserAccountActivity() {
        val intent = UserActivity.newIntent(this)

        startActivity(intent)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnMovieListLoaded(event: DataEvents.MovieListLoadedEvent) {
        mMovieList = event.movieList
        mAdapter.clearData()
        mAdapter.setNewData(mMovieList!!)
    }


}
