package com.example.lab09

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.navigation.NavHostController

@Composable
fun ScreenAmphibians(servicio: PostApiService) {

    val amphibians = remember { mutableStateListOf<AmphibianModel>() }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val lista = servicio.getAmphibians()
            amphibians.clear()
            amphibians.addAll(lista)
            Log.d("Amphibians", "Cargados ${lista.size} anfibios")
        } catch (e: Exception) {
            error = "Error: ${e.message}"
            Log.e("Amphibians", "Error", e)
        } finally {
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = error!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(amphibians) { amphibian ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column {
                            AsyncImage(
                                model = amphibian.imgSrc,
                                contentDescription = amphibian.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = amphibian.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = amphibian.type,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = amphibian.description)
                            }
                        }
                    }
                }
            }
        }
    }
}