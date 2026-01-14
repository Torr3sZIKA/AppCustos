package com.example.appcustos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun TelaDeLogin(
    viewModel: ViewModelLogin,
    modifier: Modifier = Modifier,
    aoLogar: () -> Unit,
    aoIrParaRegistro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val contexto = LocalContext.current

    val corFundoFosco = Color(0xFF121212)
    val corCardFosco = Color(0xFF1E1E1E)
    val corPrimaria = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(corFundoFosco) // Fundo igual às outras telas
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bem-vindo de volta",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Faça login para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = corCardFosco,
            unfocusedContainerColor = corCardFosco,
            focusedBorderColor = corPrimaria,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
            focusedLabelColor = corPrimaria,
            unfocusedLabelColor = Color.Gray,
            cursorColor = corPrimaria
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Palavra-passe") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.fazerLogin(
                    email,
                    senha,
                    aoSucesso = {
                        Toast.makeText(contexto, "Bem-vindo!", Toast.LENGTH_SHORT).show()
                        aoLogar()
                    },
                    aoErro = { erro ->
                        val msgTraduzida = if (erro.contains("invalid-credential") || erro.contains("wrong-password")) {
                            "Email ou palavra-passe incorretos!"
                        } else {
                            "Erro ao entrar. Verifique a sua ligação."
                        }
                        Toast.makeText(contexto, msgTraduzida, Toast.LENGTH_LONG).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Entrar", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = aoIrParaRegistro) {
            Text(
                text = "Ainda não tem conta? Registe-se aqui",
                color = Color.Gray
            )
        }
    }
}