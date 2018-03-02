package com.fabernovel.technologies.utils

import android.content.Context
import android.util.AttributeSet
import com.gturedi.views.StatefulLayout

class StatefulLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : StatefulLayout(context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        setLoadingByDefault()
    }

    private fun setLoadingByDefault() {
        val animationEnabled = isAnimationEnabled
        isAnimationEnabled = false
        showLoading()
        isAnimationEnabled = animationEnabled
    }
}
