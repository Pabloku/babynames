package com.kamisoft.babynames.commons.shared_preferences

import android.content.Context
import android.content.SharedPreferences

class AndroidPrefsManager(context: Context) : PreferencesManager {

    private val PREFS_FILE = "BabyNames"

    object Key {
        const val PARENT_NAMES_SET_DATETIME = "parent_names_set_datetime"
        const val PARENT1 = "parent1"
        const val PARENT1_NAME = "parent1_name"
        const val PARENT2 = "parent2"
        const val PARENT2_NAME = "parent2_name"
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    override fun getParentNamesSetDatetime(): Long = preferences.getLong(Key.PARENT_NAMES_SET_DATETIME, 0)

    override fun setParentNamesSetDatetime(firstUseInMillis: Long) = preferences.edit().putLong(Key.PARENT_NAMES_SET_DATETIME, firstUseInMillis).apply()

    override fun getParent1(): String = preferences.getString(Key.PARENT1, "")

    override fun setParent1(parent: String) = preferences.edit().putString(Key.PARENT1, parent).apply()

    override fun getParent1Name(): String = preferences.getString(Key.PARENT1_NAME, "")

    override fun setParent1Name(name: String) = preferences.edit().putString(Key.PARENT1_NAME, name).apply()

    override fun getParent2(): String = preferences.getString(Key.PARENT2, "")

    override fun setParent2(parent: String) = preferences.edit().putString(Key.PARENT2, parent).apply()

    override fun getParent2Name(): String = preferences.getString(Key.PARENT2_NAME, "")

    override fun setParent2Name(name: String) = preferences.edit().putString(Key.PARENT2_NAME, name).apply()

}