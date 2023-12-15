package com.example.myprojectjavaonkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Прописать в Manifests -
<receiver android:name=".MyReceiver"
android:exported="true">
<intent-filter>
<action android:name="android.intent.action.LOCALE_CHANGED" />
</intent-filter>
</receiver>
 */

class MyReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("СООБЩЕНИЕ ОТ СИСТЕМЫ\n")
            if (intent.action == null) {
                append("Action: ${intent.getStringExtra("Action")}")
            } else {
                append("Action: ${intent.action}")
            }

            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}
