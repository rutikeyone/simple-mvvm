package com.ru.simple_mvvm.views.current_color

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ru.foundation.model.ErrorResult
import com.ru.foundation.model.PendingResult
import com.ru.foundation.model.SuccessResult
import com.ru.simple_mvvm.databinding.FragmentCurrentColorBinding
import com.ru.foundation.views.BaseFragment
import com.ru.foundation.views.BaseScreen
import com.ru.foundation.views.screenViewModel
import com.ru.simple_mvvm.views.onTryAgain
import com.ru.simple_mvvm.views.renderSimpleResult

class CurrentColorFragment() : BaseFragment() {

     class Screen: BaseScreen

     override val viewModel by screenViewModel<CurrentColorViewModel>()

     private lateinit var binding: FragmentCurrentColorBinding

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
          binding = FragmentCurrentColorBinding.inflate(inflater, container, false)
          return binding.root
     }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
          viewModel.currentColor.observe(viewLifecycleOwner) { result ->
               renderSimpleResult(
                    root = root,
                    result = result,
                    onSuccessResult = {
                         colorView.setBackgroundColor(it.value)
                    }
               )
          }

          this.changeColorButton.setOnClickListener {
               viewModel.changeColor()
          }

          this.askPermissionsButton.setOnClickListener {
               viewModel.requestPermission()
          }

          onTryAgain(
               root = binding.root,
               onTryAgainPressed = { viewModel.tryAgain() }
          )

          super.onViewCreated(view, savedInstanceState)
     }
}