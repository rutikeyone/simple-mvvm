package com.ru.simple_mvvm.views.current_color

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.databinding.FragmentCurrentColorBinding
import com.ru.simple_mvvm.views.base.BaseFragment
import com.ru.simple_mvvm.views.base.BaseScreen
import com.ru.simple_mvvm.views.base.BaseViewModel
import com.ru.simple_mvvm.views.base.screenViewModel
import kotlin.properties.Delegates

class CurrentColorFragment() : BaseFragment() {

     class Screen: BaseScreen

     override val viewModel by screenViewModel<CurrentColorViewModel>()

     private lateinit var binding: FragmentCurrentColorBinding

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
          binding = FragmentCurrentColorBinding.inflate(inflater, container, false)
          return binding.root
     }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
          viewModel.currentColor.observe(viewLifecycleOwner) {
               colorView.setBackgroundColor(it.value)
          }

          this.changeColorButton.setOnClickListener {
               viewModel.changeColor()
          }

          super.onViewCreated(view, savedInstanceState)
     }
}