package com.hitglynorthz.elepes.ui.sheets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.data.api.APIService
import com.hitglynorthz.elepes.databinding.BottomsheetAddBinding
import com.hitglynorthz.elepes.models.Library
import com.hitglynorthz.elepes.viewmodels.LibraryViewModel
import com.hitglynorthz.elepes.viewmodels.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AddBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomsheetAddBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<AddBottomSheetArgs>()
    private val mLibraryViewModel: LibraryViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private lateinit var mFab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = BottomsheetAddBinding.inflate(inflater, container, false)

        initViews()
        initForm()

        binding.tvAlbum.text = args.currentItem.album
        binding.tvArtist.text = args.currentItem.artist

        Glide.with(requireContext())
            .load(args.currentItem.img)
            .error(R.drawable.ic_baseline_album)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivCover)

        binding.btnAdd.setOnClickListener {
            searchInfo()
        }

        return binding.root
    }

    //
    private fun initViews() {
        mFab = this.requireActivity().findViewById(R.id.mFab)
    }

    //
    private fun initForm() {
        val types = resources.getStringArray(R.array.types)
        val typesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, types)
        (binding.tilType.editText as? AutoCompleteTextView)?.setAdapter(typesAdapter)
        (binding.tilType.editText as? AutoCompleteTextView)?.setText(typesAdapter.getItem(0),false)
    }

    //
    private fun searchInfo() {
        isCancelable = false
        binding.progressRead.visibility = View.VISIBLE

        val params: HashMap<String, String> = HashMap()
        params["artist"] = args.currentItem.artist
        params["album"] = args.currentItem.album
        params["format"] = "json"

        val retrofit = Retrofit.Builder()
            .baseUrl("http://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getAlbumFromArtistAlbum(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    isCancelable = true
                    binding.progressRead.visibility = View.INVISIBLE
                    val items = response.body()?.album
                    if (items != null) {
                        for (i in 0 until 1) {
                            // album
                            val album = items.name ?: "N/A"
                            Log.e("album: ", album)
                            // artist
                            val artist = items.artist ?: "N/A"
                            Log.e("artist: ", artist)
                            // img
                            val img = items.image[2].text ?: "N/A"
                            Log.e("img: ", img)
                            // released
                            val released = items.wiki?.published ?: "N/A"
                            val parts = released.split(",")
                            Log.e("released: ", parts[0])
                            // url
                            val url = items.url ?: "N/A"
                            Log.e("url: ", url)
                            // nTracks
                            val nTracks = items.tracks?.track?.size ?: 0
                            Log.e("nTracks: ", nTracks.toString())
                            // tracks
                            //val tl: List<Track> = items.tracks?.track!! test green day
                            val tracks = ArrayList<String>()
                            for(t in 0 until items.tracks?.track?.count()!!) {
                                tracks.add(items.tracks.track[t].name)
                            }
                            // info
                            val info = items.wiki?.summary ?: "N/A"
                            Log.e("info", info)
                            //
                            addToLibrary(album, artist, img,  url, nTracks, tracks, info)
                        }
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                    isCancelable = true
                    binding.progressRead.visibility = View.INVISIBLE
                }
            }
        }
    }

    //
    private fun addToLibrary(
        album: String,
        artist: String,
        img: String,
        url: String,
        nTracks: Int,
        tracks: ArrayList<String>,
        info: String) {
        val mType = (binding.tilType.editText as? AutoCompleteTextView)?.text.toString()
        val newData = Library(
            0,
            album,
            artist,
            "",
            img,
            mSharedViewModel.parseType(requireContext(), mType),
            0,
            url,
            nTracks,
            tracks,
            info
        )
        mLibraryViewModel.insertData(newData)
        Snackbar.make(mFab, resources.getString(R.string.searchAlbumItemAdded), Snackbar.LENGTH_LONG).setAnchorView(mFab).show()
        //findNavController().navigate(R.id.homeFragment,null, NavOptions.Builder().setPopUpTo(findNavController().graph.startDestination, true).build())
        dismiss()
    }

}