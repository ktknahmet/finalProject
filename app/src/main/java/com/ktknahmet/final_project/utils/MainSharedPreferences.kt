package com.ktknahmet.final_project.utils

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class MainSharedPreferences (private val context: Context, private val prefName: String){

    private val gson by lazy { Gson() }

    private val sharedPrefs by lazy {
        val masterKeyAlias =
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM)

        EncryptedSharedPreferences.create(
            context,
            prefName,
            masterKeyAlias.build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    private val sharedPrefsEditor = sharedPrefs.edit()

    private val delimiter = "::"

    fun storeBoolean(key: String, value: Boolean) {
        sharedPrefsEditor.putBoolean(key, value).apply()
    }

    fun storeListBoolean(key: String, values: List<Boolean>) {
        val maskedBooleanList = mutableListOf<String>()
        values.forEach { item ->
            if (item) {
                maskedBooleanList.add("true")
            } else {
                maskedBooleanList.add("false")
            }
        }

        storeListString(key, maskedBooleanList)
    }
    fun storeString(key: String, value: String?) {
        sharedPrefsEditor.putString(key, value).apply()
    }


    fun storeStringSet(key: String, values: Set<String>) {
        sharedPrefsEditor.putStringSet(key, values).apply()
    }

    private fun storeListString(key: String, values: List<String>) {
        val arrayString: Array<String> = values.toTypedArray()
        sharedPrefsEditor.putString(key, TextUtils.join(delimiter, arrayString)).apply()
    }

    fun storeInt(key: String, value: Int) {
        sharedPrefsEditor.putInt(key, value).apply()
    }

    fun storeListInt(key: String, values: List<Int>) {
        val arrayInt: Array<Int> = values.toTypedArray()
        sharedPrefsEditor.putString(key, TextUtils.join(delimiter, arrayInt)).apply()
    }

    fun storeLong(key: String, value: Long) {
        sharedPrefsEditor.putLong(key, value).apply()
    }

    fun storeListLong(key: String, values: List<Long>) {
        val arrayLong: Array<Long> = values.toTypedArray()
        sharedPrefsEditor.putString(key, TextUtils.join(delimiter, arrayLong)).apply()
    }

    fun storeFloat(key: String, value: Float) {
        sharedPrefsEditor.putFloat(key, value).apply()
    }

    fun storeListFloat(key: String, values: List<Float>) {
        val arrayFloat: Array<Float> = values.toTypedArray()
        sharedPrefsEditor.putString(key, TextUtils.join(delimiter, arrayFloat)).apply()
    }


    fun storeDouble(key: String, value: Double) {
        sharedPrefsEditor.putString(key, value.toString()).apply()
    }


    fun storeListDouble(key: String, values: List<Double>) {
        val arrayDouble: Array<Double> = values.toTypedArray()
        sharedPrefsEditor.putString(key, TextUtils.join(delimiter, arrayDouble)).apply()
    }

    fun storeObject(key: String, value: Any) {
        sharedPrefsEditor.putString(key, gson.toJson(value)).apply()
    }

    fun storeListObject(key: String, values: List<Any>) {
        val objectStrings = mutableListOf<String>()
        values.forEach { obj ->
            objectStrings.add(gson.toJson(obj))
        }
        storeListString(key, objectStrings)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPrefs.getBoolean(key, defaultValue)
    }

    fun getListBoolean(key: String): List<Boolean> {
        val maskedBooleanList = getListString(key)
        return mutableListOf<Boolean>().apply {
            maskedBooleanList.forEach { item ->
                if (item == "true") {
                    this.add(true)
                } else {
                    this.add(false)
                }
            }
        }
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPrefs.getString(key, defaultValue)
    }

    fun getStringSet(key: String, defaultValues: Set<String> = setOf()): Set<String>? {
        return sharedPrefs.getStringSet(key, defaultValues)
    }

    fun getListString(key: String): List<String> {
        return TextUtils.split(sharedPrefs.getString(key, ""), delimiter).toList()
    }


    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPrefs.getInt(key, defaultValue)
    }

    fun getListInt(key: String): List<Int> {
        val maskedIntList = TextUtils.split(sharedPrefs.getString(key, ""), delimiter).toList()
        return mutableListOf<Int>().apply {
            maskedIntList.forEach { item -> this.add(item.toInt()) }
        }
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharedPrefs.getLong(key, defaultValue)
    }


    fun getListLong(key: String): List<Long> {
        val maskedLongList = TextUtils.split(sharedPrefs.getString(key, ""), delimiter).toList()
        return mutableListOf<Long>().apply {
            maskedLongList.forEach { item -> this.add(item.toLong()) }
        }
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sharedPrefs.getFloat(key, defaultValue)
    }

    fun getListFloat(key: String): List<Float> {
        val maskedFloatList = TextUtils.split(sharedPrefs.getString(key, ""), delimiter).toList()
        return mutableListOf<Float>().apply {
            maskedFloatList.forEach { item -> this.add(item.toFloat()) }
        }
    }


    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        val maskedDouble =
            sharedPrefs.getString(key, defaultValue.toString()) ?: defaultValue.toString()
        return maskedDouble.toDouble()
    }

    fun getListDouble(key: String): List<Double> {
        val maskedDoubleList = TextUtils.split(sharedPrefs.getString(key, ""), delimiter).toList()
        return mutableListOf<Double>().apply {
            maskedDoubleList.forEach { item -> this.add(item.toDouble()) }
        }
    }


    inline fun <reified T : Any> getObject(key: String): T {
        val objectString = getString(key)
        return `access$gson`.fromJson(objectString, T::class.java) ?: throw NullPointerException()
    }

    inline fun <reified T : Any> getListObject(key: String): List<T> {
        val objectStrings = getListString(key)
        val objects = mutableListOf<T>()

        objectStrings.forEach { objectString ->
            val value = `access$gson`.fromJson(objectString, T::class.java) ?: throw NullPointerException()
            objects.add(value)
        }

        return objects
    }

    fun remove(key: String) {
        sharedPrefsEditor.remove(key).apply()
    }

    fun clear() {
        sharedPrefsEditor.clear().apply()
    }
    @Suppress("PropertyName")
    @PublishedApi
    internal val `access$gson`: Gson
        get() = gson
}