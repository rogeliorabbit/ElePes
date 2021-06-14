package com.hitglynorthz.elepes.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.adapter.LibraryAdapter
import com.hitglynorthz.elepes.databinding.FragmentAllBinding
import com.hitglynorthz.elepes.databinding.FragmentHomeBinding
import com.hitglynorthz.elepes.models.Library
import com.hitglynorthz.elepes.ui.details.DetailsFragmentDirections
import com.hitglynorthz.elepes.viewmodels.LibraryViewModel
import com.hitglynorthz.elepes.viewmodels.SharedViewModel

class AllFragment : Fragment(),
    LibraryAdapter.OnItemClickListener,
    LibraryAdapter.OnItemLongClickListener,
    ActionMode.Callback  {

    private var _binding: FragmentAllBinding? = null
    private val binding get() = _binding!!
    private val mLibraryViewModel: LibraryViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private lateinit var mFab: FloatingActionButton
    private lateinit var mBottomNav: BottomNavigationView

    private val libraryAdapter: LibraryAdapter by lazy { LibraryAdapter(mLibraryViewModel, this, this) }
    private lateinit var libraryList: List<Library>

    private var multiSelection = false
    private lateinit var mActionMode: ActionMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAllBinding.inflate(inflater, container, false)

        initViews()

        /*mSharedViewModel.getAllFilter().observe(viewLifecycleOwner, Observer { query ->
            Log.e("Query", query)
        })*/

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding.mainRecycler.post { startPostponedEnterTransition() }

        setupRecycler()
    }

    //
    private fun initViews() {
        mFab = this.requireActivity().findViewById(R.id.mFab)
        mBottomNav = this.requireActivity().findViewById(R.id.mBottomNav)
    }

    //
    private fun setupRecycler() {
        binding.mainRecycler.isMotionEventSplittingEnabled = false
        binding.mainRecycler.adapter = libraryAdapter
        binding.mainRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        mLibraryViewModel.getAllData.observe(viewLifecycleOwner, Observer { library ->
            libraryAdapter.setData(library)
            libraryList = library
            when(library.isEmpty()) {
                true -> {
                    binding.evEmpty.visibility = View.VISIBLE
                }
                false -> {
                    binding.evEmpty.visibility = View.GONE
                }
            }
        })
        //
        binding.mainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mFab.isShown) {
                        mFab.hide()
                        mBottomNav.menu.getItem(1).isEnabled = true
                        mBottomNav.menu.getItem(1).icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_add_box, null)
                    }
                } else {
                    if (!mFab.isShown) {
                        mFab.show()
                        mBottomNav.menu.getItem(1).isEnabled = false
                        mBottomNav.menu.getItem(1).icon = null
                    }
                }
            }
        })
    }

    //
    override fun onItemClick(holder: LibraryAdapter.MyViewHolder, position: Int, cvCover: CardView, ivCover: ImageView, tvTitle: TextView, tvArtist: TextView) {
        if(multiSelection) {
            applySelection(holder, libraryList[position])
        } else {
            val extras = FragmentNavigatorExtras(
                cvCover to "cvCoverTransition",
                ivCover to "ivCoverTransition",
            )
            val directions =
                DetailsFragmentDirections.actionGlobalDetailsFragment(libraryList[position])
            findNavController().navigate(directions, extras)
        }
    }
    override fun onItemLongClick(holder: LibraryAdapter.MyViewHolder, position: Int, libraryRow: MaterialCardView) {
        if(!multiSelection) {
            multiSelection = true
            requireActivity().startActionMode(this)
            applySelection(holder, libraryList[position])
        } else {
            applySelection(holder, libraryList[position])
        }
    }

    //
    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.contextual_menu, menu)
        mActionMode = actionMode!!
        return true
    }
    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }
    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        val selectedList = libraryAdapter.getSelectedItemList()
        if(menu?.itemId == R.id.action_delete) {
            selectedList.forEach {
                mLibraryViewModel.deleteItem(it)
            }
            Snackbar.make(mFab, "${selectedList.size} Item/s removed.", Snackbar.LENGTH_SHORT).setAnchorView(mFab).show()
            multiSelection = false
            libraryAdapter.clearSelection()
            mActionMode.finish()
        }
        return true
    }
    override fun onDestroyActionMode(actionMode: ActionMode?) {
        libraryAdapter.getViewHolders().forEach { holder ->
            holder.libraryRow.isChecked = false
        }
        multiSelection = false
        libraryAdapter.clearSelection()
    }
    private fun applySelection(holder: LibraryAdapter.MyViewHolder, currentSelection: Library) {
        val selectedList = libraryAdapter.getSelectedItemList()
        if(selectedList.contains(currentSelection)) {
            libraryAdapter.removeSelection(currentSelection)
            holder.libraryRow.isChecked = false
            applyActionModeTitle(selectedList)
        } else {
            libraryAdapter.addSelection(currentSelection)
            holder.libraryRow.isChecked = true
            applyActionModeTitle(selectedList)
        }
    }
    private fun applyActionModeTitle(selectedList: ArrayList<Library>) {
        when(selectedList.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            else -> {
                mActionMode.title = selectedList.size.toString()
            }
        }
    }
    private fun clearContextualActionMode() {
        if(this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
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
        clearContextualActionMode()
    }

}