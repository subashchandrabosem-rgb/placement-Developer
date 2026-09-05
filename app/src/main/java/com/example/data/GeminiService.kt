package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    val isApiKeyConfigured: Boolean
        get() = try {
            BuildConfig.GEMINI_API_KEY.isNotBlank() && !BuildConfig.GEMINI_API_KEY.startsWith("MY_")
        } catch (_: Throwable) {
            false
        }

    suspend fun generateContent(prompt: String, systemInstruction: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (_: Throwable) { "" }

        if (apiKey.isBlank() || apiKey.startsWith("MY_")) {
            return@withContext fallbackMentorResponse(prompt)
        }

        try {
            val rootJson = JSONObject()
            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            val partsArray = JSONArray()
            val partObj = JSONObject().apply {
                put("text", prompt)
            }
            partsArray.put(partObj)
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
            rootJson.put("contents", contentsArray)

            if (!systemInstruction.isNullOrBlank()) {
                val sysContent = JSONObject()
                val sysParts = JSONArray()
                sysParts.put(JSONObject().put("text", systemInstruction))
                sysContent.put("parts", sysParts)
                rootJson.put("systemInstruction", sysContent)
            }

            val requestBody = rootJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext fallbackMentorResponse(prompt)
            }

            val resJson = JSONObject(responseBody)
            val candidates = resJson.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text")
                    if (text.isNotBlank()) {
                        return@withContext text
                    }
                }
            }

            fallbackMentorResponse(prompt)
        } catch (e: Exception) {
            fallbackMentorResponse(prompt)
        }
    }

    suspend fun evaluateInterviewAnswer(
        question: String,
        answer: String,
        interviewType: InterviewType
    ): TurnEvaluation = withContext(Dispatchers.IO) {
        val prompt = """
You are a senior technical interviewer and campus placement evaluator.
Evaluate the student's answer for this interview question:
Question: "$question"
Student Answer: "$answer"
Interview Round: "${interviewType.title}"

Provide an objective critique with:
1. Relevance score (1 to 10)
2. Clarity score (1 to 10)
3. Technical correctness (1 to 10)
4. Communication score (1 to 10)
5. Feedback (2-3 concise sentences on strengths and areas to refine)
6. Sample Improved Answer (a professional, crisp response adhering to STAR or core technical principles)

Return your response in standard format:
RELEVANCE: 8
CLARITY: 7
TECHNICAL: 8
COMMUNICATION: 7
FEEDBACK: Your point on time complexity was accurate, but explain space complexity trade-offs explicitly.
IMPROVED: I would solve this using a two-pointer approach...
""".trimIndent()

        val response = generateContent(prompt, "You are an expert campus placement recruiter.")
        parseEvaluation(response, question, answer)
    }

    private fun parseEvaluation(raw: String, question: String, answer: String): TurnEvaluation {
        var rel = 8
        var cla = 7
        var tech = 8
        var comm = 7
        var feedback = "Good structured thought process. Make sure to articulate edge cases and state constraints before finalizing your solution."
        var improved = "A stronger response begins by framing the core requirement, outlining the time and space complexity upfront, and illustrating with a clear concrete test case."

        try {
            val lines = raw.lines()
            for (line in lines) {
                val trimmed = line.trim()
                when {
                    trimmed.startsWith("RELEVANCE:", ignoreCase = true) -> {
                        rel = trimmed.substringAfter(":").trim().toIntOrNull()?.coerceIn(1, 10) ?: 8
                    }
                    trimmed.startsWith("CLARITY:", ignoreCase = true) -> {
                        cla = trimmed.substringAfter(":").trim().toIntOrNull()?.coerceIn(1, 10) ?: 7
                    }
                    trimmed.startsWith("TECHNICAL:", ignoreCase = true) -> {
                        tech = trimmed.substringAfter(":").trim().toIntOrNull()?.coerceIn(1, 10) ?: 8
                    }
                    trimmed.startsWith("COMMUNICATION:", ignoreCase = true) -> {
                        comm = trimmed.substringAfter(":").trim().toIntOrNull()?.coerceIn(1, 10) ?: 7
                    }
                    trimmed.startsWith("FEEDBACK:", ignoreCase = true) -> {
                        feedback = trimmed.substringAfter(":").trim()
                    }
                    trimmed.startsWith("IMPROVED:", ignoreCase = true) -> {
                        improved = trimmed.substringAfter(":").trim()
                    }
                }
            }
        } catch (_: Exception) {}

        return TurnEvaluation(
            relevanceScore = rel,
            clarityScore = cla,
            technicalCorrectness = tech,
            communicationScore = comm,
            feedback = feedback,
            sampleImprovedAnswer = improved
        )
    }

    private fun fallbackMentorResponse(query: String): String {
        val lower = query.lowercase()
        return when {
            lower.contains("loop") || lower.contains("python") -> """
**Python Loops Guide for Placements**

1. **For Loop (Iterative)**:
   ```python
   # Iterating over sequence with enumerate
   fruits = ["apple", "banana", "cherry"]
   for index, fruit in enumerate(fruits):
       print(f"{index}: {fruit}")
   ```
2. **While Loop (Conditional)**:
   ```python
   # Two-pointer convergence pattern
   left, right = 0, len(nums) - 1
   while left < right:
       current_sum = nums[left] + nums[right]
       if current_sum == target:
           return [left, right]
       elif current_sum < target:
           left += 1
       else:
           right -= 1
   ```
3. **Key Placement Tip**: Be prepared to analyze worst-case time complexity O(N) and remember that list comprehensions in Python run in optimized C bytecode!
""".trimIndent()

            lower.contains("java") -> """
**Top Java Placement Interview Questions:**

1. **Difference between `==` and `.equals()`**:
   - `==` checks reference/memory location equality.
   - `.equals()` checks logical string or object content equality (overridden in String, Integer, etc.).
2. **String, StringBuilder, and StringBuffer**:
   - `String`: Immutable; pooled in String Constant Pool.
   - `StringBuilder`: Mutable and fast; not thread-safe.
   - `StringBuffer`: Mutable and thread-safe (synchronized methods).
3. **OOP 4 Pillars in 1 Sentence Each**:
   - *Encapsulation*: Data hiding using private fields and getters/setters.
   - *Inheritance*: Code reusability via the `extends` keyword.
   - *Polymorphism*: Method overloading (compile-time) and overriding (runtime).
   - *Abstraction*: Hiding complex implementation details via `interface` and `abstract class`.
""".trimIndent()

            lower.contains("tcs") || lower.contains("placement") || lower.contains("prepare") -> """
**Placement Strategy for Campus Recruitment (TCS / Infosys / Cognizant / Product Firms):**

1. **Round 1 - Cognitive & Foundation (40% weight)**:
   - Practice 20 Quantitative Aptitude questions daily (focus on Percentages, Profit & Loss, Work-Time).
   - Speed is vital: aim for under 75 seconds per aptitude problem.
2. **Round 2 - Coding & CS Fundamentals (35% weight)**:
   - Master Array manipulations, HashMaps, String parsing, and 2-Pointer patterns.
   - Revise DBMS ACID properties, Normalization (1NF to 3NF), and OS Process scheduling.
3. **Round 3 - Technical & HR Interview (25% weight)**:
   - Prepare your Final Year Project using the **STAR methodology** (Situation, Task, Action, Result).
   - Practice explaining your trade-offs clearly without rushing.
""".trimIndent()

            lower.contains("probability") || lower.contains("aptitude") -> """
**Probability Master Formula & Quick Heuristics:**

- **Basic Definition**: P(E) = Number of Favorable Outcomes / Total Possible Outcomes
- **Addition Rule**: P(A or B) = P(A) + P(B) - P(A and B)
- **Independent Events**: P(A and B) = P(A) * P(B)
- **Complement Rule**: P(E') = 1 - P(E) (Often faster when asked "at least one" problems!)

*Example*: Probability of rolling at least one 6 in two dice rolls = 1 - (5/6 * 5/6) = 1 - 25/36 = 11/36.
""".trimIndent()

            lower.contains("hr") || lower.contains("interview") -> """
**Mock HR Question & High-Impact Strategy:**

*Question*: "Why should we hire you over other candidates with higher GPAs?"
*Winning Framework*:
1. **Acknowledge & Validate**: "Academic performance is important, and I respect strong coursework."
2. **Differentiate with Hands-on Execution**: "My strength lies in taking abstract CS concepts and deploying functional software with clean architecture and proactive debugging."
3. **Demonstrate Adaptability**: "During my capstone project, I learned full-stack architecture independently in three weeks to deliver ahead of schedule. I bring that same self-starting ownership to your engineering team."
""".trimIndent()

            else -> """
I am here to guide your placement journey! Here is how to make the most of PlacementPro AI:

1. **Topic Clarification**: Ask me about Data Structures, Algorithms, SQL, Java/Python, or Aptitude formulas.
2. **Interview Coaching**: Request mock behavioral or technical questions tailored to your dream company.
3. **Problem Explanations**: Paste problem descriptions or logic bottlenecks for step-by-step breakdown.

What specific concept or company round would you like to prepare for right now?
""".trimIndent()
        }
    }
}
