package com.hitglynorthz.elepes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.adapter.ArtistAdapter
import com.hitglynorthz.elepes.databinding.FragmentArtistsBinding
import com.hitglynorthz.elepes.models.Artist
import com.hitglynorthz.elepes.ui.details.ArtistListFragmentDirections
import com.hitglynorthz.elepes.viewmodels.LibraryViewModel

class ArtistsFragment : Fragment(),
    ArtistAdapter.OnItemClickListener{

    private var _binding: FragmentArtistsBinding? = null
    private val binding get() = _binding!!
    private val mLibraryViewModel: LibraryViewModel by viewModels()

    private lateinit var mFab: FloatingActionButton
    private lateinit var mBottomNav: BottomNavigationView

    private val artistAdapter: ArtistAdapter by lazy { ArtistAdapter(this) }
    private lateinit var artistList: List<Artist>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentArtistsBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
    }

    //
    private fun initViews() {
        mFab = this.requireActivity().findViewById(R.id.mFab)
        mBottomNav = this.requireActivity().findViewById(R.id.mBottomNav)
    }

    //
    private fun setupRecycler() {
        binding.artistRecycler.isMotionEventSplittingEnabled = false
        binding.artistRecycler.adapter = artistAdapter
        binding.artistRecycler.layoutManager = LinearLayoutManager(requireContext())
        mLibraryViewModel.showArtists().observe(viewLifecycleOwner, Observer { artist ->
            artistAdapter.setData(artist)
            artistList = artist
        })
    }

    //
    override fun onResume() {
        super.onResume()
        mBottomNav.menu.getItem(0).icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_sort)
        mBottomNav.menu.getItem(1).isEnabled = false
        mBottomNav.menu.getItem(1).icon = null
        setupFabBottomNav()
    }

    //
    private fun setupFabBottomNav() {
        mBottomNav.menu.getItem(0).setOnMenuItemClickListener { menuItem: MenuItem? ->
            Snackbar.make(mFab, "Artist", Snackbar.LENGTH_SHORT).setAnchorView(mFab).show()
            true
        }
    }

    //
    override fun onItemClick(position: Int) {
        val directions = ArtistListFragmentDirections.actionGlobalArtistListFragment(artistList[position].artist.toString())
        findNavController().navigate(directions)
    }

    //
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}