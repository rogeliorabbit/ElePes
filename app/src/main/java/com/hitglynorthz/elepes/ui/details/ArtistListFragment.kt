package com.hitglynorthz.elepes.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.adapter.ArtistAlbumAdapter
import com.hitglynorthz.elepes.databinding.FragmentArtistListBinding
import com.hitglynorthz.elepes.models.Library
import com.hitglynorthz.elepes.viewmodels.LibraryViewModel
import com.hitglynorthz.elepes.viewmodels.SharedViewModel

class ArtistListFragment : Fragment(),
    ArtistAlbumAdapter.OnItemClickListener {

    private var _binding: FragmentArtistListBinding? = null
    private val binding get() = _binding!!
    private val mLibraryViewModel: LibraryViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private val args by navArgs<ArtistListFragmentArgs>()

    private val artistAlbumAdapter: ArtistAlbumAdapter by lazy { ArtistAlbumAdapter(this) }
    private lateinit var libraryList: List<Library>

    private lateinit var mToolbar: MaterialToolbar
    private lateinit var mFab: FloatingActionButton
    private lateinit var mBottomNav: BottomNavigationView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentArtistListBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding.artistAlbumsRecycler.post { startPostponedEnterTransition() }

        setupRecycler()
    }

    //
    private fun initViews() {
        mToolbar = this.requireActivity().findViewById(R.id.mToolbar)
        mToolbar.title = args.artist
        mFab = this.requireActivity().findViewById(R.id.mFab)
        mBottomNav = this.requireActivity().findViewById(R.id.mBottomNav)
        mFab.setOnClickListener {
            Snackbar.make(mFab, "Order by", Snackbar.LENGTH_SHORT).setAnchorView(mFab).show()
        }
    }

    //
    private fun setupRecycler() {
        binding.artistAlbumsRecycler.isMotionEventSplittingEnabled = false
        binding.artistAlbumsRecycler.adapter = artistAlbumAdapter
        binding.artistAlbumsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        mLibraryViewModel.showArtistAlbums(args.artist).observe(viewLifecycleOwner, Observer { library ->
            artistAlbumAdapter.setData(library)
            libraryList = library
        })
    }

    //
    override fun onItemClick(position: Int, cvCover: CardView, ivCover: ImageView) {
        val extras = FragmentNavigatorExtras(
            cvCover to "cvCoverTransition",
            ivCover to "ivCoverTransition"
        )
        val directions =
            DetailsFragmentDirections.actionGlobalDetailsFragment(libraryList[position])
        findNavController().navigate(directions, extras)
    }

    //
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}