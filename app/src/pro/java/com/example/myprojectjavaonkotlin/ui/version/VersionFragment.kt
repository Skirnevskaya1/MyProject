package com.example.myprojectjavaonkotlin.ui.version

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.myprojectjavaonkotlin.BuildConfig
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.databinding.FragmentVersionBinding
import com.example.myprojectjavaonkotlin.ui.utils.snack

class VersionFragment : Fragment(R.layout.fragment_version) {

    private var _binding: FragmentVersionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentVersionBinding.bind(view)

        informationTheApp()

        binding.switchButton.setOnClickListener {
            it.snack(
                "Вы работаете с версией:\n" + BuildConfig.VERSION_NAME
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun informationTheApp() {

        binding.codVersionTextView.text = "Код версии: " + BuildConfig.VERSION_CODE

        binding.buildTypesTextView.text = BuildConfig.MY_BUIL_TYPE
        binding.buildTypesTextView2.text = getString(R.string.my_build_type)

        binding.aboutAppTextView.text = "О Приложении\nВидео поиск\nТестовая версия"
    }


    companion object {
        @JvmStatic
        fun newInstance() = VersionFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}