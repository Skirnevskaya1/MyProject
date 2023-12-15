package com.example.myprojectjavaonkotlin.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.databinding.ActivityRootBinding
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.ui.contacts.ContactsFragment
import com.example.myprojectjavaonkotlin.ui.details.DetailsVideoFragment
import com.example.myprojectjavaonkotlin.ui.favourites.FavouritesFragment
import com.example.myprojectjavaonkotlin.ui.history.HistoryFragment
import com.example.myprojectjavaonkotlin.ui.map.MapsFragment
import com.example.myprojectjavaonkotlin.ui.settings.SettingsFragment
import com.example.myprojectjavaonkotlin.ui.version.VersionFragment
import com.example.myprojectjavaonkotlin.ui.video.VideoListFragment
import com.google.firebase.messaging.FirebaseMessaging

private const val TAG_DETAILS_VIDEO_KEY = "TAG_DETAILS_VIDEO_KEY"
private const val TAG_MAIN_CONTAINER_LAYOUT_KEY = "TAG_MAIN_CONTAINER_LAYOUT_KEY"
private const val TAG_CONTACTS_KEY = "TAG_CONTACTS_KEY"
private const val TAG_MAPS_KEY = "TAG_MAPS_KEY"
private const val TAG_VERSION_KEY = "TAG_VERSION_KEY"

const val CHANNEL_ID = "CHANNEL_ID"
const val TAG_FIREBASE = "FIREBASE_MESSAGING"

class RootActivity : AppCompatActivity(),
    VideoListFragment.Controller,
    FavouritesFragment.Controller,
    SettingsFragment.Controller,
    HistoryFragment.Controller {

    private lateinit var binding: ActivityRootBinding

    private fun hidingVisibilityBNB() {
        binding.bottomNavBar.visibility = View.GONE
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBottomNavBar()

        if (savedInstanceState == null) {//проверяем какой запуск первый или нет (например, после поворота экрана)
            binding.bottomNavBar.selectedItemId = R.id.video_list_item
        } else {
            //todo иначе достать из --
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(notificationManager)
        }

        // получаем token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(TAG_FIREBASE, token)
            } else {
                Log.w(TAG_FIREBASE, "Неизвестная ошибка, отсутствует token")
            }
        }
    }

    // создаем канал
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "Channel name"
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            .apply {
                description = descriptionText
            }
        notificationManager.createNotificationChannel(channel)
    }

    private fun onBottomNavBar() {
        binding.bottomNavBar.setOnItemSelectedListener {
            TitleUtils.setupTitle(this, it)
            val fragment = when (it.itemId) {
                R.id.video_list_item -> VideoListFragment()
                R.id.favorite_item -> FavouritesFragment()
                R.id.history_item -> HistoryFragment()
                R.id.settings_item -> SettingsFragment()
                else -> throw IllegalStateException("Такого фрагмента нет")
            }
            swapFragment(fragment)
            return@setOnItemSelectedListener true
        }
    }

    private fun swapFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                binding.fragmentContainerFrameLayout.id,
                fragment,
                TAG_MAIN_CONTAINER_LAYOUT_KEY
            ).commit()
    }

    private fun openDetailsVideoFragment(videoId: String) {
        val fragment: Fragment = DetailsVideoFragment.newInstance(videoId)
        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentContainerFrameLayout.id, fragment, TAG_DETAILS_VIDEO_KEY)
            .addToBackStack(null)
            .commit()
        hidingVisibilityBNB()
    }

    private fun openContactsFragment() {
        val fragment: Fragment = ContactsFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainerFrameLayout.id, fragment, TAG_CONTACTS_KEY)
            .addToBackStack(null)
            .commit()
        hidingVisibilityBNB()
    }

    private fun openMapsGoogleFragment() {
        val fragment: Fragment = MapsFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainerFrameLayout.id, fragment, TAG_MAPS_KEY)
            .addToBackStack(null)
            .commit()
        hidingVisibilityBNB()
    }

    private fun openInformationVersion() {
        val fragment: Fragment = VersionFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainerFrameLayout.id, fragment, TAG_VERSION_KEY)
            .addToBackStack(null)
            .commit()
        hidingVisibilityBNB()
    }

    override fun openDetailsVideo(favoriteMovieDto: FavoriteMovieDto) {
        openDetailsVideoFragment(favoriteMovieDto.id)
    }

    override fun onBackPressed() {
        binding.bottomNavBar.visibility = View.VISIBLE
        super.onBackPressed()
    }

    override fun openContacts() {
        openContactsFragment()
    }

    override fun openMapsGoogle() {
        openMapsGoogleFragment()
    }

    override fun informationAboutTheApp() {
        openInformationVersion()
    }
}