package com.hitglynorthz.elepes.ui.crud

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hitglynorthz.elepes.data.api.APIService
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.databinding.FragmentAddBinding
import com.hitglynorthz.elepes.viewmodels.LibraryViewModel
import com.hitglynorthz.elepes.viewmodels.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val mLibraryViewModel: LibraryViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private lateinit var mFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        initViews()

        initForm()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFab.setOnClickListener {
            checkData()
        }
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
    private fun checkData() {
        binding.tilTitle.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!mSharedViewModel.checkEmpty(s.toString())) {
                    binding.tilTitle.error = null
                }
            }
        })
        binding.tilArtist.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!mSharedViewModel.checkEmpty(s.toString())) {
                    binding.tilArtist.error = null
                }
            }
        })
        val mTitle = binding.tilTitle.editText?.text.toString()
        val mArtist = binding.tilArtist.editText?.text.toString()
        val mRecord = binding.tilRecord.editText?.text.toString()
        val mType = (binding.tilType.editText as? AutoCompleteTextView)?.text.toString()

        when {
            mSharedViewModel.checkEmpty(mTitle) -> {
                binding.tilTitle.error = resources.getString(R.string.tilTitleError)
                binding.tilTitle.requestFocus()
            }
            mSharedViewModel.checkEmpty(mArtist) -> {
                binding.tilArtist.error = resources.getString(R.string.tilArtistError)
                binding.tilArtist.requestFocus()
            }
            else -> {
                insertData(mTitle, mArtist, mRecord, mType)
            }
        }
    }

    //
    private fun insertData(mTitle: String, mArtist: String, mRecord: String, mType: String) {
        /*val newData = Library(
            0,
            mTitle,
            mArtist,
            mRecord,
            "",
            mSharedViewModel.parseType(requireContext(), mType),
            0
        )
        mLibraryViewModel.insertData(newData)
        Snackbar.make(mFab, "Item Added", Snackbar.LENGTH_LONG).setAnchorView(mFab).show()
        findNavController().navigateUp()*/
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_top, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search_json -> {
                parseJson()
            }
        }
        return true
    }

    //
    private fun parseJson() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tilTitle.isEnabled = false
        binding.tilArtist.isEnabled = false
        binding.tilRecord.isEnabled = false
        binding.tilType.isEnabled = false

        val params: HashMap<String, String> = HashMap()
        params["artist"] = binding.tilArtist.editText?.text.toString()
        params["album"] = binding.tilTitle.editText?.text.toString()
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
                    binding.progressBar.visibility = View.INVISIBLE
                    val items = response.body()?.album
                    if (items != null) {
                        for (i in 0 until 1) {
                            // album
                            val album = items.name ?: "N/A"
                            Log.e("album: ", album)
                            binding.tilTitle.editText?.setText(album)
                            // artist
                            val artist = items.artist ?: "N/A"
                            Log.e("artist: ", artist)
                            binding.tilArtist.editText?.setText(artist)
                            // img
                            val img = items.image[2].text ?: "N/A"
                            Log.e("img: ", img)
                            binding.tilRecord.editText?.setText(img)
                            Glide
                                .with(requireContext())
                                .load(img)
                                .error(R.drawable.ic_baseline_album)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(binding.ivCover)
                        }
                    }
                    binding.tilTitle.isEnabled = true
                    binding.tilArtist.isEnabled = true
                    binding.tilRecord.isEnabled = true
                    binding.tilType.isEnabled = true
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                    binding.tilTitle.isEnabled = true
                    binding.tilArtist.isEnabled = true
                    binding.tilRecord.isEnabled = true
                    binding.tilType.isEnabled = true
                }
            }
        }
    }

    //
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}