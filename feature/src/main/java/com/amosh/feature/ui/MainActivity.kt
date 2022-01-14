package com.amosh.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import com.amosh.base.BaseActivity
import com.amosh.feature.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val bindLayout: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) = Unit
}