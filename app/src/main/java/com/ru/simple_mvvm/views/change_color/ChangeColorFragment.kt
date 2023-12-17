package com.ru.simple_mvvm.views.change_color

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.databinding.FragmentChangeColorBinding
import com.ru.foundation.views.HasScreenTitle
import com.ru.foundation.views.BaseFragment
import com.ru.foundation.views.BaseScreen
import com.ru.foundation.views.screenViewModel
import com.ru.simple_mvvm.views.onTryAgain
import com.ru.simple_mvvm.views.renderSimpleResult
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

        viewModel.viewState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { result ->
                renderSimpleResult(root, result) {
                    adapter.colors = it.colors
                    with(binding) {
                        saveButton.isVisible = it.showSaveButton
                        cancelButton.isVisible = it.showCancelButton
                        saveProgressGroup.isVisible = it.showSaveProgressBar
                        saveProgressBar.progress = it.saveProgressPercentage
                        saveProgressPercentageTextView.text = it.saveProgressPercentageMessage
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.screenTitle.observe(viewLifecycleOwner) {
            notifyScreenUpdates()
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