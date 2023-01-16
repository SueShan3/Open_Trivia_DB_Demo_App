package com.example.opentriviadbdemoapp.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.utils.SharedPrefUtils
import com.example.opentriviadbdemoapp.utils.SharedPrefUtils.Companion.theme
import com.example.opentriviadbdemoapp.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, SettingActivity::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var dataBinding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        supportActionBar?.apply {
            title = "Settings"
            setDisplayHomeAsUpEnabled(true)
        }

        // When changed system theme in settings, configuration.uiMode will reflect the change.
        // Reflect the button is checked
        dataBinding.btnEnable.isChecked =
            resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        dataBinding.btnEnable.setOnCheckedChangeListener { _, isChecked ->
            SharedPrefUtils.customSharedPrefs(this).theme = isChecked
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}