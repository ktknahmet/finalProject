package com.ktknahmet.final_project.utils.pinview

import android.content.res.Resources

/**
 * Created 07.05.2021
 */
object PinUtils {
    fun dpToPx(dp: Float): Float {
        return dp * Resources.getSystem().displayMetrics.density
    }
}
