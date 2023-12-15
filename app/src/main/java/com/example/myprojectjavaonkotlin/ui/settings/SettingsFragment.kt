package com.example.myprojectjavaonkotlin.ui.settings

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myprojectjavaonkotlin.App
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.databinding.FragmentSettingsBinding
import com.example.myprojectjavaonkotlin.ui.utils.snack
import java.io.IOException

// Геолокация
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val app: App by lazy {
        requireActivity().application as App
    }

    private val adultInteractor by lazy {
        app.di.adultInteractor
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSettingsBinding.bind(view)

        initAdultContentView()

        binding.setContactsButton.setOnClickListener {
            checkPermission()
            getController().openContacts()
        }

        binding.setGeolocationButton.setOnClickListener {
            requestVerification()
        }

        binding.mapsGoogleButton.setOnClickListener {
            getController().openMapsGoogle()
        }

        binding.versionButton.setOnClickListener {
            getController().informationAboutTheApp()
        }
    }

    private fun initAdultContentView() {
        binding.switchContent.isChecked = adultInteractor.isAdult.value ?: false
        binding.switchContent.setOnCheckedChangeListener { _, isChecked ->
            adultInteractor.isAdult.postValue(isChecked)

            Toast.makeText(
                requireContext(),
                if (isChecked) "Взрослый контент" else "Выключено",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Геолокация
    private fun requestVerification() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    // Геолокация
    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Доступ к геолокации")
                .setMessage("Уведомление")
                .setPositiveButton("Предоставить доступ") { _, _ ->
                    requestPermission()
                }
                .setNegativeButton("Отказать в доступе") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    // Геолокация
    private fun requestPermission() {
        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Геолокация
    private val requestPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            } else {
                showDialog(
                    title = "Доступ к геолокации закрыт",
                    message = "Разрешите доступ к геолокации"
                )
            }
        }

    // Геолокация
    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    // Геолокация
    private fun getLocation() {
        activity?.let { context ->
            // если дали разрешение и оно равно PERMISSION_GRANTED получили менеджер геолокации
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Получить менеджер геолокаций
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                // проверяем работает ли gps или нет
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // если работает gps то получаем провайдер
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        // получаем геоположение через каждые 60 секунд или каждые 100 метров
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            title = "GPS отключён",
                            message = "Местонахождение неизвестно. Включите GPS"
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                            title = "GPS отключён",
                            message = "Показана последняя известная локация. Включите GPS, если хотите обновить местонахождение"
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    // Геолокация
    private val onLocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(it, location)
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    // Геолокация
    private fun getAddressAsync(
        context: Context,
        location: Location
    ) {

        // Geocoder - это беблиотека которая выдаст координаты объекта
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1 // ограничение получаемого результата. Geocoder можнт выдать несколько объектов по даресу
                )
                // когда обращаемся к view и делаем post - это означает, что выполнение происходило в UI потоке
//                binding.mainFragmentFAB.post {
//                    showAddressDialog(addresses[0].getAddressLine(0), location)
//                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    // проверяем разрешения чтения контактов
    private fun checkPermission() {
        context?.let {
            when {
                // Внимательно импортировать Manifest (в библиотеках android)
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_DENIED -> {
                    // Доступ к контактам
                    view?.snack("Список контактов получен")
                    getController().openContacts()
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
                view?.snack("Список контактов получен")
            } else {
                // не выдана
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Уведомление")
                        .setNegativeButton("Закрыть") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }

    interface Controller {
        fun openContacts()
        fun openMapsGoogle()
        fun informationAboutTheApp()
    }

    private fun getController(): Controller = activity as Controller

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}