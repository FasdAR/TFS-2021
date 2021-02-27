package ru.fasdev.homeworkone.background.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class SyncContactsBroadcastReceiver(private val activity: AppCompatActivity) : BroadcastReceiver() {
    companion object {
        const val ACTION_CONTACTS = "contacts"
        const val KEY_CONTACTS = "contacts"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            activity.setResult(AppCompatActivity.RESULT_OK, intent)
            activity.finish()
        }
    }
}
