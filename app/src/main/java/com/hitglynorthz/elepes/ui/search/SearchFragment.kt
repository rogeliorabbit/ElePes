package com.hitglynorthz.elepes.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hitglynorthz.elepes.MainActivity
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.databinding.FragmentSearchBinding
import com.hitglynorthz.elepes.utils.hideKeyboard

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFab: FloatingActionButton

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFab.setOnClickListener {
            searchView.isIconified = false
            searchView.requestFocus()
        }
    }

    //
    private fun initViews() {
        mFab = this.requireActivity().findViewById(R.id.mFab)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_top, menu);
        super.onCreateOptionsMenu(menu, inflater)
        val search = menu.findItem(R.id.action_search)
        searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.isIconified = false
        searchView.onActionViewExpanded()
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.findViewById<View>(androidx.appcompat.R.id.search_plate).background = null
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // clear list
                searchView.clearFocus()
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    //
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        hideKeyboard(activity as MainActivity)
    }
}