package com.aiforge.chat.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiforge.chat.ui.screens.ChatScreen
import com.aiforge.chat.ui.screens.ConversationsScreen
import com.aiforge.chat.ui.screens.SettingsScreen
import com.aiforge.chat.viewmodel.ConversationsViewModel
import com.aiforge.chat.viewmodel.SettingsViewModel

sealed class Screen(val route: String) {
    object Chat : Screen("chat/{conversationId}") {
        fun createRoute(conversationId: Long? = null): String {
            return if (conversationId != null) "chat/$conversationId" else "chat/new"
        }
    }
    object Conversations : Screen("conversations")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val conversationsViewModel: ConversationsViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Conversations.route
    ) {
        composable(Screen.Conversations.route) {
            ConversationsScreen(
                conversations = conversationsViewModel.conversations,
                onConversationClick = { conversationId ->
                    navController.navigate(Screen.Chat.createRoute(conversationId)) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNewChatClick = {
                    navController.navigate(Screen.Chat.createRoute(null)) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onDeleteConversation = { conversationId ->
                    conversationsViewModel.deleteConversation(conversationId)
                }
            )
        }

        composable("chat/{conversationId}") { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId")?.toLongOrNull()
            ChatScreen(
                conversationId = conversationId,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable("chat/new") {
            ChatScreen(
                conversationId = null,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                settings = settingsViewModel.settings,
                onSave = { settings ->
                    settingsViewModel.saveSettings(settings)
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() },
                onClearHistory = {
                    conversationsViewModel.deleteAllConversations()
                }
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    onNewChatClick: () -> Unit
) {
    val screens = listOf(
        Screen.Conversations,
        Screen.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarState = remember(currentDestination) {
        screens.any { screen ->
            currentDestination?.hierarchy?.any { it.route == screen.route } == true
        }
    }

    if (bottomBarState) {
        androidx.compose.material3.NavigationBar {
            screens.forEach { screen ->
                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                androidx.compose.material3.NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        when (screen) {
                            Screen.Conversations -> {
                                androidx.compose.material.icons.Icons.Default.Chat
                            }
                            Screen.Settings -> {
                                androidx.compose.material.icons.Icons.Default.Settings
                            }
                            else -> {
                                androidx.compose.material.icons.Icons.Default.Home
                            }
                        }
                    },
                    label = {
                        when (screen) {
                            Screen.Conversations -> "Chats"
                            Screen.Settings -> "Settings"
                            else -> ""
                        }
                    }
                )
            }
        }
    }
}
