package com.ktknahmet.final_project.utils.sharedPreferences

import com.ktknahmet.final_project.utils.Constant.DISABLE
import com.ktknahmet.final_project.utils.Constant.ENABLE
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.directBootCtx

class SettingsPref {
    private val prefs = MainSharedPreferences(directBootCtx, MyPref.settingsPrefs)
    @Suppress("unused")
    fun getBoolean(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }
    fun getInt(key: String): Int {
        return prefs.getInt(key, 0)
    }
    fun getString(key: String): String? {
        return prefs.getString(key, "")
    }
    fun statusAnimation(): Boolean {
        if (prefs.getInt(MyPref.animation, DISABLE) == ENABLE) {
            return true
        }
        return false
    }
}