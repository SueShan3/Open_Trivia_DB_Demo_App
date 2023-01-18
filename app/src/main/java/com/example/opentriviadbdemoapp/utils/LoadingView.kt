package com.example.opentriviadbdemoapp.utils

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.databinding.LoadingViewBinding

class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        DataBindingUtil.inflate<LoadingViewBinding>(inflater, R.layout.loading_view, this, true)

        orientation = VERTICAL

        isVisible = false
        gravity = Gravity.CENTER

        setOnClickListener {

        }
    }
}