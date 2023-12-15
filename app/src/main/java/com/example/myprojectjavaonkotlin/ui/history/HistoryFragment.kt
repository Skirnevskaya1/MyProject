package com.example.myprojectjavaonkotlin.ui.history

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprojectjavaonkotlin.AppStateMovie
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.databinding.FragmentHistoryBinding
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.ui.utils.hide
import com.example.myprojectjavaonkotlin.ui.utils.show
import com.example.myprojectjavaonkotlin.ui.utils.showSnackBar
import java.util.*

private const val FRAGMENT_UUID_KEY = "FRAGMENT_UUID_KEY"

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    private lateinit var adapter: HistoryAdapter

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

        _binding = FragmentHistoryBinding.bind(view)

        initView()

        viewModel.historyLiveData.observe(viewLifecycleOwner) {
            renderData(it)
        }

        viewModel.selectedVideoLiveData.observe(viewLifecycleOwner) {
            getController().openDetailsVideo(it)
        }

        viewModel.getAllHistory()
    }

    private fun initView() {
        binding.historyCollectionVideoRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = HistoryAdapter(
            onVideoClickListener = {
                viewModel.onVideoClick(it)
            }
        )
        binding.historyCollectionVideoRecyclerView.adapter = adapter
    }

    private fun renderData(appStateMovie: AppStateMovie) {
        when (appStateMovie) {
            is AppStateMovie.SuccessMovie -> {
                binding.historyCollectionVideoRecyclerView.show()
                binding.progressTaskBar.hide()
                adapter.setData(appStateMovie.movieData)
            }
            is AppStateMovie.Loading -> {
                binding.historyCollectionVideoRecyclerView.hide()
                binding.progressTaskBar.show()
            }
            is AppStateMovie.Error -> {
                binding.historyCollectionVideoRecyclerView.show()
                binding.progressTaskBar.hide()
                binding.historyCollectionVideoRecyclerView.showSnackBar(
                    "Ошибка",
                    "Перезагрузить"
                ) {
                    viewModel.getAllHistory()
                }
            }
        }
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