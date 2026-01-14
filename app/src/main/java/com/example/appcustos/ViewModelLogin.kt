package com.example.appcustos

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ViewModelLogin : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    fun fazerLogin(email: String, senha: String, aoSucesso: () -> Unit, aoErro: (String) -> Unit) {
        if (email.isBlank() || senha.isBlank()) {
            aoErro("Preencha todos os campos!")
            return
        }

        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                aoSucesso()
            }
            .addOnFailureListener { e ->
                aoErro(e.message ?: "Erro desconhecido")
            }
    }

    fun criarConta(email: String, senha: String, aoSucesso: () -> Unit, aoErro: (String) -> Unit) {
        if (email.isBlank() || senha.isBlank()) {
            aoErro("Preencha todos os campos!")
            return
        }

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                aoSucesso()
            }
            .addOnFailureListener { e ->
                aoErro(e.message ?: "Erro desconhecido")
            }
    }
}