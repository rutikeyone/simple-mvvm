package com.ru.simple_mvvm.views.change_color

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.databinding.FragmentChangeColorBinding
import com.ru.foundation.views.HasScreenTitle
import com.ru.foundation.views.BaseFragment
import com.ru.foundation.views.BaseScreen
import com.ru.foundation.views.screenViewModel
import com.ru.simple_mvvm.views.onTryAgain
import com.ru.simple_mvvm.views.renderSimpleResult

class ChangeColorFragment : BaseFragment(), HasScreenTitle {

    class Screen(
        val currentColorId: Long
    ) : BaseScreen

    override val viewModel by screenViewModel<ChangeColorViewModel>()

    private lateinit var binding: FragmentChangeColorBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChangeColorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        val adapter = ColorsAdapter(viewModel)
        setupLayoutManager(adapter)
        saveButton.setOnClickListener { viewModel.onSavePressed() }
        cancelButton.setOnClickListener { viewModel.onCancelPressed() }
        viewModel.viewState.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(root, result) {
                adapter.colors = it.colors
                with(binding) {
                    saveButton.isVisible = it.showSaveButton
                    cancelButton.isVisible = it.showCancelButton
                    saveProgressBar.isVisible = it.showSaveProgressBar
                }
            }
        }

        viewModel.screenTitle.observe(viewLifecycleOwner) {
            notifyScreenChanged()
        }

        onTryAgain(root) {
            viewModel.tryAgain()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun getScreenTitle(): String? =  viewModel.screenTitle.value

    private fun setupLayoutManager(adapter: ColorsAdapter) = with(binding) {
        val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.root.width
                val itemWith = resources.getDimensionPixelOffset(R.dimen.item_width)
                val columns = width / itemWith
                binding.colorsRecyclerView.adapter = adapter
                binding.colorsRecyclerView.layoutManager = GridLayoutManager(requireContext(), columns)
            }
        }
        root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }
}