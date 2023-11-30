package com.ru.foundation.model.tasks

interface ThreadUtils {

    fun sleep(milliseconds: Long)

    class Default: ThreadUtils {
        override fun sleep(milliseconds: Long) {
            Thread.sleep(milliseconds)
        }

    }

}