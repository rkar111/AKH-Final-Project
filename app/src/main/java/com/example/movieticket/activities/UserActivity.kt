package com.example.movieticket.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.movieticket.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, UserActivity::class.java)
            return intent
        }

    }

    private var mFirebaseUser: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }


        mFirebaseUser = FirebaseAuth.getInstance().currentUser!!

        displayData()

        btn_my_ticket.setOnClickListener {
            val intent = MyBookingActivity.newIntent(this)

            startActivity(intent)
        }
    }

    fun displayData() {
        Glide.with(this)
            .load(mFirebaseUser!!.photoUrl)
            .into(profileImage)
        tvIPSProfileName.text = mFirebaseUser!!.displayName
        tvIPSEmail.text = mFirebaseUser!!.email
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