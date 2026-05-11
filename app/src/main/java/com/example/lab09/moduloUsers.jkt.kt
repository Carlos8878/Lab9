package com.example.lab09

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ScreenUsers(servicio: PostApiService) {

    val users = remember { mutableStateListOf<UserModel>() }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val lista = servicio.getUsers()
            users.clear()
            users.addAll(lista)
            Log.d("ScreenUsers", "Cargados ${lista.size} usuarios")
        } catch (e: Exception) {
            errorMessage = "Error al cargar usuarios: ${e.message}"
            Log.e("ScreenUsers", "Error: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(users) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "@${user.username}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("📧 ${user.email}")
                            Text("📱 ${user.phone}")
                            if (!user.website.isNullOrBlank()) {
                                Text("🌐 ${user.website}")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "🏢 ${user.company}",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}