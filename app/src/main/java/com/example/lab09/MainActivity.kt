package com.example.lab09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://android-kotlin-fun-mars-server.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val servicio = retrofit.create(PostApiService::class.java)

        setContent {
            ProgPrincipal9(servicio)
        }
    }
}

// ==================== FUNCIÓN PRINCIPAL ====================
@Composable
fun ProgPrincipal9(servicio: PostApiService) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        content = { paddingValues ->
            Contenido(paddingValues, navController, servicio)
        }
    )
}

// ==================== BARRA SUPERIOR ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Laboratorio 09 - APIs",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

// ==================== BARRA INFERIOR (4 pestañas) ====================
@Composable
fun BarraInferior(navController: NavHostController) {
    NavigationBar(containerColor = Color.LightGray) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = navController.currentDestination?.route == "inicio",
            onClick = { navController.navigate("inicio") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Favorite, contentDescription = "Posts") },
            label = { Text("Posts") },
            selected = navController.currentDestination?.route == "posts",
            onClick = { navController.navigate("posts") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Usuarios") },
            label = { Text("Usuarios") },
            selected = navController.currentDestination?.route == "users",
            onClick = { navController.navigate("users") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Anfibios") },
            label = { Text("Anfibios") },
            selected = navController.currentDestination?.route == "amphibians",
            onClick = { navController.navigate("amphibians") }
        )
    }
}

// ==================== NAVEGACIÓN ====================
@Composable
fun Contenido(
    pv: PaddingValues,
    navController: NavHostController,
    servicio: PostApiService
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
    ) {
        NavHost(
            navController = navController,
            startDestination = "inicio"
        ) {
            composable("inicio") { ScreenInicio() }
            composable("posts") { ScreenPosts(navController, servicio) }
            composable("users") { ScreenUsers(servicio) }
            composable("amphibians") { ScreenAmphibians(servicio) }   // ← Nueva pestaña
            composable(
                "postsVer/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                ScreenPost(navController, servicio, it.arguments?.getInt("id") ?: 0)
            }
        }
    }
}

@Composable
fun ScreenInicio() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "¡Bienvenido al Laboratorio 09!",
            style = MaterialTheme.typography.headlineMedium)
    }
}