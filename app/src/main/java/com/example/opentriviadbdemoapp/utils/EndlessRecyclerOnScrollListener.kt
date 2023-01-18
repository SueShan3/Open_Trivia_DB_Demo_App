package com.example.opentriviadbdemoapp.utils

import android.os.Handler
import android.os.Looper
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener(private var mLayoutManager: LinearLayoutManager?) :
    RecyclerView.OnScrollListener() {

    companion object {
        const val VISIBLE_THRESHOLD = 1 // The minimum amount of items to have below your current scroll position before loading more.
    }

    private var loading = false // True if we are still waiting for the last set of data to load.

    private var mHasMorePages = true
    private var mEnabled = false

    private val handler = Handler(Looper.getMainLooper())

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (mHasMorePages && !loading && mEnabled) {
            val totalItemCount = mLayoutManager?.itemCount ?: 0
            val lastVisibleItem = mLayoutManager?.findLastVisibleItemPosition() ?: 0

            if (lastVisibleItem >= totalItemCount - VISIBLE_THRESHOLD) {
                loading = true
                handler.removeCallbacks(runnable)
                handler.post(runnable)
            }
        }
    }

    fun hasMorePages(hasMorePages: Boolean) {
        mHasMorePages = hasMorePages
    }

    fun setEnabled(enabled: Boolean) {
        mEnabled = enabled
    }

    /**
     * Gives the chance to trigger network call and show appropriate UI
     */
    abstract fun onLoadMore()

    /**
     * Gives the chance to hide appropriate UI
     */
    @CallSuper
    open fun onLoadMoreComplete() {
        loading = false
    }

    fun isLoading(): Boolean {
        return loading
    }

    private val runnable = Runnable { onLoadMore() }
}