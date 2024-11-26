package com.example.setpreference

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.setpreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private val channelId = "TEST_NOTIF"
    private val usernameData = "tes"
    private val passwordData = "123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi PrefManager
        prefManager = PrefManager.getInstance(this)

        // Pastikan NotificationChannel hanya dibuat satu kali
        createNotificationChannel()

        // Periksa status login
        checkLoginStatus()

        with(binding) {
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (username == usernameData && password == passwordData) {
                    prefManager.saveUsername(username)
                    checkLoginStatus()
                } else {
                    Toast.makeText(this@MainActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }

            btnLogout.setOnClickListener {
                prefManager.saveUsername("")
                checkLoginStatus()
            }

            btnClear.setOnClickListener {
                prefManager.clear()
                checkLoginStatus()
            }

            btnUpdate.setOnClickListener {
                val notifImage = BitmapFactory.decodeResource(resources, R.drawable.img)
                val builder = NotificationCompat.Builder(this@MainActivity, channelId)
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentTitle("Notifku")
                    .setContentText("Ini update notifikasi")
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(notifImage)
                    )
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, builder.build())
            }

            btnNotif.setOnClickListener {
                sendNotificationWithActions()
            }
        }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.getUsername()
        if (isLoggedIn.isEmpty()) {
            binding.llLogin.visibility = View.VISIBLE
            binding.llLogged.visibility = View.GONE
        } else {
            binding.llLogin.visibility = View.GONE
            binding.llLogged.visibility = View.VISIBLE
        }
    }

    private fun sendNotificationWithActions() {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        // PendingIntent untuk membaca pesan
        val readIntent = Intent(this, NotifReceiver::class.java).apply {
            action = "ACTION_READ"
            putExtra("MESSAGE", "Semangat kuliahnya!")
        }
        val readPendingIntent = PendingIntent.getBroadcast(this, 1, readIntent, flag)

        // PendingIntent untuk logout
        val logoutIntent = Intent(this, NotifReceiver::class.java).apply {
            action = "ACTION_LOGOUT"
        }
        val logoutPendingIntent = PendingIntent.getBroadcast(this, 2, logoutIntent, flag)

        // Membuat notifikasi dengan aksi
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("Hay Pacar")
            .setContentText("Semangat kuliahnya!!")
            .setAutoCancel(true)
            .addAction(0, "Baca", readPendingIntent)
            .addAction(0, "Logout", logoutPendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifikasi PBBB",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel untuk notifikasi latihan"
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
