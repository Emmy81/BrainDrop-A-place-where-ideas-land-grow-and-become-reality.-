package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ai.GeminiService
import com.example.data.database.BrainDropDatabase
import com.example.data.repository.IdeaRepository
import com.example.ui.navigation.AiAssistantRoute
import com.example.ui.navigation.AppBottomNavigation
import com.example.ui.navigation.BrainLibraryRoute
import com.example.ui.navigation.CreateIdeaRoute
import com.example.ui.navigation.HomeRoute
import com.example.ui.navigation.IdeaDetailRoute
import com.example.ui.navigation.ProfileRoute
import com.example.ui.navigation.SplashRoute
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.BrainLibraryScreen
import com.example.ui.screens.CreateIdeaScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IdeaDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AiAssistantViewModel
import com.example.viewmodel.AiAssistantViewModelFactory
import com.example.viewmodel.IdeaViewModel
import com.example.viewmodel.IdeaViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = BrainDropDatabase.getDatabase(this)
        val repository = IdeaRepository(database.ideaDao())
        val geminiService = GeminiService()

        setContent {
            MyApplicationTheme {
                val ideaViewModel: IdeaViewModel = viewModel(factory = IdeaViewModelFactory(repository, geminiService))
                val aiAssistantViewModel: AiAssistantViewModel = viewModel(factory = AiAssistantViewModelFactory(geminiService))

                BrainDropApp(ideaViewModel, aiAssistantViewModel)
            }
        }
    }
}

@Composable
fun BrainDropApp(ideaViewModel: IdeaViewModel, aiAssistantViewModel: AiAssistantViewModel) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { AppBottomNavigation(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = SplashRoute) {
                composable<SplashRoute> {
                    SplashScreen(onNavigateToHome = {
                        navController.navigate(HomeRoute) {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    })
                }
                composable<HomeRoute> {
                    HomeScreen(
                        viewModel = ideaViewModel,
                        onNavigateToCreate = { navController.navigate(CreateIdeaRoute) },
                        onNavigateToDetail = { id -> navController.navigate(IdeaDetailRoute(id)) },
                        onNavigateToAi = { navController.navigate(AiAssistantRoute) }
                    )
                }
                composable<BrainLibraryRoute> {
                    BrainLibraryScreen(
                        viewModel = ideaViewModel,
                        onNavigateToDetail = { id -> navController.navigate(IdeaDetailRoute(id)) }
                    )
                }
                composable<CreateIdeaRoute> {
                    CreateIdeaScreen(
                        viewModel = ideaViewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToAi = { navController.navigate(AiAssistantRoute) }
                    )
                }
                composable<IdeaDetailRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<IdeaDetailRoute>()
                    IdeaDetailScreen(
                        ideaId = route.ideaId,
                        viewModel = ideaViewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToAi = { navController.navigate(AiAssistantRoute) }
                    )
                }
                composable<AiAssistantRoute> {
                    AiAssistantScreen(viewModel = aiAssistantViewModel)
                }
                composable<ProfileRoute> {
                    ProfileScreen(viewModel = ideaViewModel, aiViewModel = aiAssistantViewModel)
                }
            }
        }
    }
}
