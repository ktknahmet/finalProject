package com.ktknahmet.final_project.utils.initprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri
import com.ktknahmet.final_project.utils.exceptions.unsupported
import com.ktknahmet.final_project.utils.initprovider.ObsoleteContentProviderHack


/**
 * Base class for [ContentProvider]s used for initialization purposes.
 */
@ObsoleteContentProviderHack
abstract class InitProvider : ContentProvider() {
    final override fun insert(uri: Uri, values: ContentValues?) = unsupported()
    final override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ) = unsupported()

    final override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ) = unsupported()

    final override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ) = unsupported()

    final override fun getType(uri: Uri) = unsupported()
}
