package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.FragmentMainBinding
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment

class MainFragment : BindingFragment<FragmentMainBinding>() {

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initButtonSearch()
        initButtonMediaLibrary()
        initButtonSettings()
    }

    private fun initButtonSearch() {
        binding.buttonSearch.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }
    }

    private fun initButtonMediaLibrary() {
        binding.buttonMediaLibrary.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mediaLibraryFragment)
        }
    }

    private fun initButtonSettings() {
        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }
}