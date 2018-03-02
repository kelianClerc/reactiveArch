package com.fabernovel.technologies.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (t: T) -> Unit) =
    this.observe(owner, Observer { it?.let { observer(it) } })

fun Any?.toSimpleString() = this
    .toString()
    .replaceBeforeLast('.', "")
    .replaceAfter('@', "")
    .replace(".", "")
    .replace("@", "")
    .replace('$','.')
