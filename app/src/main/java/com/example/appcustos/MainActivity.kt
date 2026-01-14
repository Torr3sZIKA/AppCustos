package com.example.appcustos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.appcustos.ui.theme.AppCustosTheme
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {
    private val loginViewModel: ViewModelLogin by viewModels()
    private val gastosViewModel: ViewModelGastos by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        criarCanalNotificacao()

        enableEdgeToEdge()
        setContent {
            AppCustosTheme {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted -> /* Logica se quiseres avisar o user */ }
                )

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                var estaLogado by remember { mutableStateOf(false) }
                var mostrarRegistro by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when {
                        estaLogado -> {
                            TelaPrincipal(
                                viewModel = gastosViewModel,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        mostrarRegistro -> {
                            TelaDeRegistro(
                                viewModel = loginViewModel,
                                modifier = Modifier.padding(innerPadding),
                                aoRegistrar = { mostrarRegistro = false },
                                aoVoltar = { mostrarRegistro = false }
                            )
                        }
                        else -> {
                            TelaDeLogin(
                                viewModel = loginViewModel,
                                modifier = Modifier.padding(innerPadding),
                                aoLogar = { estaLogado = true },
                                aoIrParaRegistro = { mostrarRegistro = true }
                            )
                        }
                    }
                }
            }
        }
    }
    private fun criarCanalNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nome = "Notificações de Gastos"
            val descricaoTexto = "Avisa quando um gasto é guardado"
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel("GASTOS_CHANNEL", nome, importancia).apply {
                description = descricaoTexto
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(canal)
        }
    }
}