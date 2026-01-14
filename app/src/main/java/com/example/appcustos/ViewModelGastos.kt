package com.example.appcustos

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Gasto(
    val id: String = "",
    val descricao: String = "",
    val valor: Double = 0.0,
    val categoria: String = ""
)

class ViewModelGastos : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val _gastos = MutableStateFlow<List<Gasto>>(emptyList())
    val gastos: StateFlow<List<Gasto>> = _gastos

    private val _orcamento = MutableStateFlow(0.0)
    val orcamento: StateFlow<Double> = _orcamento

    fun carregarDados() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("usuarios").document(userId).collection("gastos")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val lista = snapshot.documents.mapNotNull { it.toObject(Gasto::class.java)?.copy(id = it.id) }
                    _gastos.value = lista
                }
            }

        db.collection("usuarios").document(userId).get().addOnSuccessListener { doc ->
            _orcamento.value = doc.getDouble("orcamento") ?: 1000.0
        }
    }

    fun atualizarOrcamento(novoValor: Double) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(userId).set(mapOf("orcamento" to novoValor))
        _orcamento.value = novoValor
    }

    fun adicionarNovoGasto(contexto: Context, descricao: String, valor: Double, categoria: String) {
        val userId = auth.currentUser?.uid ?: return
        val novoGasto = hashMapOf(
            "descricao" to descricao,
            "valor" to valor,
            "categoria" to categoria
        )

        db.collection("usuarios").document(userId).collection("gastos")
            .add(novoGasto)
            .addOnSuccessListener {
                Notificoes.enviarNotificacaoGasto(
                    contexto,
                    "Gasto Registado!",
                    "Adicionaste $descricao (${String.format("%.2f", valor)}€)"
                )
                val totalAtual = _gastos.value.sumOf { it.valor }
                if (totalAtual > _orcamento.value) {
                    Notificoes.enviarNotificacaoGasto(
                        contexto,
                        "Atenção: Limite Excedido!",
                        "Ultrapassaste o teu orçamento mensal de ${String.format("%.2f", _orcamento.value)}€!"
                    )
                }
            }
    }

    fun eliminarGasto(idGasto: String) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(userId).collection("gastos").document(idGasto).delete()
    }
}