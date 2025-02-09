package com.kasiopec.qjournal

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.kasiopec.qjournal.databinding.ActivitySettingsBinding
import com.kasiopec.qjournal.view.viewBinding

class SettingsActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySettingsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }


        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        if (!preferences.contains(NOTIFICATION_TIME_KEY)) {
            preferences.edit {
                putString(NOTIFICATION_TIME_KEY, DEFAULT_NOTIFICATION_TIME.toString())
                apply()
            }
        }

        setupSwitches(preferences)

        with(binding) {
            switchDaily.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                preferences.edit {
                    putBoolean(ApplicationActivity.DAILY_NOTIFICATION_BOOL, isChecked)
                    apply()
                }
            }
            switchResetGen.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
                preferences.edit {
                    putBoolean(ApplicationActivity.RESET_NOTIFICATIONS, checked)
                    apply()
                }
                switchWeekly.apply {
                    isChecked = checked
                    isClickable = checked
                }
                switchMonthly.apply {
                    isChecked = checked
                    isClickable = checked
                }
            }

            switchWeekly.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                preferences.edit {
                    putBoolean(ApplicationActivity.WEEKLY_NOTIFICATIONS_BOOL, isChecked)
                    apply()
                }
            }

            switchMonthly.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                preferences.edit {
                    putBoolean(ApplicationActivity.MONTHLY_NOTIFICATIONS_BOOL, isChecked)
                    apply()
                }
            }
            val notifyTime = preferences.getString(NOTIFICATION_TIME_KEY, DEFAULT_NOTIFICATION_TIME.toString())
            notificationIntervalEditField.setText(notifyTime.toString())

            btnApply.setOnClickListener {
                preferences.edit {
                    putString(
                        NOTIFICATION_TIME_KEY,
                        notificationIntervalEditField.getText().toString()
                    )
                    apply()
                }
                startActivity(Intent(this@SettingsActivity, MainActivity::class.java))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSwitches(preferences: SharedPreferences) {
        with(binding) {
            val isResetNotificationsEnabled = preferences.getBoolean(ApplicationActivity.RESET_NOTIFICATIONS, true)
            val isDailyNotificationsEnabled = preferences.getBoolean(ApplicationActivity.DAILY_NOTIFICATION_BOOL, true)
            val isWeeklyNotificationsEnabled = preferences.getBoolean(ApplicationActivity.WEEKLY_NOTIFICATIONS_BOOL, true)
            val isMonthlyNotificationsEnabled = preferences.getBoolean(ApplicationActivity.MONTHLY_NOTIFICATIONS_BOOL, true)

            switchWeekly.isChecked = isWeeklyNotificationsEnabled
            switchMonthly.isChecked = isMonthlyNotificationsEnabled
            switchDaily.isChecked = isDailyNotificationsEnabled
            switchResetGen.isChecked = isResetNotificationsEnabled
            switchResetGen.isChecked = isResetNotificationsEnabled.not()
            switchMonthly.isClickable = isResetNotificationsEnabled.not()
            switchWeekly.isClickable = isResetNotificationsEnabled.not()
        }
    }

    companion object {
        private const val DEFAULT_NOTIFICATION_TIME = 2.0
        const val NOTIFICATION_TIME_KEY: String = "NotificationTime"
    }
}
