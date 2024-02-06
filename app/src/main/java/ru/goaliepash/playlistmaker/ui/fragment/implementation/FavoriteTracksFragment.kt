package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.domain.model.Track
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.FragmentFavoriteTracksBinding
import ru.goaliepash.playlistmaker.presentation.state.FavoriteTracksState
import ru.goaliepash.playlistmaker.presentation.view_model.FavoriteTracksViewModel
import ru.goaliepash.playlistmaker.ui.adapter.TrackAdapter
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment
import ru.goaliepash.playlistmaker.utils.Constants.CLICK_DEBOUNCE_DELAY
import ru.goaliepash.playlistmaker.utils.debounce

class FavoriteTracksFragment : BindingFragment<FragmentFavoriteTracksBinding>() {

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private var trackAdapter: TrackAdapter? = null

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteTracksBinding {
        return FragmentFavoriteTracksBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                openTrackActivity(it)
            }
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getFavoriteTracks()
        viewModel.getFavoriteTracksState().observe(viewLifecycleOwner) {
            renderFavoriteTracksState(it)
        }
    }

    private fun openTrackActivity(track: Track) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
            AudioPlayerFragment.createArgs(track)
        )
    }

    private fun initRecyclerView() {
        trackAdapter = TrackAdapter(onTrackClickDebounce)
        binding.recyclerView.adapter = trackAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun renderFavoriteTracksState(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Loading -> {
                hideRecyclerView()
                hidePlaceHolder()
                showLoader()
            }

            is FavoriteTracksState.Content -> {
                showRecyclerView()
                hidePlaceHolder()
                hideLoader()
                trackAdapter?.tracks?.clear()
                trackAdapter?.tracks?.addAll(state.favoriteTracks)
                trackAdapter?.notifyDataSetChanged()
            }

            is FavoriteTracksState.Empty -> {
                hideRecyclerView()
                showPlaceholder()
                hideLoader()
            }
        }
    }

    private fun showRecyclerView() {
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun hideRecyclerView() {
        binding.recyclerView.visibility = View.GONE
    }

    private fun showLoader() {
        binding.linearLayoutProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.linearLayoutProgressBar.visibility = View.GONE
    }

    private fun showPlaceholder() {
        binding.linearLayoutPlaceholder.visibility = View.VISIBLE
    }

    private fun hidePlaceHolder() {
        binding.linearLayoutPlaceholder.visibility = View.GONE
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}