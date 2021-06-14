package com.hitglynorthz.elepes.ui.crud

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.databinding.FragmentEditBinding
import com.hitglynorthz.elepes.viewmodels.LibraryViewModel
import com.hitglynorthz.elepes.viewmodels.SharedViewModel

class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditFragmentArgs>()
    private val mLibraryViewModel: LibraryViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private lateinit var mFab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        initViews()

        initForm()

        binding.tilTitle.editText?.setText(args.currentItem.title)
        binding.tilArtist.editText?.setText(args.currentItem.artist)
        binding.tilRecord.editText?.setText(args.currentItem.record)

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
        (binding.tilType.editText as? AutoCompleteTextView)?.setText(typesAdapter.getItem(mSharedViewModel.parseTypeToInt(args.currentItem.type)),false)
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
                updateData(mTitle, mArtist, mRecord, mType)
            }
        }
    }

    //
    private fun updateData(mTitle: String, mArtist: String, mRecord: String, mType: String) {
        /*val updatedItem = Library(
            args.currentItem.id,
            mTitle,
            mArtist,
            mRecord,
            "",
            mSharedViewModel.parseType(requireContext(), mType),
            0
        )
        mLibraryViewModel.updateItem(updatedItem)
        Snackbar.make(mFab, "Item Edited", Snackbar.LENGTH_LONG).setAnchorView(mFab).show()
        findNavController().navigateUp()*/
    }

    //
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}