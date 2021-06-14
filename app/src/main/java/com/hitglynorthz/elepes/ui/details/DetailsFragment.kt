package com.hitglynorthz.elepes.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.databinding.FragmentDetailsBinding
import com.hitglynorthz.elepes.models.Type
import com.hitglynorthz.elepes.viewmodels.LibraryViewModel


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsFragmentArgs>()

    private lateinit var mFab: FloatingActionButton
    private lateinit var mBottomAppBar: BottomAppBar

    private val mLibraryViewModel: LibraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        initViews()

        Glide.with(requireContext())
            .load(args.currentItem.img)
            .error(R.drawable.ic_baseline_album)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.ivCover)

        binding.tvTitle.text = args.currentItem.title
        binding.tvArtist.text = args.currentItem.artist
        if(!TextUtils.isEmpty(args.currentItem.record)) {
            binding.tvRecord.text = args.currentItem.record
        }
        when(args.currentItem.type) {
            Type.CD -> {
                binding.tvType.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_music_box), null, null, null)
                binding.tvType.text = args.currentItem.type.name
            }
            Type.VINYL -> {
                binding.tvType.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_music_circle), null, null, null)
                binding.tvType.text = args.currentItem.type.name
            }
            Type.CLOUD -> {
                binding.tvType.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_cloud_circle), null, null, null)
                binding.tvType.text = args.currentItem.type.name
            }
        }
        binding.tvNtracks.text = String.format(resources.getString(R.string.detailsnTracks), args.currentItem.nTracks.toString())
        if(args.currentItem.tracks.isNotEmpty()) {
            binding.llTracks.visibility = View.VISIBLE
            for (i in args.currentItem.tracks) {
                val lChip = Chip(requireContext())
                lChip.text = i
                lChip.isCheckable = false
                //lChip.isEnabled = false
                lChip.ellipsize = TextUtils.TruncateAt.END
                //lChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                //lChip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.secondaryLight))
                binding.cgTracks.addView(lChip)
            }
        }

        if(args.currentItem.url.isNotEmpty()) {
            binding.tvInfo.text = args.currentItem.info
            binding.llLastfm.visibility = View.VISIBLE
            binding.llLastfm.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(args.currentItem.url)
                startActivity(intent)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit -> {
                    val directions = DetailsFragmentDirections.actionDetailsFragmentToEditFragment(args.currentItem)
                    findNavController().navigate(directions)
                    true
                }
                R.id.action_delete -> {
                    showDeleteDialog()
                    true
                }
                else -> false
            }
        }

        mFab.setOnClickListener {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).setAnchorView(mFab).show()
        }
    }

    //
    private fun initViews() {
        mFab = this.requireActivity().findViewById(R.id.mFab)
        mBottomAppBar = this.requireActivity().findViewById(R.id.mBottomAppBar)
    }

    //
    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialogDeleteTitle))
            .setMessage(resources.getString(R.string.dialogDeleteText))
            .setNegativeButton(resources.getString(R.string.dialogDeleteNo)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.dialogDeleteOk)) { dialog, which ->
                mLibraryViewModel.deleteItem(args.currentItem)
                Snackbar.make(mFab, "Item Deleted", Snackbar.LENGTH_LONG).setAnchorView(mFab).show()
                findNavController().navigateUp()
            }
            .show()
    }

    //
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}