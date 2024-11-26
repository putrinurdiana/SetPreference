package com.example.setpreference

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class NotifReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (context != null) {
            when (action) {
                "ACTION_LOGOUT" -> {
                    // Hapus data sesi
                    PrefManager.getInstance(context).clear()
                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()

                    // Log untuk memastikan logout
                    Log.d("NotifReceiver", "Logout action received, clearing data")

                    // Kembali ke halaman login
                    val loginIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(loginIntent)
                }
                else -> {
                    val msg = intent?.getStringExtra("MESSAGE")
                    if (msg != null) {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        Log.d("NotifReceiver", "Received message: $msg")
                    }
                }
            }
        }
    }
}
