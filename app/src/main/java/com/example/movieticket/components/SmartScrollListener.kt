package com.example.movieticket.components


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SmartScrollListener(private val mSmartScrollListener: OnSmartScrollListener) :
    RecyclerView.OnScrollListener() {

    private var isListEndReached = false

    interface OnSmartScrollListener {
        fun onListEndReach()
    }

    override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(rv, dx, dy)

        val visibleItemCount = rv.getLayoutManager()!!.getChildCount()
        val totalItemCount = rv.getLayoutManager()!!.getItemCount()
        val pastVisibleItems =
            (rv.getLayoutManager() as LinearLayoutManager).findFirstVisibleItemPosition()

        if (visibleItemCount + pastVisibleItems < totalItemCount) {
            isListEndReached = false
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, scrollState: Int) {
        super.onScrollStateChanged(recyclerView, scrollState)
        if (scrollState == RecyclerView.SCROLL_STATE_IDLE
            && (recyclerView.getLayoutManager() as LinearLayoutManager).findLastCompletelyVisibleItemPosition() === recyclerView.getAdapter()!!.getItemCount() - 1
            && !isListEndReached
        ) {
            isListEndReached = true
            mSmartScrollListener.onListEndReach()
        }
    }
}
