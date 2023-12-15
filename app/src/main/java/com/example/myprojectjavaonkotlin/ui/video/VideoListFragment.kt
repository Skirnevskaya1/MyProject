package com.example.myprojectjavaonkotlin.ui.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprojectjavaonkotlin.App
import com.example.myprojectjavaonkotlin.MyReceiver
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.databinding.FragmentVideoListBinding
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.interactor.CollectionInteractor
import java.util.*

private const val FRAGMENT_UUID_KEY = "FRAGMENT_UUID_KEY"

class VideoListFragment : Fragment(R.layout.fragment_video_list) {

    private val app: App get() = requireActivity().application as App

    private val collectionVideoRepo: CollectionInteractor by lazy {
        app.di.collectionInteractor
    }

    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!

    /**
     * поздняя инициализация ViewModel, положили в него repo
     * в связи с тем что ViewModel при каждом повороте пересоздается, если необходимо
     * сохранять экран, необходимо ViewModel сохранить вне данного класса
     */
    private val viewModel: VideoListViewModel by lazy {
        ViewModelProvider(
            this,
            VideoListViewModelFactory(
                collectionVideoRepo
            )
        )[VideoListViewModel::class.java]
    }

    private lateinit var adapter: CollectionVideoAdapter

    //уникальный id (для того чтобы можно было сохранить состояние экрана за пределами класса
    private lateinit var fragmentUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //проверка, есть ли это значение, если нет то создаем его
        fragmentUid =
            savedInstanceState?.getString(FRAGMENT_UUID_KEY) ?: UUID.randomUUID().toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //при сохроанении положить ID
        outState.putString(FRAGMENT_UUID_KEY, fragmentUid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentVideoListBinding.bind(view)

        initView()
//        throw RuntimeException("Test Crash") // Force a crash

        viewModel.inProgressLiveData.observe(viewLifecycleOwner) { inProgress ->
            binding.collectionVideoRecyclerView.isVisible = !inProgress
            binding.progressTaskBar.isVisible = inProgress
        }

        viewModel.videoListLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.selectedVideoLiveData.observe(viewLifecycleOwner) {
            getController().openDetailsVideo(it)
        }

        onReceiver()
    }

    private fun onReceiver() {
        context?.let {
            it.sendBroadcast(Intent(it, MyReceiver::class.java).apply {
                putExtra(
                    "Action",
                    "Запущен фрагмент Видео (Receiver)"
                )
            })
        }
    }

    private fun initView() {
        binding.collectionVideoRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CollectionVideoAdapter(
            data = emptyList(),
            onVideoClickListener = {
                viewModel.onVideoClick(it)
            }
        )
        binding.collectionVideoRecyclerView.adapter = adapter
    }

    interface Controller {
        fun openDetailsVideo(favoriteMovieDto: FavoriteMovieDto)
    }

    private fun getController(): Controller = activity as Controller

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }
}