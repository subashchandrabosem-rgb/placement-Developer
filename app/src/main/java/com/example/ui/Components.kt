package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.*
import com.example.state.AppUiState
import com.example.state.AppViewModel
import com.example.state.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    uiState: AppUiState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    var showNotificationsDialog by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier.testTag("app_top_bar"),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        if (uiState.isAuthenticated) {
                            if (uiState.currentUserRole == UserRole.ADMIN) {
                                viewModel.navigateTo(Screen.ADMIN_DASHBOARD)
                            } else {
                                viewModel.navigateTo(Screen.STUDENT_DASHBOARD)
                            }
                        } else {
                            viewModel.navigateTo(Screen.LANDING)
                        }
                    }
                    .padding(vertical = 4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "PlacementPro Logo",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "PlacementPro",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.padding(horizontal = 2.dp)
                        ) {
                            Text(
                                text = "AI",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    if (uiState.isAuthenticated) {
                        Text(
                            text = if (uiState.currentUserRole == UserRole.ADMIN) "Admin Portal" else uiState.studentProfile.targetRole,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        },
        actions = {
            if (uiState.isAuthenticated) {
                // Global Search button
                IconButton(
                    onClick = { viewModel.setSearchOpen(true) },
                    modifier = Modifier.testTag("search_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search"
                    )
                }

                // Notification icon with badge
                val unreadCount = uiState.notifications.count { !it.isRead }
                IconButton(
                    onClick = { showNotificationsDialog = true },
                    modifier = Modifier.testTag("notifications_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadCount > 0) {
                                Badge { Text("$unreadCount") }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                }
            }

            // Theme toggle
            IconButton(
                onClick = { viewModel.toggleTheme() },
                modifier = Modifier.testTag("theme_toggle_button")
            ) {
                Icon(
                    imageVector = if (uiState.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme"
                )
            }

            // Logout or Role Switch
            if (uiState.isAuthenticated) {
                IconButton(
                    onClick = { viewModel.logout() },
                    modifier = Modifier.testTag("logout_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )

    if (showNotificationsDialog) {
        NotificationsDialog(
            notifications = uiState.notifications,
            onDismiss = { showNotificationsDialog = false },
            onMarkAllRead = {
                viewModel.markAllNotificationsAsRead()
            }
        )
    }

    if (uiState.isSearchOpen) {
        GlobalSearchDialog(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { viewModel.setSearchOpen(false) }
        )
    }
}

@Composable
fun AppBottomNav(
    currentScreen: Screen,
    userRole: UserRole,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .testTag("app_bottom_nav")
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        if (userRole == UserRole.ADMIN) {
            NavigationBarItem(
                selected = currentScreen == Screen.ADMIN_DASHBOARD,
                onClick = { onNavigate(Screen.ADMIN_DASHBOARD) },
                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                label = { Text("Overview") }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.ADMIN_QUESTIONS,
                onClick = { onNavigate(Screen.ADMIN_QUESTIONS) },
                icon = { Icon(Icons.Default.Quiz, contentDescription = "Questions") },
                label = { Text("Questions") }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.ADMIN_TESTS,
                onClick = { onNavigate(Screen.ADMIN_TESTS) },
                icon = { Icon(Icons.Default.Assignment, contentDescription = "Mock Tests") },
                label = { Text("Tests") }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.ADMIN_STUDENTS,
                onClick = { onNavigate(Screen.ADMIN_STUDENTS) },
                icon = { Icon(Icons.Default.People, contentDescription = "Students") },
                label = { Text("Students") }
            )
        } else {
            NavigationBarItem(
                selected = currentScreen == Screen.STUDENT_DASHBOARD,
                onClick = { onNavigate(Screen.STUDENT_DASHBOARD) },
                icon = { Icon(if (currentScreen == Screen.STUDENT_DASHBOARD) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "Home") },
                label = { Text("Home") }
            )
            NavigationBarItem(
                selected = currentScreen in listOf(Screen.PROGRAMMING, Screen.APTITUDE, Screen.REASONING, Screen.VERBAL, Screen.SOFT_SKILLS),
                onClick = { onNavigate(Screen.PROGRAMMING) },
                icon = { Icon(Icons.Default.Code, contentDescription = "Practice") },
                label = { Text("Practice") }
            )
            NavigationBarItem(
                selected = currentScreen in listOf(Screen.MOCK_TESTS, Screen.ACTIVE_TEST, Screen.TEST_RESULT),
                onClick = { onNavigate(Screen.MOCK_TESTS) },
                icon = { Icon(Icons.Default.Timer, contentDescription = "Tests") },
                label = { Text("Mock Tests") }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.AI_INTERVIEW,
                onClick = { onNavigate(Screen.AI_INTERVIEW) },
                icon = { Icon(Icons.Default.RecordVoiceOver, contentDescription = "AI Interview") },
                label = { Text("Interview") }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.AI_MENTOR,
                onClick = { onNavigate(Screen.AI_MENTOR) },
                icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Mentor") },
                label = { Text("AI Mentor") }
            )
            NavigationBarItem(
                selected = currentScreen in listOf(Screen.ANALYTICS, Screen.STUDY_PLAN, Screen.ACHIEVEMENTS),
                onClick = { onNavigate(Screen.ANALYTICS) },
                icon = { Icon(Icons.Default.Insights, contentDescription = "Analytics") },
                label = { Text("Analytics") }
            )
        }
    }
}

@Composable
fun ReadinessScoreGauge(
    score: Int,
    modifier: Modifier = Modifier,
    onClickFormula: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickFormula() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Placement Readiness Score",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Calculation info",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when {
                        score >= 85 -> "Excellent • High probability for Tier-1 offers"
                        score >= 70 -> "Placement Ready • Strong campus drive candidate"
                        score >= 50 -> "Good Progress • Focus on weaker interview areas"
                        else -> "Foundation Stage • Complete daily aptitude & coding"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { score / 100f },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = when {
                        score >= 80 -> MaterialTheme.colorScheme.primary
                        score >= 65 -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.tertiary
                    },
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$score%",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = Color.White
                    )
                    Text(
                        text = "READY",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationsDialog(
    notifications: List<NotificationItem>,
    onDismiss: () -> Unit,
    onMarkAllRead: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = onMarkAllRead) {
                        Text("Mark all read", style = MaterialTheme.typography.labelMedium)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                if (notifications.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No notifications right now.", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(notifications) { notif ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (notif.isRead) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                        else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                                    )
                                    .padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = when (notif.type) {
                                        "TEST" -> Icons.Default.Assignment
                                        "ACHIEVEMENT" -> Icons.Default.MilitaryTech
                                        else -> Icons.Default.Notifications
                                    },
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = notif.title,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        text = notif.message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun GlobalSearchDialog(
    uiState: AppUiState,
    viewModel: AppViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Search topics, questions, mock tests...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                val query = uiState.searchQuery.trim().lowercase()
                val matchedQuestions = remember(query, uiState.questions) {
                    if (query.isBlank()) emptyList()
                    else uiState.questions.filter {
                        it.topic.lowercase().contains(query) ||
                        it.question.lowercase().contains(query) ||
                        it.category.displayName.lowercase().contains(query)
                    }.take(4)
                }

                val matchedTests = remember(query, uiState.mockTests) {
                    if (query.isBlank()) emptyList()
                    else uiState.mockTests.filter {
                        it.title.lowercase().contains(query) ||
                        it.category.displayName.lowercase().contains(query)
                    }.take(3)
                }

                val matchedChallenges = remember(query, uiState.codingChallenges) {
                    if (query.isBlank()) emptyList()
                    else uiState.codingChallenges.filter {
                        it.title.lowercase().contains(query) ||
                        it.language.lowercase().contains(query) ||
                        it.topic.lowercase().contains(query)
                    }.take(2)
                }

                if (query.isBlank()) {
                    Text(
                        text = "Popular searches: Percentages, Two Sum, Binary Search, TCS, Time & Work, OOP, Blood Relations",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else if (matchedQuestions.isEmpty() && matchedTests.isEmpty() && matchedChallenges.isEmpty()) {
                    Text(
                        text = "No results found for '$query'. Try another keyword.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 350.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (matchedChallenges.isNotEmpty()) {
                            item {
                                Text("Coding Challenges", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                            }
                            items(matchedChallenges) { ch ->
                                SearchResultRow(
                                    icon = Icons.Default.Code,
                                    title = ch.title,
                                    subtitle = "${ch.language} • ${ch.difficulty.label} • ${ch.topic}",
                                    onClick = {
                                        onDismiss()
                                        viewModel.navigateTo(Screen.PROGRAMMING)
                                    }
                                )
                            }
                        }

                        if (matchedTests.isNotEmpty()) {
                            item {
                                Text("Mock Tests", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                            }
                            items(matchedTests) { test ->
                                SearchResultRow(
                                    icon = Icons.Default.Assignment,
                                    title = test.title,
                                    subtitle = "${test.durationMinutes} mins • ${test.totalQuestions} Questions",
                                    onClick = {
                                        onDismiss()
                                        viewModel.startMockTest(test.id)
                                    }
                                )
                            }
                        }

                        if (matchedQuestions.isNotEmpty()) {
                            item {
                                Text("Practice Questions", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                            }
                            items(matchedQuestions) { q ->
                                SearchResultRow(
                                    icon = Icons.Default.Quiz,
                                    title = q.topic,
                                    subtitle = q.question.take(65) + "...",
                                    onClick = {
                                        onDismiss()
                                        when (q.category) {
                                            QuestionCategory.APTITUDE -> viewModel.navigateTo(Screen.APTITUDE)
                                            QuestionCategory.REASONING -> viewModel.navigateTo(Screen.REASONING)
                                            QuestionCategory.VERBAL -> viewModel.navigateTo(Screen.VERBAL)
                                            else -> viewModel.navigateTo(Screen.PROGRAMMING)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun SearchResultRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun FormulaExplanationDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Readiness Score Formula",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "PlacementPro AI calculates your placement readiness using a transparent weighted composite benchmark modeled after premier campus hiring patterns:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))
                val weights = listOf(
                    "Programming & DSA" to "20%",
                    "Quantitative Aptitude" to "20%",
                    "Logical Reasoning" to "15%",
                    "Verbal Ability & Grammar" to "10%",
                    "Core Technical (OS/DBMS/CN)" to "15%",
                    "AI Mock Interview Performance" to "20%"
                )

                weights.forEach { (cat, weight) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(cat, style = MaterialTheme.typography.bodyMedium)
                        Text(weight, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Note: This metric reflects your balanced preparation depth across all rounds. It does not guarantee employment offers.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Got It")
                }
            }
        }
    }
}
