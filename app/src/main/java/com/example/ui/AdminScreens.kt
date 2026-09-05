package com.example.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.*
import com.example.state.AppUiState
import com.example.state.AppViewModel
import com.example.state.Screen

@Composable
fun AdminOverviewScreen(
    uiState: AppUiState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    var showAddQuestionDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Admin Management Portal",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Campus Placement System Administration & Batch Analytics",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Authorized", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
        }

        // 6 Platform Statistics Metrics
        item {
            Text("Platform Real-Time Analytics", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AdminStatCard("Students", "${SampleData.sampleAdminStudents.size + 142}", Icons.Default.People, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                AdminStatCard("Active Today", "87", Icons.Default.TrendingUp, Color(0xFF10B981), Modifier.weight(1f))
                AdminStatCard("Questions", "${uiState.questions.size}", Icons.Default.Quiz, MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AdminStatCard("Mock Tests", "${uiState.mockTests.size}", Icons.Default.Assignment, MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
                AdminStatCard("Tests Taken", "328", Icons.Default.FactCheck, Color(0xFF8B5CF6), Modifier.weight(1f))
                AdminStatCard("Avg Batch Score", "74.8%", Icons.Default.Analytics, Color(0xFFEC4899), Modifier.weight(1f))
            }
        }

        // Quick Administrative Actions
        item {
            Text("Quick Management Actions", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { showAddQuestionDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("admin_add_question_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Question")
                }

                OutlinedButton(
                    onClick = { viewModel.navigateTo(Screen.ADMIN_TESTS) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Manage Tests")
                }
            }
        }

        // Top Placement Candidates / Student Roster preview
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Active Student Candidates", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        TextButton(onClick = { viewModel.navigateTo(Screen.ADMIN_STUDENTS) }) {
                            Text("View All", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    SampleData.sampleAdminStudents.take(3).forEach { st ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = st.name.first().toString(),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(st.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Text("${st.department} • ${st.targetRole}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "${st.testsCompleted} Tests",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddQuestionDialog) {
        AddQuestionDialog(
            onDismiss = { showAddQuestionDialog = false },
            onAdd = { cat, topic, diff, qText, opts, correctIdx, expl, tags ->
                viewModel.addQuestion(cat, topic, diff, qText, opts, correctIdx, expl, tags)
                showAddQuestionDialog = false
            }
        )
    }
}

@Composable
fun AdminQuestionsScreen(
    uiState: AppUiState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCatFilter by remember { mutableStateOf<QuestionCategory?>(null) }
    var questionToDelete by remember { mutableStateOf<Question?>(null) }

    val filteredQuestions = remember(selectedCatFilter, uiState.questions) {
        if (selectedCatFilter == null) uiState.questions
        else uiState.questions.filter { it.category == selectedCatFilter }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Question Bank Management", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Text("${filteredQuestions.size} Questions available in repository", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.testTag("admin_new_question_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New")
                }
            }
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedCatFilter == null,
                    onClick = { selectedCatFilter = null },
                    label = { Text("All", style = MaterialTheme.typography.labelSmall) }
                )
                QuestionCategory.values().take(4).forEach { cat ->
                    FilterChip(
                        selected = selectedCatFilter == cat,
                        onClick = { selectedCatFilter = cat },
                        label = { Text(cat.displayName, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
        }

        // Question List
        items(filteredQuestions) { q ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${q.category.displayName} • ${q.topic}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        IconButton(
                            onClick = { questionToDelete = q },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(q.question, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Correct Answer: Option ${('A' + q.correctAnswerIndex)}: ${q.options.getOrElse(q.correctAnswerIndex) { "" }}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddQuestionDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { cat, topic, diff, qText, opts, correctIdx, expl, tags ->
                viewModel.addQuestion(cat, topic, diff, qText, opts, correctIdx, expl, tags)
                showAddDialog = false
            }
        )
    }

    if (questionToDelete != null) {
        AlertDialog(
            onDismissRequest = { questionToDelete = null },
            title = { Text("Delete Question?") },
            text = { Text("Are you sure you want to permanently delete this question from the bank?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteQuestion(questionToDelete!!.id)
                        questionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { questionToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminTestsScreen(
    uiState: AppUiState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Mock Assessments Management", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text("Publish or unpublish mock placement tests for candidates", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        items(uiState.mockTests) { test ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(test.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text("${test.durationMinutes} mins • ${test.totalQuestions} Questions • ${test.category.displayName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (test.isPublished) "Published" else "Draft",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (test.isPublished) Color(0xFF10B981) else MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = test.isPublished,
                            onCheckedChange = { viewModel.togglePublishMockTest(test.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStudentsScreen(
    uiState: AppUiState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    var selectedStudentForDetail by remember { mutableStateOf<StudentProfile?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Registered Student Profiles", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text("Monitor individual candidate preparation progress and metrics", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        items(SampleData.sampleAdminStudents) { student ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedStudentForDetail = student }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = student.name.first().toString(),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(student.name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text(student.college, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                        Text("Target: ${student.targetRole}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("${student.testsCompleted} Tests", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        Text("${student.problemsSolved} Solved", style = MaterialTheme.typography.labelSmall, color = Color(0xFF10B981))
                    }
                }
            }
        }
    }

    if (selectedStudentForDetail != null) {
        val st = selectedStudentForDetail!!
        Dialog(onDismissRequest = { selectedStudentForDetail = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(st.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Text(st.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("College: ${st.college}", style = MaterialTheme.typography.bodySmall)
                    Text("Dept: ${st.department} (${st.year})", style = MaterialTheme.typography.bodySmall)
                    Text("Role Goal: ${st.targetRole}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        ScoreColumn("Tests", "${st.testsCompleted}")
                        ScoreColumn("Solved", "${st.problemsSolved}")
                        ScoreColumn("Interviews", "${st.interviewsCompleted}")
                        ScoreColumn("Streak", "${st.currentStreak}d")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { selectedStudentForDetail = null }, modifier = Modifier.fillMaxWidth()) {
                        Text("Close Profile")
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Text(label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
    }
}

@Composable
fun AddQuestionDialog(
    onDismiss: () -> Unit,
    onAdd: (QuestionCategory, String, Difficulty, String, List<String>, Int, String, List<String>) -> Unit
) {
    var category by remember { mutableStateOf(QuestionCategory.APTITUDE) }
    var topic by remember { mutableStateOf("Percentages") }
    var difficulty by remember { mutableStateOf(Difficulty.BEGINNER) }
    var questionText by remember { mutableStateOf("") }
    var optA by remember { mutableStateOf("") }
    var optB by remember { mutableStateOf("") }
    var optC by remember { mutableStateOf("") }
    var optD by remember { mutableStateOf("") }
    var correctIdx by remember { mutableIntStateOf(0) }
    var explanation by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    Text("Add Question to Bank", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }

                item {
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Topic (e.g. Percentages, Recursion)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = questionText,
                        onValueChange = { questionText = it },
                        label = { Text("Question Statement") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(value = optA, onValueChange = { optA = it }, label = { Text("Option A") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
                item {
                    OutlinedTextField(value = optB, onValueChange = { optB = it }, label = { Text("Option B") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
                item {
                    OutlinedTextField(value = optC, onValueChange = { optC = it }, label = { Text("Option C") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
                item {
                    OutlinedTextField(value = optD, onValueChange = { optD = it }, label = { Text("Option D") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }

                item {
                    Text("Correct Option: ${('A' + correctIdx)}", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("A", "B", "C", "D").forEachIndexed { idx, label ->
                            FilterChip(
                                selected = correctIdx == idx,
                                onClick = { correctIdx = idx },
                                label = { Text(label) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = explanation,
                        onValueChange = { explanation = it },
                        label = { Text("Step-by-step Solution Explanation") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onDismiss) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (questionText.isNotBlank()) {
                                    onAdd(
                                        category,
                                        topic,
                                        difficulty,
                                        questionText,
                                        listOf(optA.ifBlank { "Option 1" }, optB.ifBlank { "Option 2" }, optC.ifBlank { "Option 3" }, optD.ifBlank { "Option 4" }),
                                        correctIdx,
                                        explanation.ifBlank { "Direct formula solution applies." },
                                        listOf(topic)
                                    )
                                }
                            }
                        ) {
                            Text("Publish Question")
                        }
                    }
                }
            }
        }
    }
}
