package com.example.capstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsPreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Set preferences dengan file XML
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Dapatkan referensi ke SwitchPreference untuk Dark Mode
        val darkModeSwitch: SwitchPreferenceCompat? = findPreference("dark_mode")

        // Set status Switch berdasarkan mode saat ini
        darkModeSwitch?.let {
            it.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

            // Listener untuk mengubah mode saat switch diubah
            it.setOnPreferenceChangeListener { _, newValue ->
                val isDarkModeEnabled = newValue as Boolean
                if (isDarkModeEnabled) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                true
            }
        }
    }
}
