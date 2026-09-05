package com.example.data

enum class UserRole {
    STUDENT,
    ADMIN
}

data class StudentProfile(
    val uid: String = "user_student_101",
    val name: String = "Rahul Sharma",
    val email: String = "rahul.sharma@engg.edu",
    val college: String = "National Institute of Technology",
    val department: String = "Computer Science & Engineering",
    val year: String = "Final Year (4th Year)",
    val phone: String = "+91 98765 43210",
    val targetRole: String = "Software Development Engineer (SDE)",
    val role: UserRole = UserRole.STUDENT,
    val createdAt: Long = System.currentTimeMillis() - 86400000L * 15,
    val profileCompleted: Boolean = true,
    val currentStreak: Int = 5,
    val dailyGoal: String = "Complete 20 Aptitude & 2 Coding Problems",
    val testsCompleted: Int = 8,
    val problemsSolved: Int = 34,
    val interviewsCompleted: Int = 3
)

data class ReadinessScoreBreakdown(
    val programmingScore: Int = 78,
    val aptitudeScore: Int = 82,
    val reasoningScore: Int = 74,
    val verbalScore: Int = 70,
    val technicalScore: Int = 80,
    val interviewScore: Int = 75
) {
    // Transparent weighted formula:
    // Programming: 20%, Aptitude: 20%, Reasoning: 15%, Verbal: 10%, Technical: 15%, Interview: 20%
    val overallReadinessScore: Int
        get() = (
            (programmingScore * 0.20) +
            (aptitudeScore * 0.20) +
            (reasoningScore * 0.15) +
            (verbalScore * 0.10) +
            (technicalScore * 0.15) +
            (interviewScore * 0.20)
        ).toInt()
}

enum class QuestionCategory(val displayName: String) {
    PROGRAMMING("Programming"),
    APTITUDE("Aptitude"),
    REASONING("Reasoning"),
    VERBAL("Verbal Ability"),
    TECHNICAL("Technical Concepts"),
    SOFT_SKILLS("Soft Skills")
}

enum class Difficulty(val label: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced")
}

data class Question(
    val id: String,
    val category: QuestionCategory,
    val topic: String,
    val difficulty: Difficulty,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int, // 0 for A, 1 for B, 2 for C, 3 for D
    val explanation: String,
    val tags: List<String> = emptyList(),
    val isPublished: Boolean = true,
    val codeSnippet: String? = null
)

data class TestCase(
    val input: String,
    val expectedOutput: String,
    val isHidden: Boolean = false
)

data class CodingChallenge(
    val id: String,
    val title: String,
    val language: String,
    val difficulty: Difficulty,
    val topic: String,
    val problemStatement: String,
    val inputFormat: String,
    val outputFormat: String,
    val constraints: String,
    val exampleInput: String,
    val exampleOutput: String,
    val starterCode: String,
    val testCases: List<TestCase>
)

data class SoftSkillLesson(
    val id: String,
    val title: String,
    val concept: String,
    val workplaceExample: String,
    val practicalActivity: String,
    val quickQuizQuestion: String,
    val quizOptions: List<String>,
    val correctQuizOptionIndex: Int,
    val quizExplanation: String
)

data class MockTest(
    val id: String,
    val title: String,
    val category: QuestionCategory,
    val durationMinutes: Int,
    val totalQuestions: Int,
    val description: String,
    val questionIds: List<String>,
    val isPublished: Boolean = true
)

data class TestResult(
    val id: String,
    val testId: String,
    val testTitle: String,
    val totalQuestions: Int,
    val attempted: Int,
    val correct: Int,
    val incorrect: Int,
    val scorePercentage: Int,
    val accuracy: Int,
    val timeUsedSeconds: Int,
    val topicPerformance: Map<String, Int>,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val tag: String? = null
)

enum class InterviewType(val title: String, val description: String) {
    HR("HR Interview", "Culture fit, background, strengths, aspirations, and situational response"),
    TECHNICAL("Technical Interview", "CS core fundamentals, DBMS, OS, Computer Networks & System Design"),
    PROGRAMMING("Programming Interview", "Data structures, algorithms, complexity analysis & problem solving"),
    BEHAVIORAL("Behavioral Interview", "Conflict resolution, teamwork, STAR methodology leadership stories"),
    MIXED("Mixed Placement Interview", "Full comprehensive campus round combining technical & behavioral depth")
}

data class InterviewTurn(
    val questionNumber: Int,
    val question: String,
    var studentAnswer: String = "",
    var evaluation: TurnEvaluation? = null
)

data class TurnEvaluation(
    val relevanceScore: Int, // 1-10
    val clarityScore: Int, // 1-10
    val technicalCorrectness: Int, // 1-10
    val communicationScore: Int, // 1-10
    val feedback: String,
    val sampleImprovedAnswer: String
)

data class FinalInterviewReport(
    val interviewType: InterviewType,
    val difficulty: Difficulty,
    val overallScore: Int, // 0-100
    val relevanceAvg: Int,
    val clarityAvg: Int,
    val technicalAvg: Int,
    val communicationAvg: Int,
    val strongAreas: List<String>,
    val weakAreas: List<String>,
    val preparationRecommendations: List<String>,
    val sampleAnswersSummary: List<String>,
    val date: Long = System.currentTimeMillis()
)

data class StudyTask(
    val id: String,
    val dayNumber: Int,
    val title: String,
    val description: String,
    val category: QuestionCategory,
    val isCompleted: Boolean = false
)

data class StudyPlan(
    val targetRole: String,
    val skillLevel: Difficulty,
    val dailyHours: Int,
    val targetDateDays: Int,
    val weakSubjects: List<String>,
    val tasks: List<StudyTask>
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean,
    val currentProgress: Int,
    val maxProgress: Int,
    val unlockedDate: String? = null
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: String, // "TEST", "REMINDER", "ACHIEVEMENT", "RECOMMENDATION"
    val timestamp: Long = System.currentTimeMillis(),
    var isRead: Boolean = false
)
