package com.hitglynorthz.elepes.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.hitglynorthz.elepes.data.api.APIService
import com.hitglynorthz.elepes.MainActivity
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.adapter.ResultAdapter
import com.hitglynorthz.elepes.databinding.FragmentSearchAlbumBinding
import com.hitglynorthz.elepes.models.search.Cell
import com.hitglynorthz.elepes.utils.Constants.Companion.API_KEY
import com.hitglynorthz.elepes.utils.hideKeyboard
import com.hitglynorthz.elepes.viewmodels.LibraryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchAlbumFragment : Fragment(), ResultAdapter.OnItemClickListener {

    private var _binding: FragmentSearchAlbumBinding? = null
    private val binding get() = _binding!!
    private val mLibraryViewModel: LibraryViewModel by viewModels()

    var itemsResult: ArrayList<Cell> = ArrayList()
    private lateinit var resultAdapter: ResultAdapter

    private lateinit var mFab: FloatingActionButton

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchAlbumBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        mFab.setOnClickListener {
            searchView.isIconified = false
            searchView.requestFocus()
        }
    }

    //
    private fun initViews() {
        resultAdapter = ResultAdapter(itemsResult, this)
        mFab = this.requireActivity().findViewById(R.id.mFab)
    }

    //
    private fun setupRecycler() {
        binding.rvSearchAlbum.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_top, menu);
        super.onCreateOptionsMenu(menu, inflater)
        val search = menu.findItem(R.id.action_search)
        searchView = search.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.searchAlbumFragment)
        searchView.isIconified = false
        searchView.onActionViewExpanded()
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.findViewById<View>(androidx.appcompat.R.id.search_plate).background = null
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                itemsResult.clear()
                searchView.clearFocus()
                parseJson(query!!)
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })
    }

    //
    private fun parseJson(query: String) {
        binding.ivListInfo.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        val params: HashMap<String, String> = HashMap()
        params["album"] = query
        params["api_key"] = API_KEY
        params["format"] = "json"

        val retrofit = Retrofit.Builder()
            .baseUrl("http://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getAlbumFromAlbum(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val items = response.body()?.results
                    if (items != null) {
                        for (i in 0 until items.albummatches.album.count()) {
                            // album
                            val album = items.albummatches.album[i].name
                            Log.e("album", album)
                            // artist
                            val artist = items.albummatches.album[i].artist
                            Log.e("artist", artist)
                            // img
                            val img = items.albummatches.album[i].image[2].text

                            val model =
                                Cell(
                                    album,
                                    artist,
                                    img
                                )
                            itemsResult.add(model)
                            resultAdapter.notifyDataSetChanged()
                        }
                    }
                    if(items!!.albummatches.album.count() == 0){
                        Snackbar.make(mFab, "Nothing Found for $query", Snackbar.LENGTH_LONG).setAnchorView(mFab).show()
                        binding.ivListInfo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_undraw_void))
                        binding.ivListInfo.visibility = View.VISIBLE
                    }
                    binding.rvSearchAlbum.adapter = resultAdapter
                    binding.progressBar.visibility = View.GONE
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    //
    override fun onItemClick(position: Int) {
        //showDialogAdd(itemsResult[position])
        val directions = SearchAlbumFragmentDirections.actionSearchAlbumFragmentToAddBottomSheet(itemsResult[position])
        findNavController().navigate(directions)
    }

    //
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        hideKeyboard(activity as MainActivity)
    }
}