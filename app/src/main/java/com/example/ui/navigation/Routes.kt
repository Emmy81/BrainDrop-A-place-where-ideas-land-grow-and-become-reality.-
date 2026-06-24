package com.example.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object HomeRoute

@Serializable
object BrainLibraryRoute

@Serializable
object AiAssistantRoute

@Serializable
object ProfileRoute

@Serializable
object CreateIdeaRoute

@Serializable
data class IdeaDetailRoute(val ideaId: Int)
