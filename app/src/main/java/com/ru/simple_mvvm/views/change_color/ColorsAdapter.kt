package com.ru.simple_mvvm.views.change_color

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ru.simple_mvvm.databinding.ItemColorBinding
import com.ru.simple_mvvm.model.colors.NamedColor

class ColorsAdapter(
    private val listener: Listener,
): RecyclerView.Adapter<ColorsAdapter.Holder>(), View.OnClickListener {

    var colors: List<NamedColorListItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemColorBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val namedColor = colors[position].namedColor
        val selected = colors[position].selected
        with(holder.binding) {
            root.tag = namedColor
            colorNameTextView.text = namedColor.name
            colorView.setBackgroundColor(namedColor.value)
            selectedIndicatorImageView.isVisible = selected
        }
    }

    override fun onClick(v: View) {
        val color = v.tag as? NamedColor ?: return
        listener.onColorChosen(color)
    }

    class Holder(
        val binding: ItemColorBinding,
    ): RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun onColorChosen(namedColor: NamedColor)
    }

}