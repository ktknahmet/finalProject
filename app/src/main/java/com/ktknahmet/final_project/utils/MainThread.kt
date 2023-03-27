@file:Suppress("unused")

package com.ktknahmet.final_project.utils

import android.os.Looper

/** This main looper cache avoids synchronization overhead when accessed repeatedly. */
@JvmField
val mainLooper: Looper = Looper.getMainLooper()!!
@JvmField
val mainThread: Thread = mainLooper.thread

val isMainThread: Boolean inline get() = mainThread === Thread.currentThread()

@PublishedApi
internal val currentThread: Any? inline get() = Thread.currentThread()
