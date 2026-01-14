package com.example.appcustos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TelaPrincipal(viewModel: ViewModelGastos, modifier: Modifier = Modifier) {
    val contexto = LocalContext.current
    val corFundoFosco = Color(0xFF121212)
    val corCardFosco = Color(0xFF1E1E1E)

    val listaDeGastos by viewModel.gastos.collectAsState()
    val orcamento by viewModel.orcamento.collectAsState()

    var mostrarDialogoGasto by remember { mutableStateOf(false) }
    var mostrarDialogoOrcamento by remember { mutableStateOf(false) }

    val categoriasCores = mapOf(
        "Alimentação" to Color(0xFF64B5F6), "Transporte" to Color(0xFF81C784),
        "Lazer" to Color(0xFFFFD54F), "Saúde" to Color(0xFFE57373), "Outros" to Color(0xFFBA68C8)
    )

    LaunchedEffect(Unit) { viewModel.carregarDados() }

    Scaffold(
        containerColor = corFundoFosco,
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogoGasto = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    ) { padding ->
        Column(modifier = modifier.padding(padding).fillMaxSize().padding(16.dp)) {

            val totalGeral = listaDeGastos.sumOf { it.valor }
            val percentagemUso = if (orcamento > 0) (totalGeral / orcamento).toFloat() else 0f
            val corProgresso = if (percentagemUso > 1f) Color.Red else MaterialTheme.colorScheme.primary

            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clickable { mostrarDialogoOrcamento = true },
                colors = CardDefaults.cardColors(containerColor = corCardFosco)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Total Gasto", color = Color.Gray, style = MaterialTheme.typography.labelLarge)
                            Text("${String.format("%.2f", totalGeral)} €", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Orçamento", color = Color.Gray, style = MaterialTheme.typography.labelLarge)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${orcamento.toInt()} €", color = Color.White, style = MaterialTheme.typography.titleMedium)
                                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp).padding(start = 4.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { percentagemUso.coerceAtMost(1f) },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = corProgresso,
                        trackColor = Color.White.copy(alpha = 0.1f),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    if (totalGeral > orcamento) {
                        Text("Limite ultrapassado!", color = Color.Red, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }

            Text("Distribuição", color = Color.Gray, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(vertical = 8.dp))
            Row(modifier = Modifier.fillMaxWidth().height(12.dp).background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(6.dp))) {
                if (totalGeral > 0) {
                    categoriasCores.forEach { (cat, cor) ->
                        val totalCat = listaDeGastos.filter { it.categoria == cat }.sumOf { it.valor }
                        val proporcao = (totalCat / totalGeral).toFloat()
                        if (proporcao > 0) Box(modifier = Modifier.fillMaxHeight().weight(proporcao).background(cor))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(listaDeGastos) { gasto ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = corCardFosco)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).background(categoriasCores[gasto.categoria] ?: Color.Gray, RoundedCornerShape(5.dp)))
                            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                                Text(gasto.descricao, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                                Text(gasto.categoria, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                            }
                            Text("${String.format("%.2f", gasto.valor)} €", color = Color.White, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { viewModel.eliminarGasto(gasto.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(alpha = 0.6f))
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoOrcamento) {
        var tempOrcamento by remember { mutableStateOf(orcamento.toString()) }
        AlertDialog(
            onDismissRequest = { mostrarDialogoOrcamento = false },
            containerColor = corCardFosco,
            title = { Text("Definir Orçamento Mensal", color = Color.White) },
            text = {
                OutlinedTextField(
                    value = tempOrcamento, onValueChange = { tempOrcamento = it },
                    label = { Text("Valor do Orçamento") }, modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.atualizarOrcamento(tempOrcamento.toDoubleOrNull() ?: orcamento)
                    mostrarDialogoOrcamento = false
                }) { Text("Confirmar") }
            }
        )
    }

    if (mostrarDialogoGasto) {
        DialogoNovoGasto(
            categorias = categoriasCores.keys.toList(), corFundo = corCardFosco,
            onDismiss = { mostrarDialogoGasto = false },
            onConfirm = { d, v, c -> viewModel.adicionarNovoGasto(contexto, d, v, c); mostrarDialogoGasto = false }
        )
    }
}

@Composable
fun DialogoNovoGasto(categorias: List<String>, corFundo: Color, onDismiss: () -> Unit, onConfirm: (String, Double, String) -> Unit) {
    var desc by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var cat by remember { mutableStateOf(categorias[0]) }
    AlertDialog(
        onDismissRequest = onDismiss, containerColor = corFundo,
        title = { Text("Novo Gasto", color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Descrição") }, colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                OutlinedTextField(value = valor, onValueChange = { valor = it }, label = { Text("Valor") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                ScrollableTabRow(selectedTabIndex = categorias.indexOf(cat), containerColor = Color.Transparent, edgePadding = 0.dp, divider = {}) {
                    categorias.forEach { Tab(selected = cat == it, onClick = { cat = it }, text = { Text(it, style = MaterialTheme.typography.bodySmall) }) }
                }
            }
        },
        confirmButton = { Button(onClick = { onConfirm(desc, valor.replace(",",".").toDoubleOrNull() ?: 0.0, cat) }) { Text("Guardar") } }
    )
}