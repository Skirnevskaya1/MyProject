package com.example.myprojectjavaonkotlin.ui.favourites

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprojectjavaonkotlin.App
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.databinding.FragmentFavouritesBinding
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.repo.FavoriteMovieRepo
import java.util.*

private const val FRAGMENT_UUID_KEY = "FRAGMENT_UUID_KEY"

class FavouritesFragment : Fragment(R.layout.fragment_favourites) {

    private val app: App get() = requireActivity().application as App

    private val favoriteMovieRepo: FavoriteMovieRepo by lazy {
        app.di.favoriteMovieRepo
    }

    private lateinit var adapter: FavoriteAdapter

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteListViewModel by lazy {
        ViewModelProvider(
            this,
            FavoriteListViewModel.Factory(
                favoriteMovieRepo
            )
        )[FavoriteListViewModel::class.java]
    }

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

        _binding = FragmentFavouritesBinding.bind(view)

        initView()

        viewModel.inProgressLiveData.observe(viewLifecycleOwner) { inProgress ->
            binding.favoriteCollectionVideoRecyclerView.isVisible = !inProgress
            binding.progressTaskBar.isVisible = inProgress
        }

        viewModel.favoriteListLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.selectedVideoLiveData.observe(viewLifecycleOwner) {
            getController().openDetailsVideo(it)
        }
    }

    private fun initView() {
        binding.favoriteCollectionVideoRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FavoriteAdapter(
            data = emptyList(),
            onDetailVideoListener = {
                viewModel.onVideoClick(it)
            }
        )
        binding.favoriteCollectionVideoRecyclerView.adapter = adapter
    }

    interface Controller {
        fun openDetailsVideo(favoriteMovieDto: FavoriteMovieDto)
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