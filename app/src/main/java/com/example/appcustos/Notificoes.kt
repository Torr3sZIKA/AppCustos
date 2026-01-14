package com.example.appcustos

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

object Notificoes {
    const val CHANNEL_ID = "GASTOS_CHANNEL"

    fun enviarNotificacaoGasto(contexto: Context, titulo: String, mensagem: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        val builder = NotificationCompat.Builder(contexto, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_warning) // Ícone de aviso para alertas
            .setContentTitle(titulo)
            .setContentText(mensagem)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Prioridade alta para o utilizador ver logo
            .setAutoCancel(true)

        val notificationManager = contexto.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}