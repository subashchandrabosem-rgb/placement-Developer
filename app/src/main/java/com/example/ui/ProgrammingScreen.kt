package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.state.AppUiState
import com.example.state.AppViewModel
import com.example.state.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgrammingScreen(
    uiState: AppUiState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    var selectedLanguage by remember { mutableStateOf("Java") }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.BEGINNER) }
    // 0: Coding Challenge, 1: MCQ & Output, 2: Learn Concepts
    var selectedModeTab by remember { mutableIntStateOf(0) }

    // Active challenge
    var activeChallengeIndex by remember { mutableIntStateOf(0) }
    val challenges = uiState.codingChallenges
    val currentChallenge = challenges.getOrNull(activeChallengeIndex) ?: challenges.first()

    var userCode by remember(currentChallenge.id) { mutableStateOf(currentChallenge.starterCode) }
    var consoleOutput by remember { mutableStateOf<String?>(null) }
    var isRunningCode by remember { mutableStateOf(false) }

    // MCQ state
    val mcqQuestions = remember(uiState.questions) {
        uiState.questions.filter { it.category == QuestionCategory.PROGRAMMING }
    }
    var mcqIndex by remember { mutableIntStateOf(0) }
    var selectedMcqOption by remember { mutableStateOf<Int?>(null) }
    var showMcqExplanation by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
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
                        text = "Programming & DSA",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Master coding rounds for TCS, Amazon, Infosys & product firms",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "${uiState.studentProfile.problemsSolved} Solved",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Language Selector
        item {
            val languages = listOf("Java", "Python", "C++", "C", "JavaScript")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(languages) { lang ->
                    FilterChip(
                        selected = selectedLanguage == lang,
                        onClick = { selectedLanguage = lang },
                        label = { Text(lang) },
                        leadingIcon = {
                            if (selectedLanguage == lang) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    )
                }
            }
        }

        // Mode Tabs: Coding Challenge, MCQ / Output, Learn
        item {
            PrimaryTabRow(
                selectedTabIndex = selectedModeTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedModeTab == 0,
                    onClick = { selectedModeTab = 0 },
                    text = { Text("Code Sandbox", style = MaterialTheme.typography.labelLarge) },
                    icon = { Icon(Icons.Default.Terminal, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                Tab(
                    selected = selectedModeTab == 1,
                    onClick = { selectedModeTab = 1 },
                    text = { Text("MCQ & Output", style = MaterialTheme.typography.labelLarge) },
                    icon = { Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                Tab(
                    selected = selectedModeTab == 2,
                    onClick = { selectedModeTab = 2 },
                    text = { Text("Learn Concepts", style = MaterialTheme.typography.labelLarge) },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
            }
        }

        when (selectedModeTab) {
            0 -> {
                // Code Challenge Mode
                item {
                    // Challenge switcher chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(challenges.size) { idx ->
                            val ch = challenges[idx]
                            InputChip(
                                selected = activeChallengeIndex == idx,
                                onClick = {
                                    activeChallengeIndex = idx
                                    consoleOutput = null
                                },
                                label = { Text(ch.title) }
                            )
                        }
                    }
                }

                item {
                    // Problem Statement Card
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
                                Text(
                                    text = currentChallenge.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = currentChallenge.difficulty.label,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentChallenge.problemStatement,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Example Input: ${currentChallenge.exampleInput.replace("\n", " | ")}",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Example Output: ${currentChallenge.exampleOutput}",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                item {
                    // Interactive Code Editor Box
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        border = CardDefaults.outlinedCardBorder(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Code, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Code Editor (${currentChallenge.language})", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                                }
                                TextButton(
                                    onClick = { userCode = currentChallenge.starterCode }
                                ) {
                                    Text("Reset Code", style = MaterialTheme.typography.labelSmall)
                                }
                            }

                            OutlinedTextField(
                                value = userCode,
                                onValueChange = { userCode = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 180.dp, max = 280.dp)
                                    .testTag("code_editor_field"),
                                textStyle = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        isRunningCode = true
                                        val result = viewModel.runCodeChallenge(currentChallenge.id)
                                        consoleOutput = result.second
                                        isRunningCode = false
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("run_test_cases_button")
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Run & Test Cases")
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.sendMentorMessage("Please review my ${currentChallenge.language} solution for '${currentChallenge.title}':\n```\n$userCode\n```")
                                        viewModel.navigateTo(Screen.AI_MENTOR)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Ask AI Review")
                                }
                            }

                            if (consoleOutput != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF0F172A),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "Execution Console",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Color(0xFF38BDF8)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = consoleOutput!!,
                                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                            color = Color(0xFF4ADE80)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // MCQ & Output Prediction Mode
                val currentQ = mcqQuestions.getOrNull(mcqIndex) ?: mcqQuestions.firstOrNull()

                if (currentQ != null) {
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
                                        text = "Question ${mcqIndex + 1} of ${mcqQuestions.size}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = currentQ.topic,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = currentQ.question,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )

                                if (currentQ.codeSnippet != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = currentQ.codeSnippet,
                                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                currentQ.options.forEachIndexed { optIndex, optionText ->
                                    val isSelected = selectedMcqOption == optIndex
                                    val isCorrect = optIndex == currentQ.correctAnswerIndex
                                    val cardColor = when {
                                        !showMcqExplanation && isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                        showMcqExplanation && isCorrect -> Color(0xFF10B981).copy(alpha = 0.2f)
                                        showMcqExplanation && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = cardColor,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable {
                                                if (!showMcqExplanation) {
                                                    selectedMcqOption = optIndex
                                                }
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${('A' + optIndex)}. ",
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = optionText,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.weight(1f)
                                            )
                                            if (showMcqExplanation) {
                                                if (isCorrect) {
                                                    Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF10B981))
                                                } else if (isSelected) {
                                                    Icon(Icons.Default.Cancel, contentDescription = "Incorrect", tint = MaterialTheme.colorScheme.error)
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                if (!showMcqExplanation) {
                                    Button(
                                        onClick = { showMcqExplanation = true },
                                        enabled = selectedMcqOption != null,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Check Answer")
                                    }
                                } else {
                                    // Detailed Explanation Card
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = "Detailed Explanation:",
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = currentQ.explanation,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = {
                                            selectedMcqOption = null
                                            showMcqExplanation = false
                                            mcqIndex = (mcqIndex + 1) % mcqQuestions.size
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Next Practice Question")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Learn Concepts Mode
                val topics = listOf(
                    "Time & Space Complexity (Big-O)" to "O(1) < O(log N) < O(N) < O(N log N) < O(N^2) < O(2^N). Know when hash tables degrade to O(N) and master Master Theorem.",
                    "Two-Pointer & Sliding Window" to "Ideal for subarray sums, palindromes, and finding duplicates in sorted sequences with O(1) auxiliary space.",
                    "Trees & BST Traversals" to "Inorder traversal of a BST gives elements in sorted order. Master BFS (using Queue) and DFS (using Recursion/Stack).",
                    "Dynamic Programming Patterns" to "Identify optimal substructure and overlapping subproblems. Recognize 0/1 Knapsack, Longest Common Subsequence, and Fibonacci recurrence."
                )

                items(topics.size) { idx ->
                    val (topicTitle, topicSummary) = topics[idx]
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Bookmark, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(topicTitle, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(topicSummary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ask AI Mentor for deep dive >",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    viewModel.sendMentorMessage("Explain $topicTitle with clean code examples for placement interviews.")
                                    viewModel.navigateTo(Screen.AI_MENTOR)
                                }
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
