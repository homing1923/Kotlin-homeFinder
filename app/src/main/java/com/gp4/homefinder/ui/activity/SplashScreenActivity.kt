package com.gp4.homefinder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.FirebaseAuth
import com.gp4.homefinder.R
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.ui.splashfragments.SplashFragmentDirections

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: SplashScreenActivity

    private lateinit var dataSource: DataSource

    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true

    private val SPLASH_TIME_OUT: Long = 2000 // 2 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        dataSource = DataSource.getInstance()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container1) as NavHostFragment
        this.navController = navHostFragment.navController
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)


        Handler(Looper.getMainLooper()).postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            this.navController.navigate(R.id.entryFragment)
        }, SPLASH_TIME_OUT)
    }

    override fun onBackPressed() {
        val currentFragmentId =
            supportFragmentManager.fragments.last()?.id
        Log.d("D1", "$currentFragmentId")
        super.onBackPressed()

    }

}