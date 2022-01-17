package com.amosh.feature.ui.filterSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.amosh.common.ScreenUtils
import com.amosh.domain.entity.ListType
import com.amosh.domain.entity.SortBy
import com.amosh.feature.R
import com.amosh.feature.databinding.FragmentFilterSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var listener: OnActionsListener
    private var sortBy: SortBy? = null


    override fun onStart() {
        super.onStart()
        // To show the sheet full height
        val dialog = dialog
        if (dialog != null) {
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        val view = view
        view?.post {
            val parent = view.parent as View
            val params: CoordinatorLayout.LayoutParams =
                parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior
            bottomSheetBehavior.peekHeight = ScreenUtils(requireContext()).height
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            rbHighestRate.isChecked = sortBy == SortBy.HIGHEST_RATE
            rbMostPopular.isChecked = sortBy == SortBy.MOST_POPULAR
            rbNone.isChecked = sortBy == SortBy.NONE

            btnDone.setOnClickListener {
                val selectedSort = when {
                    rbHighestRate.isChecked -> SortBy.HIGHEST_RATE
                    rbMostPopular.isChecked -> SortBy.MOST_POPULAR
                    else -> SortBy.NONE
                }

                listener.onDoneListener(
                    selectedSort
                )
            }
        }
    }

    interface OnActionsListener {
        fun onDoneListener(sortBy: SortBy)
    }

    companion object {
        const val TAG = "FilterSheetFragment"

        @JvmStatic
        fun newInstance(
            listener: OnActionsListener,
            sortBy: SortBy?,
        ): FilterSheetFragment {
            val fragment = FilterSheetFragment()
            fragment.listener = listener
            fragment.sortBy = sortBy
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}