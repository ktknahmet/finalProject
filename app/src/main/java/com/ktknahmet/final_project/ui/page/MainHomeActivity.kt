package com.ktknahmet.final_project.ui.page


import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.databinding.ActivityMainHomeBinding
import com.ktknahmet.final_project.ui.base.BaseActivity
import com.ktknahmet.final_project.utils.Constant.LIGHT
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainHomeActivity : BaseActivity() {
    private lateinit var binding: ActivityMainHomeBinding
    private lateinit var pref: MainSharedPreferences
    private var myTheme: Int = LIGHT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = MainSharedPreferences(this, MyPref.bilgiler)
        myTheme = pref.getInt(MyPref.theme, LIGHT)
        setUI(myTheme, this)
     
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?
        val navController = navHostFragment!!.navController

        binding.bottomNavbarMain.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.profileFragment -> binding.bottomNavbarMain.visible()
                R.id.spendingFragment -> hideBottomNav()
                R.id.paymentFragment -> hideBottomNav()
                R.id.bildirimFragment -> hideBottomNav()
                R.id.friendsFragment -> hideBottomNav()
                R.id.addfriendsFragment -> hideBottomNav()
                R.id.graphFragment -> hideBottomNav()
                R.id.borcDurum -> hideBottomNav()
                R.id.pdfViewer -> hideBottomNav()

            }
        }
    }

    private fun hideBottomNav() {
        binding.bottomNavbarMain.gone()
    }


}