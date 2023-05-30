package com.universe.findingfalcone.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, onChange: (T) -> Unit, onNullValue: (() -> Unit)? = null) {
    observe(lifecycleOwner, Observer { if (it == null) onNullValue?.invoke() else onChange(it) })
}