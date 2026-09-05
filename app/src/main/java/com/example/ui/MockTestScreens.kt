package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Timer
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
fun MockTestListScreen(
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
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Mock Placement Assessments",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Simulated online tests aligned with TCS NQT, Infosys SP/DSE, and Cognizant formats.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(uiState.mockTests.filter { it.isPublished }) { test ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = test.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = test.category.displayName,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = test.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${test.durationMinutes} mins", style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${test.totalQuestions} Questions", style = MaterialTheme.typography.labelMedium)
                        }

                        Button(
                            onClick = { viewModel.startMockTest(test.id) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("start_test_button_${test.id}")
                        ) {
                            Text("Start Test")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveTestRunnerScreen(
    uiState: AppUiState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val activeState = uiState.activeTestState
    if (activeState == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var showPaletteDialog by remember { mutableStateOf(false) }
    var showSubmitConfirmation by remember { mutableStateOf(false) }

    val currentQ = activeState.questions.getOrNull(activeState.currentQuestionIndex)
    val selectedOption = currentQ?.let { activeState.selectedAnswers[it.id] }
    val isMarked = currentQ?.let { activeState.markedForReview.contains(it.id) } ?: false

    val minutes = activeState.timeRemainingSeconds / 60
    val seconds = activeState.timeRemainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val isTimeWarning = activeState.timeRemainingSeconds < 120

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Sticky Timer & Progress Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = activeState.test.title,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1
                    )
                    Text(
                        text = "Question ${activeState.currentQuestionIndex + 1} of ${activeState.questions.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Timer Chip
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isTimeWarning) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = if (isTimeWarning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = timeFormatted,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = if (isTimeWarning) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Question Palette Icon
                    IconButton(onClick = { showPaletteDialog = true }) {
                        Icon(Icons.Default.GridView, contentDescription = "Question Palette")
                    }
                }
            }
        }

        // Active Question View
        if (currentQ != null) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = currentQ.topic,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (isMarked) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = MaterialTheme.colorScheme.tertiaryContainer
                                    ) {
                                        Text(
                                            text = "Marked for Review",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = currentQ.question,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )

                            if (currentQ.codeSnippet != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = currentQ.codeSnippet,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            currentQ.options.forEachIndexed { optIndex, optText ->
                                val isSelected = selectedOption == optIndex
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            viewModel.selectTestAnswer(currentQ.id, optIndex)
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { viewModel.selectTestAnswer(currentQ.id, optIndex) }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${('A' + optIndex)}. $optText",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Action Bar: Previous, Mark for Review, Next, Submit
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.setTestQuestionIndex(activeState.currentQuestionIndex - 1)
                    },
                    enabled = activeState.currentQuestionIndex > 0
                ) {
                    Text("Prev")
                }

                OutlinedButton(
                    onClick = {
                        if (currentQ != null) viewModel.toggleMarkForReview(currentQ.id)
                    }
                ) {
                    Text(if (isMarked) "Unmark" else "Mark Review")
                }

                if (activeState.currentQuestionIndex < activeState.questions.size - 1) {
                    Button(
                        onClick = {
                            viewModel.setTestQuestionIndex(activeState.currentQuestionIndex + 1)
                        }
                    ) {
                        Text("Next")
                    }
                } else {
                    Button(
                        onClick = { showSubmitConfirmation = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.testTag("submit_test_button")
                    ) {
                        Text("Submit Test")
                    }
                }
            }
        }
    }

    // Palette Dialog
    if (showPaletteDialog) {
        Dialog(onDismissRequest = { showPaletteDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Question Palette",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.heightIn(max = 240.dp)
                    ) {
                        itemsIndexed(activeState.questions) { idx, q ->
                            val isAnswered = activeState.selectedAnswers.containsKey(q.id)
                            val isRev = activeState.markedForReview.contains(q.id)
                            val isCurrent = idx == activeState.currentQuestionIndex

                            val boxColor = when {
                                isCurrent -> MaterialTheme.colorScheme.primary
                                isRev -> MaterialTheme.colorScheme.tertiary
                                isAnswered -> Color(0xFF10B981)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(boxColor)
                                    .clickable {
                                        viewModel.setTestQuestionIndex(idx)
                                        showPaletteDialog = false
                                    }
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isAnswered || isCurrent || isRev) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(onClick = { showPaletteDialog = false }, modifier = Modifier.fillMaxWidth()) {
                        Text("Close Palette")
                    }
                }
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitConfirmation) {
        AlertDialog(
            onDismissRequest = { showSubmitConfirmation = false },
            title = { Text("Submit Mock Assessment?") },
            text = {
                Text("You have answered ${activeState.selectedAnswers.size} out of ${activeState.questions.size} questions. Do you want to finalize and calculate your score?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmation = false
                        viewModel.submitActiveTest()
                    }
                ) {
                    Text("Confirm Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirmation = false }) {
                    Text("Continue Test")
                }
            }
        )
    }
}

@Composable
fun TestResultScreen(
    uiState: AppUiState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val result = uiState.latestTestResult
    if (result == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { viewModel.navigateTo(Screen.STUDENT_DASHBOARD) }) {
                Text("Return to Dashboard")
            }
        }
        return
    }

    val activeTest = uiState.activeTestState?.questions ?: emptyList()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Assessment Result",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = result.testTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                if (result.scorePercentage >= 70) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
                            )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${result.scorePercentage}%",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                                color = Color.White
                            )
                            Text(
                                text = "SCORE",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ScoreColumn("Total Qs", "${result.totalQuestions}")
                        ScoreColumn("Correct", "${result.correct}")
                        ScoreColumn("Incorrect", "${result.incorrect}")
                        ScoreColumn("Accuracy", "${result.accuracy}%")
                    }
                }
            }
        }

        // Detailed Question Review Section
        item {
            Text(
                text = "Detailed Answer Review & Solutions",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        items(activeTest) { question ->
            val userSelected = uiState.activeTestState?.selectedAnswers?.get(question.id)
            val isCorrect = userSelected == question.correctAnswerIndex

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
                        Text(question.topic, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isCorrect) Color(0xFF10B981).copy(alpha = 0.2f) else MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = if (isCorrect) "Correct" else "Incorrect",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isCorrect) Color(0xFF10B981) else MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(question.question, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))

                    Spacer(modifier = Modifier.height(8.dp))
                    question.options.forEachIndexed { optIdx, optText ->
                        val isCorrectOption = optIdx == question.correctAnswerIndex
                        val isUserChoice = userSelected == optIdx

                        val optColor = when {
                            isCorrectOption -> Color(0xFF10B981).copy(alpha = 0.2f)
                            isUserChoice && !isCorrectOption -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                            else -> Color.Transparent
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = optColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            Row(modifier = Modifier.padding(6.dp)) {
                                Text("${('A' + optIdx)}. ", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                Text(optText, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Explanation:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.secondary)
                            Text(question.explanation, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.navigateTo(Screen.STUDENT_DASHBOARD) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Return to Dashboard")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
