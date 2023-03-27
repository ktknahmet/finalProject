@file:Suppress("unused")

package com.ktknahmet.final_project.utils.exceptions

/** Throws an [IllegalStateException] with a message that includes [value]. */
fun unexpectedValue(value: Any?): Nothing = throw IllegalStateException("Unexpected value: $value")

@Deprecated("Use error from stdlib instead", ReplaceWith("error(errorMessage)"))
/** Throws an [IllegalStateException] with the passed message. */
fun illegal(errorMessage: String = ""): Nothing = error(errorMessage)

/** Throws an [UnsupportedOperationException] with the passed message. */
fun unsupported(
    errorMessage: String? = null
): Nothing = throw UnsupportedOperationException(errorMessage)
