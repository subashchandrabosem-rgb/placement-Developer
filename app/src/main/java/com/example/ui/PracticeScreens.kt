package com.example.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.state.AppUiState
import com.example.state.AppViewModel
import com.example.state.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeHubScreen(
    initialCategory: QuestionCategory,
    uiState: AppUiState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var selectedTopic by remember { mutableStateOf("All Topics") }
    var isTimedMode by remember { mutableStateOf(false) }

    // Filter questions
    val categoryQuestions = remember(selectedCategory, selectedTopic, uiState.questions) {
        uiState.questions.filter {
            it.category == selectedCategory &&
            (selectedTopic == "All Topics" || it.topic.equals(selectedTopic, ignoreCase = true))
        }
    }

    var currentQuestionIdx by remember(selectedCategory, selectedTopic) { mutableIntStateOf(0) }
    var selectedOption by remember(currentQuestionIdx, selectedCategory, selectedTopic) { mutableStateOf<Int?>(null) }
    var showExplanation by remember(currentQuestionIdx, selectedCategory, selectedTopic) { mutableStateOf(false) }

    // Soft skill state
    val softSkills = SampleData.softSkillLessons
    var activeSoftSkillIndex by remember { mutableIntStateOf(0) }
    var selectedSoftQuizOption by remember(activeSoftSkillIndex) { mutableStateOf<Int?>(null) }
    var showSoftQuizExplanation by remember(activeSoftSkillIndex) { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Category Switcher tabs
            PrimaryTabRow(
                selectedTabIndex = when (selectedCategory) {
                    QuestionCategory.APTITUDE -> 0
                    QuestionCategory.REASONING -> 1
                    QuestionCategory.VERBAL -> 2
                    QuestionCategory.SOFT_SKILLS -> 3
                    else -> 0
                },
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedCategory == QuestionCategory.APTITUDE,
                    onClick = { selectedCategory = QuestionCategory.APTITUDE; selectedTopic = "All Topics" },
                    text = { Text("Aptitude") }
                )
                Tab(
                    selected = selectedCategory == QuestionCategory.REASONING,
                    onClick = { selectedCategory = QuestionCategory.REASONING; selectedTopic = "All Topics" },
                    text = { Text("Reasoning") }
                )
                Tab(
                    selected = selectedCategory == QuestionCategory.VERBAL,
                    onClick = { selectedCategory = QuestionCategory.VERBAL; selectedTopic = "All Topics" },
                    text = { Text("Verbal") }
                )
                Tab(
                    selected = selectedCategory == QuestionCategory.SOFT_SKILLS,
                    onClick = { selectedCategory = QuestionCategory.SOFT_SKILLS },
                    text = { Text("Soft Skills") }
                )
            }
        }

        if (selectedCategory != QuestionCategory.SOFT_SKILLS) {
            // Topic Filter Chips
            item {
                val topics = when (selectedCategory) {
                    QuestionCategory.APTITUDE -> listOf(
                        "All Topics", "Percentages", "Time and Work", "Probability", "Profit and Loss", "Time, Speed and Distance", "Number System", "Simple Interest"
                    )
                    QuestionCategory.REASONING -> listOf(
                        "All Topics", "Blood Relations", "Coding-Decoding", "Number Series", "Syllogisms", "Direction Sense", "Puzzles"
                    )
                    QuestionCategory.VERBAL -> listOf(
                        "All Topics", "Sentence Correction", "Synonyms", "Reading Comprehension", "Grammar", "Error Detection"
                    )
                    else -> emptyList()
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(topics) { top ->
                        FilterChip(
                            selected = selectedTopic == top,
                            onClick = { selectedTopic = top },
                            label = { Text(top, style = MaterialTheme.typography.labelMedium) }
                        )
                    }
                }
            }

            // Mode & Stat bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = isTimedMode,
                            onCheckedChange = { isTimedMode = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isTimedMode) "Timed Mode (60s)" else "Practice Mode",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Text(
                        text = "${categoryQuestions.size} Questions Available",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Question Card
            val activeQuestion = categoryQuestions.getOrNull(currentQuestionIdx) ?: categoryQuestions.firstOrNull()

            if (activeQuestion != null) {
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
                                    text = "Question ${currentQuestionIdx + 1} of ${categoryQuestions.size}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                ) {
                                    Text(
                                        text = "${activeQuestion.topic} • ${activeQuestion.difficulty.label}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = activeQuestion.question,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            activeQuestion.options.forEachIndexed { optIdx, optText ->
                                val isSelected = selectedOption == optIdx
                                val isCorrect = optIdx == activeQuestion.correctAnswerIndex

                                val cardColor = when {
                                    !showExplanation && isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                    showExplanation && isCorrect -> Color(0xFF10B981).copy(alpha = 0.2f)
                                    showExplanation && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = cardColor,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            if (!showExplanation) selectedOption = optIdx
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${('A' + optIdx)}. ",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = optText,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (showExplanation) {
                                            if (isCorrect) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF10B981))
                                            } else if (isSelected) {
                                                Icon(Icons.Default.Cancel, contentDescription = "Incorrect", tint = MaterialTheme.colorScheme.error)
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            if (!showExplanation) {
                                Button(
                                    onClick = { showExplanation = true },
                                    enabled = selectedOption != null,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Verify Answer")
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Step-by-step Solution:",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = activeQuestion.explanation,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.sendMentorMessage("Help me solve this aptitude question step-by-step with quick tricks:\n${activeQuestion.question}")
                                            viewModel.navigateTo(Screen.AI_MENTOR)
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Ask AI Shortcut")
                                    }

                                    Button(
                                        onClick = {
                                            selectedOption = null
                                            showExplanation = false
                                            currentQuestionIdx = (currentQuestionIdx + 1) % categoryQuestions.size
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Next Question")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Text("No questions currently match the selected topic filter.")
                }
            }
        } else {
            // Soft Skills Module (10 Lessons with Concepts, Examples, Activity & Quick Quiz)
            val currentLesson = softSkills.getOrNull(activeSoftSkillIndex) ?: softSkills.first()

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(softSkills.size) { idx ->
                        InputChip(
                            selected = activeSoftSkillIndex == idx,
                            onClick = {
                                activeSoftSkillIndex = idx
                                selectedSoftQuizOption = null
                                showSoftQuizExplanation = false
                            },
                            label = { Text(softSkills[idx].title) }
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = currentLesson.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Concept Section
                        Text("1. Core Concept", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                        Text(currentLesson.concept, style = MaterialTheme.typography.bodySmall)

                        Spacer(modifier = Modifier.height(12.dp))

                        // Workplace Example
                        Text("2. Workplace / Campus Placement Example", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.secondary)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(currentLesson.workplaceExample, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(10.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Practical Activity
                        Text("3. Practical Actionable Drill", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.tertiary)
                        Text(currentLesson.practicalActivity, style = MaterialTheme.typography.bodySmall)

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Quiz
                        Text("Quick Assessment Check", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(currentLesson.quickQuizQuestion, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))

                        Spacer(modifier = Modifier.height(10.dp))
                        currentLesson.quizOptions.forEachIndexed { optIdx, optText ->
                            val isSelected = selectedSoftQuizOption == optIdx
                            val isCorrect = optIdx == currentLesson.correctQuizOptionIndex

                            val cardColor = when {
                                !showSoftQuizExplanation && isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                showSoftQuizExplanation && isCorrect -> Color(0xFF10B981).copy(alpha = 0.2f)
                                showSoftQuizExplanation && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = cardColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable {
                                        if (!showSoftQuizExplanation) selectedSoftQuizOption = optIdx
                                    }
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text("${('A' + optIdx)}. ", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                    Text(optText, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (!showSoftQuizExplanation) {
                            Button(
                                onClick = { showSoftQuizExplanation = true },
                                enabled = selectedSoftQuizOption != null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Check Answer")
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(currentLesson.quizExplanation, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(8.dp))
                            }
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
