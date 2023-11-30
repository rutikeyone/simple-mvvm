package com.ru.foundation.utils.delegates

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

class Await<T> {
    private val countDownLatch = CountDownLatch(1)

    private var finalResult = AtomicReference<T>(null)

    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        countDownLatch.await()
        return finalResult.get()
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if(value == null) return
        if(finalResult.compareAndSet(null, value)) {
            countDownLatch.countDown()
        }
    }

}