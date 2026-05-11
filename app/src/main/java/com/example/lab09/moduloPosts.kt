package com.example.lab09

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

// ==================== PANTALLA DE LISTA DE POSTS ====================
@Composable
fun ScreenPosts(
    navController: NavHostController,
    servicio: PostApiService
) {

    val listaPosts = remember { mutableStateListOf<PostModel>() }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val listado = servicio.getUserPosts()
            listaPosts.clear()
            listaPosts.addAll(listado)
            errorMsg = null
            Log.d("ScreenPosts", "Cargados ${listado.size} posts")
        } catch (e: Exception) {
            Log.e("ScreenPosts", "Error: ${e.message}")
            errorMsg = e.message
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        if (listaPosts.isEmpty()) {
            item {
                Text(
                    text = errorMsg ?: "Sin posts para mostrar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(listaPosts) { item ->
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = item.id.toString(),
                        modifier = Modifier.weight(0.1f),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = item.title,
                        modifier = Modifier.weight(0.8f)
                    )
                    IconButton(
                        onClick = { navController.navigate("postsVer/${item.id}") },
                        modifier = Modifier.weight(0.1f)
                    ) {
                        Icon(Icons.Outlined.Search, contentDescription = "Ver")
                    }
                }
            }
        }
    }
}

// ==================== PANTALLA DE DETALLE DE POST ====================
@Composable
fun ScreenPost(
    navController: NavHostController,
    servicio: PostApiService,
    id: Int
) {
    var post by remember { mutableStateOf<PostModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(id) {
        try {
            isLoading = true
            errorMsg = null
            val xpost = servicio.getUserPostById(id)
            post = xpost
        } catch (e: Exception) {
            Log.e("ScreenPost", "Error cargando post $id: ${e.message}")
            errorMsg = e.message
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMsg != null) {
            Text(
                text = "Error: $errorMsg",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        } else if (post != null) {
            OutlinedTextField(
                value = post?.id?.toString().orEmpty(),
                onValueChange = {},
                label = { Text("ID") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = post?.userId?.toString().orEmpty(),
                onValueChange = {},
                label = { Text("User ID") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = post?.title.orEmpty(),
                onValueChange = {},
                label = { Text("Título") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = post?.body.orEmpty(),
                onValueChange = {},
                label = { Text("Contenido") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}