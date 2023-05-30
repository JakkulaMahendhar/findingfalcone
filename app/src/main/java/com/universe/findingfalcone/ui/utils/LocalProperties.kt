package com.universe.findingfalcone.ui.utils

import android.content.SharedPreferences
import javax.inject.Inject

class LocalProperties @Inject constructor(private val pref: SharedPreferences) {
    companion object {
        private const val TOKEN = "token"
    }

    private var editor: SharedPreferences.Editor = pref.edit()

    var token: String?
        get() = pref.getString(TOKEN, null)
        set(v) {
            editor.putString(TOKEN, v)
            editor.commit()
        }
}