package com.example.opentriviadbdemoapp.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.databinding.ActivityMainBinding
import com.example.opentriviadbdemoapp.utils.SharedPrefUtils
import com.example.opentriviadbdemoapp.utils.SharedPrefUtils.Companion.token
import com.example.opentriviadbdemoapp.utils.Status
import com.example.opentriviadbdemoapp.viewmodel.OpenTriviaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val openTriviaViewModel: OpenTriviaViewModel by viewModel()

    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openTriviaViewModel.getSessionToken()
        openTriviaViewModel.getSessionTokenObserver().observe(this, Observer {
            when (it?.status) {
                Status.SUCCESS -> {
                    val sessionToken = it.data?.token
                    if(sessionToken != null){
                        SharedPrefUtils.customSharedPrefs(this).token = sessionToken
                        Log.d("SueShan", "Session Token: $sessionToken")
                    }

                }
                else -> {}
            }
        })

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(dataBinding.bottomNav, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                SettingActivity.start(this)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}