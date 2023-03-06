package org.teslasoft.assistant.settings

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import org.teslasoft.assistant.R
import org.teslasoft.assistant.onboarding.ActivationActivity

class SettingsActivity : FragmentActivity() {

    private var btnChangeApi: LinearLayout? = null
    private var btnChangeAccount: LinearLayout? = null
    private var silenceSwitch: MaterialSwitch? = null
    private var btnClearChat: MaterialButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnChangeApi = findViewById(R.id.btn_manage_api)
        btnChangeAccount = findViewById(R.id.btn_manage_account)
        silenceSwitch = findViewById(R.id.silent_switch)
        btnClearChat = findViewById(R.id.btn_clear_chat)

        btnChangeApi?.setOnClickListener {
            startActivity(Intent(this, ActivationActivity::class.java))
            finish()
        }

        btnChangeAccount?.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("https://platform.openai.com/account")
            startActivity(intent)
        }

        btnClearChat?.setOnClickListener {
            MaterialAlertDialogBuilder(this, R.style.App_MaterialAlertDialog)
                .setTitle("Confirm")
                .setMessage("Are you sure? This action can not be undone.")
                .setPositiveButton("Clear") { _, _ ->
                    run {
                        val sharedPreferences: SharedPreferences = getSharedPreferences("chat", MODE_PRIVATE)
                        val editor: Editor = sharedPreferences.edit()
                        editor.putString("chat", "[]")
                        editor.apply()
                        Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .show()
        }

        val silenceSettings: SharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val silenceMode = silenceSettings.getBoolean("silence_mode", false)

        silenceSwitch?.isChecked = silenceMode

        silenceSwitch?.setOnCheckedChangeListener { _, isChecked ->
            run {
                val editor: SharedPreferences.Editor = silenceSettings.edit()
                if (isChecked) {
                    editor.putBoolean("silence_mode", true)
                } else {
                    editor.putBoolean("silence_mode", false)
                }

                editor.apply()
            }
        }
    }
}