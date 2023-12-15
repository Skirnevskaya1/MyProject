package com.example.myprojectjavaonkotlin.ui.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.databinding.FragmentContactsBinding
import com.example.myprojectjavaonkotlin.ui.utils.hide
import com.example.myprojectjavaonkotlin.ui.utils.show

class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val adapter: ContactsAdapter by lazy { ContactsAdapter() }

    private val viewModel: ContactsViewModel by lazy {
        ViewModelProvider(this)[ContactsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentContactsBinding.bind(view)
        binding.contactsListRecyclerView.adapter = adapter
        binding.contactsListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.contactsListRecyclerView.adapter = adapter

        checkPermission()
        viewModel.contacts.observe(viewLifecycleOwner) {
            renderData(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Success -> {
                binding.contactsListRecyclerView.show()
                binding.progressTaskBar.hide()
                adapter.contacts = data.data
                adapter.notifyDataSetChanged()
            }
            is AppState.Loading -> {
                binding.contactsListRecyclerView.show()
                binding.progressTaskBar.hide()
            }
        }
    }

    // проверяем разрешения чтения контактов
    private fun checkPermission() {
        context?.let {
            when {
                // Внимательно импортировать Manifest (в библиотеках android)
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    // Доступ к контактам
                    getContacts()
                }
                // пояснение перед запросом разрешения (не обязательно. показывается в окне запроса)
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Для чтения контактов и демонстрации")
                        .setPositiveButton("Предоставить доступ") { _, _ ->
                            // Запрашиваем разрешение
                            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                        .setNegativeButton("Отказать в доступе") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                else -> {
                    // если ничего не произошло, то запрашиваем разрешения
                    requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                }
            }
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // пермиссия выдана
                getContacts()
            } else {
                // не выдана
                context?.let {
                    AlertDialog.Builder(it)
                        .setMessage("Уведомление")
                        .setTitle("Доступ к контактам не разрешен!")
                        .setNegativeButton("Закрыть") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }

    private fun getContacts() {
        viewModel.getContacts()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContactsFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}