package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.view.LayoutInflater
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.playlistmaker.databinding.FragmentFavoriteTracksBinding
import ru.goaliepash.playlistmaker.presentation.view_model.FavoriteTracksViewModel
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment

class FavoriteTracksFragment : BindingFragment<FragmentFavoriteTracksBinding>() {

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavoriteTracksBinding {
        return FragmentFavoriteTracksBinding.inflate(inflater, container, false)
    }

    companion object {

        fun newInstance() = FavoriteTracksFragment()
    }
}