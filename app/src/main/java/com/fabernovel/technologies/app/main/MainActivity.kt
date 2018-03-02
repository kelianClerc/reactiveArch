package com.fabernovel.technologies.app.main

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import com.fabernovel.technologies.R
import com.fabernovel.technologies.app.RandomUserReactiveScreen
import com.fabernovel.technologies.app.common.BaseActivity


class MainActivity : BaseActivity<MainUiEvent, MainViewModel, RandomUserReactiveScreen.Main>() {

    override fun makeViewModel(provider: ViewModelProvider): MainViewModel =
        provider.get(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
