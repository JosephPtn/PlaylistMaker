package com.saturnnetwork.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.saturnnetwork.playlistmaker.databinding.SettingFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getThemeSetting()

        binding.shareApp.setOnClickListener {
            viewModel.onShareButtonClicked()
        }
        binding.contactSupport.setOnClickListener {
            viewModel.onContactSupportButtonClicked()
        }
        binding.userAgreement.setOnClickListener {
            viewModel.onButtonToUserAgreement()
        }
        viewModel.observeIntentLiveData().observe(viewLifecycleOwner) { intent ->
            startActivity(intent)
        }
        viewModel.observeSelectedMode().observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it
            AppCompatDelegate.setDefaultNightMode(
                if (it) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )

        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setThemeSetting(isChecked)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}