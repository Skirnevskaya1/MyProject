package com.example.myprojectjavaonkotlin.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myprojectjavaonkotlin.App
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.R.string
import com.example.myprojectjavaonkotlin.data.room.HistoryLocalRepo
import com.example.myprojectjavaonkotlin.databinding.FragmentDetailsVideoBinding
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.interactor.LikeInteractor
import com.example.myprojectjavaonkotlin.domain.repo.FavoriteMovieRepo
import com.example.myprojectjavaonkotlin.domain.repo.MovieWithFavoriteRepo
import com.example.myprojectjavaonkotlin.ui.utils.snack
import com.squareup.picasso.Picasso
import java.util.*

private const val DETAILS_VIDEO_KEY = "DETAILS_VIDEO_KEY"
private const val FRAGMENT_UUID_KEY = "FRAGMENT_UUID_KEY"

class DetailsVideoFragment : Fragment() {

    private var _binding: FragmentDetailsVideoBinding? = null
    private val binding get() = _binding!!

    private val app: App get() = requireActivity().application as App

    private val movieWithFavoriteRepo: MovieWithFavoriteRepo by lazy {
        app.di.movieWithFavoriteRepo
    }

    private val likeInteractor: LikeInteractor by lazy {
        app.di.likeInteractor
    }

    private val favoriteMovieRepo: FavoriteMovieRepo by lazy {
        app.di.favoriteMovieRepo
    }

    private val historyLocalRepo: HistoryLocalRepo by lazy {
        app.di.historyLocalRepo
    }

    /**
     * поздняя инициализация ViewModel, положили в него repo
     * в связи с тем что ViewModel при каждом повороте пересоздается, если необходимо
     * сохранять экран, необходимо ViewModel сохранить вне данного класса
     */

    private val viewModel: DetailsViewModel by viewModels {
        DetailsViewModel.Factory(
            movieWithFavoriteRepo,
            favoriteMovieRepo,
            requireArguments().getString(DETAILS_VIDEO_KEY)!!,
            historyLocalRepo
        )
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.videoLiveData.observe(viewLifecycleOwner) {
            setVideoEntity(it)
            markChosen(it.isFavorite)
            view.snack(getString(string.name_film) + it.title)
        }
    }

    private fun setVideoEntity(favoriteMovieDto: FavoriteMovieDto) {
        binding.nameDetailsTextView.text = favoriteMovieDto.title
        binding.genreDetailsTextView.text = favoriteMovieDto.genres
        binding.yearReleaseDetailsTextView.text = favoriteMovieDto.yearRelease
        binding.descriptionDetailsTextView.text = favoriteMovieDto.description

        markChosen(favoriteMovieDto.isFavorite)

        if (favoriteMovieDto.image.isNotBlank()) {
            //Picasso
            Picasso.get()
                .load(favoriteMovieDto.image)
                .fit() // это значит, что картинка будет размещена по выделенному размеру для нее.
                .placeholder(R.drawable.uploading_images)
                .into(binding.coverImageView)
            binding.coverImageView.scaleType =
                ImageView.ScaleType.FIT_CENTER// растягиваем картинку на весь элемент
        }

        saveMovie(favoriteMovieDto)

        binding.favoriteChoiceImageView.setOnClickListener {

            likeInteractor.changeLike(
                FavoriteMovieDto(
                    id = favoriteMovieDto.id,
                    image = favoriteMovieDto.image,
                    title = favoriteMovieDto.title,
                    description = favoriteMovieDto.description,
                    runtimeStr = favoriteMovieDto.runtimeStr,
                    genres = favoriteMovieDto.genres,
                    genreList = ArrayList(favoriteMovieDto.genreList),
                    yearRelease = favoriteMovieDto.yearRelease,
                    comment = favoriteMovieDto.comment,
                    isFavorite = favoriteMovieDto.isFavorite
                )
            )
            viewModel.onFavoriteChange(favoriteMovieDto)
            markChosen(!favoriteMovieDto.isFavorite)
        }
    }

    private fun saveMovie(favoriteMovieDto: FavoriteMovieDto) {
        viewModel.saveMovieToDb(
            FavoriteMovieDto(
                id = favoriteMovieDto.id,
                image = favoriteMovieDto.image,
                title = favoriteMovieDto.title,
                description = favoriteMovieDto.description,
                runtimeStr = favoriteMovieDto.runtimeStr,
                genres = favoriteMovieDto.genres,
                genreList = ArrayList(favoriteMovieDto.genreList),
                yearRelease = favoriteMovieDto.yearRelease,
                comment = favoriteMovieDto.comment,
                isFavorite = favoriteMovieDto.isFavorite
            )
        )
    }

    private fun markChosen(isFavorite: Boolean) {
        binding.favoriteChoiceImageView.setImageResource(
            if (isFavorite)
                R.drawable.favourites_icon_filled
            else
                R.drawable.favourites_icon
        )
    }

    companion object {
        fun newInstance(videoId: String) =
            DetailsVideoFragment().apply {
                arguments = Bundle().apply {
                    putString(DETAILS_VIDEO_KEY, videoId)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
