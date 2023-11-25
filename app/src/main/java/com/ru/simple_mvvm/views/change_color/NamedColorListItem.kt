package com.ru.simple_mvvm.views.change_color

import com.ru.simple_mvvm.model.colors.NamedColor

data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean,
)