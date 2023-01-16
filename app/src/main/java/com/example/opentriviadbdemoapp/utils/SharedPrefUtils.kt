package com.example.opentriviadbdemoapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class SharedPrefUtils {

    companion object {
        private const val THEME = "theme"
        private const val TOKEN = "token"

        fun customSharedPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences("filename", Context.MODE_PRIVATE)

        private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
            val edit = edit()
            operation(edit)
            edit.apply()
        }

        var SharedPreferences.theme
            get() = getBoolean(THEME, false)
            set(value) {
                edit() {
                    it.putBoolean(THEME, value)

                    if (value) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }

        var SharedPreferences.token
            get() = getString(TOKEN, "")
            set(value){
                edit(){
                    it.putString(TOKEN, value)
                }
            }
    }
}