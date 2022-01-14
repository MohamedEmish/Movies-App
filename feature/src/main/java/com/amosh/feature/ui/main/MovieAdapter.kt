package com.amosh.feature.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.amosh.base.BaseRecyclerAdapter
import com.amosh.feature.databinding.RowMovieItemLayoutBinding
import com.amosh.feature.model.MovieUiModel

/**
 * Adapter class for RecyclerView
 */
class MovieAdapter constructor(
    private val clickFunc: ((MovieUiModel?) -> Unit)? = null
) : BaseRecyclerAdapter<MovieUiModel, RowMovieItemLayoutBinding, MovieViewHolder>(
    WeatherItemDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = RowMovieItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieViewHolder(binding = binding, click = clickFunc)
    }

}

class WeatherItemDiffUtil : DiffUtil.ItemCallback<MovieUiModel>() {
    override fun areItemsTheSame(oldItem: MovieUiModel, newItem: MovieUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieUiModel, newItem: MovieUiModel): Boolean {
        return oldItem == newItem
    }
}