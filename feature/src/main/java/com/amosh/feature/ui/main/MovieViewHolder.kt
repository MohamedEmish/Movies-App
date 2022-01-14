package com.amosh.feature.ui.main

import com.amosh.base.BaseViewHolder
import com.amosh.feature.R
import com.amosh.feature.databinding.RowMovieItemLayoutBinding
import com.amosh.feature.model.MovieUiModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.shape.CornerFamily

/**
 * ViewHolder class for Movie
 */
class MovieViewHolder constructor(
    private val binding: RowMovieItemLayoutBinding,
    private val click: ((MovieUiModel?) -> Unit)? = null
) : BaseViewHolder<MovieUiModel, RowMovieItemLayoutBinding>(binding) {


    init {
        binding.root.setOnClickListener {
            click?.invoke(getRowItem())
        }
    }

    override fun bind() {
        getRowItem()?.let {
            with(binding) {
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