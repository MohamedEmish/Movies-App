package com.amosh.feature.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.amosh.base.BaseRecyclerAdapter
import com.amosh.base.BaseViewHolder
import com.amosh.common.Constants
import com.amosh.feature.R
import com.amosh.feature.databinding.RowMovieItemLayoutBinding
import com.amosh.feature.model.MovieUiModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.shape.CornerFamily

/**
 * Adapter class for RecyclerView
 */
class MovieAdapter constructor(
    private val clickFunc: ((MovieUiModel?) -> Unit)? = null,
    private val fetchNext: (() -> Unit)? = null,
) : BaseRecyclerAdapter<MovieUiModel, RowMovieItemLayoutBinding, MovieAdapter.MovieViewHolder>(
    MovieItemDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = RowMovieItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieViewHolder(binding = binding, click = clickFunc, fetchNext = fetchNext)
    }

    inner class MovieViewHolder(
        private val binding: RowMovieItemLayoutBinding,
        private val click: ((MovieUiModel?) -> Unit)? = null,
        private val fetchNext: (() -> Unit)? = null
    ) : BaseViewHolder<MovieUiModel, RowMovieItemLayoutBinding>(binding) {

        override fun bind() {
            if ((currentList.size - 1) - absoluteAdapterPosition == Constants.LIST_BOTTOM_OFFSET)
                fetchNext?.invoke()

            getRowItem()?.let {
                with(binding) {
                    root.setOnClickListener {
                        click?.invoke(getRowItem())
                    }
                    ivImage.apply {
                        shapeAppearanceModel = binding.ivImage.shapeAppearanceModel
                            .toBuilder()
                            .setTopRightCorner(CornerFamily.ROUNDED, 36f)
                            .setTopLeftCorner(CornerFamily.ROUNDED, 36f)
                            .build()

                        val imageUrl = "https://image.tmdb.org/t/p/w300${it.poster_path}"
                        Glide.with(this)
                            .load(imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.ic_app_logo)
                            .placeholder(R.drawable.ic_app_logo)
                            .into(this)
                    }

                    tvTitle.text = it.title
                    tvRate.text = it.vote_average.toString()
                }
            }
        }
    }
}

class MovieItemDiffUtil : DiffUtil.ItemCallback<MovieUiModel>() {
    override fun areItemsTheSame(oldItem: MovieUiModel, newItem: MovieUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieUiModel, newItem: MovieUiModel): Boolean {
        return oldItem == newItem
    }
}