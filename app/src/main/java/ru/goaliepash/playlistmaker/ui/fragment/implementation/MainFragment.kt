package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val buttonSearchClickListener = View.OnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, SearchFragment.newInstance())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit()
        }
        binding.buttonSearch.setOnClickListener(buttonSearchClickListener)
    }

    private fun initButtonMediaLibrary() {
        binding.buttonMediaLibrary.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, MediaLibraryFragment.newInstance())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit()
        }
    }

    private fun initButtonSettings() {
        binding.buttonSettings.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, SettingsFragment.newInstance())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit()
        }
    }
}