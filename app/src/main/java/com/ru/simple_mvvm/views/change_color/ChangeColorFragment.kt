package com.ru.simple_mvvm.views.change_color

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.databinding.FragmentChangeColorBinding
import com.ru.foundation.views.HasScreenTitle
import com.ru.foundation.views.BaseFragment
import com.ru.foundation.views.BaseScreen
import com.ru.foundation.views.screenViewModel

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
        viewModel.colorList.observe(viewLifecycleOwner) {
            adapter.colors = it
        }

        viewModel.screenTitle.observe(viewLifecycleOwner) {
            notifyScreenChanged()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun getScreenTitle(): String? =  viewModel.screenTitle.value

    private fun setupLayoutManager(adapter: ColorsAdapter) = with(binding) {
        val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.colorsRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.colorsRecyclerView.width
                val itemWith = resources.getDimensionPixelOffset(R.dimen.item_width)
                val columns = width / itemWith
                binding.colorsRecyclerView.adapter = adapter
                binding.colorsRecyclerView.layoutManager = GridLayoutManager(requireContext(), columns)
            }
        }
        colorsRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }
}