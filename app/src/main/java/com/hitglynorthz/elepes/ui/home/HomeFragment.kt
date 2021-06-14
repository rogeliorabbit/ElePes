package com.hitglynorthz.elepes.ui.home

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.adapter.LibraryAdapter
import com.hitglynorthz.elepes.adapter.TabsHomeAdapter
import com.hitglynorthz.elepes.databinding.FragmentHomeBinding
import com.hitglynorthz.elepes.models.Library
import com.hitglynorthz.elepes.viewmodels.LibraryViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mLibraryViewModel: LibraryViewModel by viewModels()

    private lateinit var tabsHomeAdapter: TabsHomeAdapter
    private var tabsTitle = arrayListOf<String>()

    private lateinit var mFab: FloatingActionButton
    private lateinit var mBottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initViews()
        initTabs()
        setupTabs()
        setupFabBottomNav()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding.viewPagerHome.post { startPostponedEnterTransition() }
    }

    //
    private fun initViews() {
        mFab = this.requireActivity().findViewById(R.id.mFab)
        mBottomNav = this.requireActivity().findViewById(R.id.mBottomNav)
        binding.tabLayoutHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.position
                if(!mFab.isShown) {
                    mFab.show()
                    mBottomNav.menu.getItem(1).isEnabled = false
                    mBottomNav.menu.getItem(1).icon = null
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    //
    private fun initTabs() {
        tabsTitle = arrayListOf(
            "Albums",
            "Artists",
            "Favs"
        )
    }

    //
    private fun setupTabs() {
        tabsHomeAdapter = TabsHomeAdapter(this)
        binding.viewPagerHome.adapter = tabsHomeAdapter
        binding.viewPagerHome.offscreenPageLimit = 3
        TabLayoutMediator(binding.tabLayoutHome, binding.viewPagerHome) {tab, position ->
            tab.text = tabsTitle[position] //"${position + 1}"
        }.attach()

        binding.viewPagerHome.isUserInputEnabled = false

        mLibraryViewModel.getAllData.observe(viewLifecycleOwner, Observer { library ->
            when(library.isEmpty()) {
                true -> {
                    binding.tabLayoutHome.visibility = View.GONE
                    mBottomNav.visibility = View.GONE
                }
                false -> {
                    binding.tabLayoutHome.visibility = View.VISIBLE
                    mBottomNav.visibility = View.VISIBLE
                }
            }
        })
    }

    //
    private fun setupFabBottomNav() {
        mBottomNav.menu.getItem(1).setOnMenuItemClickListener { menuItem: MenuItem? ->
            showDialogAdd()
            true
        }
        mFab.setOnClickListener {
            showDialogAdd()
        }
    }

    //
    private fun showDialogAdd() {
        val items = arrayOf("Add Manually", "Search Album")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add New")
            .setItems(items) { dialog, which ->
                if(which == 0) {
                    findNavController().navigate(R.id.action_global_addFragment)
                }
                if(which == 1){
                    findNavController().navigate(R.id.action_global_searchAlbumFragment)
                }
            }
            .show()
    }

    //
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home_top, menu)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
            }
        }
        return true
    }

    //
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}