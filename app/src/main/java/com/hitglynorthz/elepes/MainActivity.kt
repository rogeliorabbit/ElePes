package com.hitglynorthz.elepes

import android.opengl.Visibility
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.hitglynorthz.elepes.databinding.ActivityMainBinding
import com.hitglynorthz.elepes.viewmodels.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mToolbar)

        initNavigation()
        setupNavigation()
        initBottomNav()
    }

    //
    private fun initNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    //
    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.homeFragment -> {
                    if(binding.mBottomAppBar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
                        binding.mBottomAppBar.replaceMenu(R.menu.menu_bottom_main)
                    } else {
                        binding.mBottomAppBar.setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER, R.menu.menu_bottom_main)
                    }
                    binding.mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_add))
                    binding.mBottomNav.animate().translationY(0F).alpha(1.0f).setListener(null)
                    mSharedViewModel.hideShowFab(binding.mFab, true)
                    binding.mAppBar.setLiftable(true)
                }
                R.id.detailsFragment -> {
                    if(binding.mBottomAppBar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_END) {
                        binding.mBottomAppBar.replaceMenu(R.menu.menu_bottom_details)
                    } else {
                        binding.mBottomAppBar.setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_END, R.menu.menu_bottom_details)
                    }
                    binding.mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_border))
                    binding.mBottomNav.animate().translationY(binding.mBottomNav.height.toFloat()).alpha(0.0f).setListener(null)
                    mSharedViewModel.hideShowFab(binding.mFab, true)
                    binding.mToolbar.title = ""
                    binding.mAppBar.setLiftable(true)
                }
                R.id.addFragment -> {
                    if(binding.mBottomAppBar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
                        binding.mBottomAppBar.replaceMenu(R.menu.menu_bottom_add)
                    } else {
                        binding.mBottomAppBar.setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER, R.menu.menu_bottom_add)
                    }
                    binding.mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_save))
                    binding.mBottomNav.animate().translationY(binding.mBottomNav.height.toFloat()).alpha(0.0f).setListener(null)
                    mSharedViewModel.hideShowFab(binding.mFab, true)
                    binding.mAppBar.setLiftable(false)
                }
                R.id.searchFragment -> {
                    if(binding.mBottomAppBar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
                        binding.mBottomAppBar.replaceMenu(R.menu.menu_bottom_search)
                    } else {
                        binding.mBottomAppBar.setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER, R.menu.menu_bottom_search)
                    }
                    binding.mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_keyboard))
                    binding.mBottomNav.animate().translationY(binding.mBottomNav.height.toFloat()).alpha(0.0f).setListener(null)
                    mSharedViewModel.hideShowFab(binding.mFab, true)
                    binding.mToolbar.title = ""
                    binding.mAppBar.setLiftable(false)
                }
                R.id.searchAlbumFragment -> {
                    if(binding.mBottomAppBar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
                        binding.mBottomAppBar.replaceMenu(R.menu.menu_bottom_search_album)
                    } else {
                        binding.mBottomAppBar.setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER, R.menu.menu_bottom_search_album)
                    }
                    binding.mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_keyboard))
                    binding.mBottomNav.animate().translationY(binding.mBottomNav.height.toFloat()).alpha(0.0f).setListener(null)
                    mSharedViewModel.hideShowFab(binding.mFab, true)
                    binding.mToolbar.title = ""
                    binding.mAppBar.setLiftable(false)
                }
                R.id.editFragment -> {
                    if(binding.mBottomAppBar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
                        binding.mBottomAppBar.replaceMenu(R.menu.menu_bottom_edit)
                    } else {
                        binding.mBottomAppBar.setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER, R.menu.menu_bottom_edit)
                    }
                    binding.mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_save))
                    binding.mBottomNav.animate().translationY(binding.mBottomNav.height.toFloat()).alpha(0.0f).setListener(null)
                    mSharedViewModel.hideShowFab(binding.mFab, true)
                    binding.mAppBar.setLiftable(false)
                }
                R.id.mainBottomSheet -> {
                    binding.mBottomAppBar.setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER, R.menu.menu_bottom_main)
                    binding.mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_add))
                    binding.mBottomNav.animate().translationY(binding.mBottomNav.height.toFloat()).alpha(0.0f).setListener(null)
                    mSharedViewModel.hideShowFab(binding.mFab, false)
                }
                R.id.addBottomSheet -> {
                    binding.mBottomAppBar.setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER, R.menu.menu_bottom_main)
                    binding.mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_keyboard))
                    binding.mBottomNav.animate().translationY(binding.mBottomNav.height.toFloat()).alpha(0.0f).setListener(null)
                    mSharedViewModel.hideShowFab(binding.mFab, true)
                }
                R.id.artistListFragment -> {
                    binding.mBottomAppBar.setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER, R.menu.menu_bottom_artist_album)
                    binding.mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_filter_list))
                    binding.mBottomNav.animate().translationY(binding.mBottomNav.height.toFloat()).alpha(0.0f).setListener(null)
                    mSharedViewModel.hideShowFab(binding.mFab, true)
                    binding.mAppBar.setLiftable(true)
                }
                else -> {
                    binding.mBottomAppBar.setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_END, R.menu.menu_bottom_main)
                    binding.mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_add))
                    binding.mBottomNav.animate().translationY(binding.mBottomNav.height.toFloat()).alpha(0.0f).setListener(null)
                    mSharedViewModel.hideShowFab(binding.mFab, true)
                    binding.mAppBar.setLiftable(false)
                }
            }
        }
    }

    //
    private fun initBottomNav() {
        binding.mBottomNav.background = null
        binding.mBottomNav.menu.getItem(0).isCheckable = false
        binding.mBottomNav.menu.getItem(1).isEnabled = false
        binding.mBottomNav.menu.getItem(2).isCheckable = false
        binding.mBottomNav.menu.getItem(2).setOnMenuItemClickListener { menuItem: MenuItem? ->
            navController.navigate(R.id.action_global_searchFragment)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        setupNavigation()
        initBottomNav()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}