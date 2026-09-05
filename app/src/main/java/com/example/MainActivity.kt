package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.QuestionCategory
import com.example.data.UserRole
import com.example.state.AppViewModel
import com.example.state.Screen
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(uiState.toastMessage) {
                val msg = uiState.toastMessage
                if (msg != null) {
                    snackbarHostState.showSnackbar(msg)
                }
            }

            MyApplicationTheme(darkTheme = uiState.isDarkTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        AppTopBar(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    },
                    bottomBar = {
                        if (uiState.isAuthenticated) {
                            AppBottomNav(
                                currentScreen = uiState.currentScreen,
                                userRole = uiState.currentUserRole,
                                onNavigate = { screen -> viewModel.navigateTo(screen) }
                            )
                        }
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    contentWindowInsets = ScaffoldDefaults.contentWindowInsets
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (uiState.currentScreen) {
                            Screen.LANDING -> LandingScreen(viewModel = viewModel)
                            Screen.LOGIN, Screen.REGISTER -> AuthScreen(uiState = uiState, viewModel = viewModel)
                            Screen.STUDENT_DASHBOARD -> StudentDashboardScreen(uiState = uiState, viewModel = viewModel)
                            Screen.PROGRAMMING -> ProgrammingScreen(uiState = uiState, viewModel = viewModel)
                            Screen.APTITUDE -> PracticeHubScreen(initialCategory = QuestionCategory.APTITUDE, uiState = uiState, viewModel = viewModel)
                            Screen.REASONING -> PracticeHubScreen(initialCategory = QuestionCategory.REASONING, uiState = uiState, viewModel = viewModel)
                            Screen.VERBAL -> PracticeHubScreen(initialCategory = QuestionCategory.VERBAL, uiState = uiState, viewModel = viewModel)
                            Screen.SOFT_SKILLS -> PracticeHubScreen(initialCategory = QuestionCategory.SOFT_SKILLS, uiState = uiState, viewModel = viewModel)
                            Screen.AI_MENTOR -> AiMentorScreen(uiState = uiState, viewModel = viewModel)
                            Screen.AI_INTERVIEW -> AiInterviewScreen(uiState = uiState, viewModel = viewModel)
                            Screen.MOCK_TESTS -> MockTestListScreen(uiState = uiState, viewModel = viewModel)
                            Screen.ACTIVE_TEST -> ActiveTestRunnerScreen(uiState = uiState, viewModel = viewModel)
                            Screen.TEST_RESULT -> TestResultScreen(uiState = uiState, viewModel = viewModel)
                            Screen.ANALYTICS, Screen.STUDY_PLAN, Screen.ACHIEVEMENTS -> AnalyticsAndStudyScreen(uiState = uiState, viewModel = viewModel)
                            Screen.ADMIN_DASHBOARD -> AdminOverviewScreen(uiState = uiState, viewModel = viewModel)
                            Screen.ADMIN_QUESTIONS -> AdminQuestionsScreen(uiState = uiState, viewModel = viewModel)
                            Screen.ADMIN_TESTS -> AdminTestsScreen(uiState = uiState, viewModel = viewModel)
                            Screen.ADMIN_STUDENTS -> AdminStudentsScreen(uiState = uiState, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
