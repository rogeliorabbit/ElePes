package com.hitglynorthz.elepes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.databinding.FragmentFavsBinding

class FavsFragment : Fragment() {

    private var _binding: FragmentFavsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFab: FloatingActionButton
    private lateinit var mBottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavsBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    //
    private fun initViews() {
        mFab = this.requireActivity().findViewById(R.id.mFab)
        mBottomNav = this.requireActivity().findViewById(R.id.mBottomNav)
    }

    //
    override fun onResume() {
        super.onResume()
        mBottomNav.menu.getItem(0).icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_list_type)
        mBottomNav.menu.getItem(1).isEnabled = false
        mBottomNav.menu.getItem(1).icon = null
        setupFabBottomNav()
    }

    //
    private fun setupFabBottomNav() {
        mBottomNav.menu.getItem(0).setOnMenuItemClickListener { menuItem: MenuItem? ->
            findNavController().navigate(R.id.action_global_mainBottomSheet)
            true
        }
    }

    //
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}