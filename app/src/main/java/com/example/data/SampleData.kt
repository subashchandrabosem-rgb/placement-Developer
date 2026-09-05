package com.example.data

object SampleData {

    val initialQuestions: List<Question> = listOf(
        // Aptitude Questions
        Question(
            id = "apt_01",
            category = QuestionCategory.APTITUDE,
            topic = "Percentages",
            difficulty = Difficulty.BEGINNER,
            question = "If the price of petrol increases by 25%, by how much percentage must a motorist reduce his consumption so that expenditure remains unchanged?",
            options = listOf("20%", "25%", "16.66%", "30%"),
            correctAnswerIndex = 0,
            explanation = "Formula: Reduction % = [r / (100 + r)] * 100 = [25 / 125] * 100 = 20%. Hence, consumption must be reduced by 20%.",
            tags = listOf("Percentage", "Expenditure", "TCS", "Infosys")
        ),
        Question(
            id = "apt_02",
            category = QuestionCategory.APTITUDE,
            topic = "Time and Work",
            difficulty = Difficulty.INTERMEDIATE,
            question = "A can do a piece of work in 12 days and B can do it in 16 days. They worked together for 4 days, then A left. How many more days will B take to complete the remaining work?",
            options = listOf("6 days", "6.66 days", "8 days", "5 days"),
            correctAnswerIndex = 1,
            explanation = "A's 1-day work = 1/12, B's 1-day work = 1/16. Together in 1 day = (4+3)/48 = 7/48. In 4 days, work done = 4 * 7/48 = 7/12. Remaining work = 1 - 7/12 = 5/12. Time taken by B = (5/12) / (1/16) = (5/12) * 16 = 20/3 = 6.66 days.",
            tags = listOf("Work", "Efficiency", "Wipro", "Cognizant")
        ),
        Question(
            id = "apt_03",
            category = QuestionCategory.APTITUDE,
            topic = "Probability",
            difficulty = Difficulty.INTERMEDIATE,
            question = "Two dice are rolled simultaneously. What is the probability that the sum of the numbers appearing on both dice is a prime number?",
            options = listOf("5/12", "7/18", "1/2", "13/36"),
            correctAnswerIndex = 0,
            explanation = "Possible prime sums: 2, 3, 5, 7, 11. Sum 2: (1,1)[1]; Sum 3: (1,2),(2,1)[2]; Sum 5: (1,4),(4,1),(2,3),(3,2)[4]; Sum 7: (1,6),(6,1),(2,5),(5,2),(3,4),(4,3)[6]; Sum 11: (5,6),(6,5)[2]. Total favorable outcomes = 1+2+4+6+2 = 15. Total outcomes = 36. Probability = 15/36 = 5/12.",
            tags = listOf("Probability", "Dice", "Accenture")
        ),
        Question(
            id = "apt_04",
            category = QuestionCategory.APTITUDE,
            topic = "Profit and Loss",
            difficulty = Difficulty.BEGINNER,
            question = "A shopkeeper sells an article for ₹840 gaining 20% on cost price. What was the cost price of the article?",
            options = listOf("₹650", "₹700", "₹720", "₹750"),
            correctAnswerIndex = 1,
            explanation = "Selling Price = 1.20 * Cost Price. CP = 840 / 1.2 = ₹700.",
            tags = listOf("Profit", "Loss", "Math")
        ),
        Question(
            id = "apt_05",
            category = QuestionCategory.APTITUDE,
            topic = "Time, Speed and Distance",
            difficulty = Difficulty.ADVANCED,
            question = "A train 150 meters long passes a telegraph post in 12 seconds and crosses another train of equal length traveling in opposite direction in 10 seconds. Find the speed of the second train.",
            options = listOf("54 km/h", "63 km/h", "72 km/h", "81 km/h"),
            correctAnswerIndex = 1,
            explanation = "Speed of first train = 150/12 = 12.5 m/s. When crossing second train, relative speed = (150+150)/10 = 30 m/s. Since traveling opposite, S1 + S2 = 30 => S2 = 30 - 12.5 = 17.5 m/s = 17.5 * (18/5) = 63 km/h.",
            tags = listOf("Trains", "Relative Speed", "Amazon")
        ),
        Question(
            id = "apt_06",
            category = QuestionCategory.APTITUDE,
            topic = "Number System",
            difficulty = Difficulty.BEGINNER,
            question = "What is the remainder when 7^84 is divided by 342?",
            options = listOf("1", "7", "49", "341"),
            correctAnswerIndex = 0,
            explanation = "7^3 = 343 = 342 + 1. Therefore, 7^84 = (7^3)^28 = (342 + 1)^28. By Binomial expansion, remainder = 1^28 = 1.",
            tags = listOf("Remainders", "Number Theory")
        ),
        Question(
            id = "apt_07",
            category = QuestionCategory.APTITUDE,
            topic = "Simple Interest",
            difficulty = Difficulty.BEGINNER,
            question = "A sum of money triples itself in 8 years at simple interest. In how many years will it become 7 times itself at the same rate?",
            options = listOf("16 years", "20 years", "24 years", "28 years"),
            correctAnswerIndex = 2,
            explanation = "Money triples: Interest = 2P in 8 years. Rate r = (2P * 100) / (P * 8) = 25%. To become 7 times: Interest = 6P. Time = (6P * 100) / (P * 25) = 24 years.",
            tags = listOf("Interest", "Banking")
        ),

        // Reasoning Questions
        Question(
            id = "reas_01",
            category = QuestionCategory.REASONING,
            topic = "Blood Relations",
            difficulty = Difficulty.BEGINNER,
            question = "Pointing to a photograph, Rohit said, 'She is the only daughter of my grandfather's only son.' How is the person in the photograph related to Rohit?",
            options = listOf("Mother", "Sister", "Cousin", "Aunt"),
            correctAnswerIndex = 1,
            explanation = "Rohit's grandfather's only son is Rohit's father. The only daughter of Rohit's father is Rohit's sister.",
            tags = listOf("Blood Relations", "Logical", "Capgemini")
        ),
        Question(
            id = "reas_02",
            category = QuestionCategory.REASONING,
            topic = "Coding-Decoding",
            difficulty = Difficulty.INTERMEDIATE,
            question = "In a certain code, 'ENGINEER' is written as 'GPILGTT'. How is 'STUDENT' written in that same code?",
            options = listOf("UVWFGPV", "UWVFGPU", "UWVFGPV", "TVWFHPU"),
            correctAnswerIndex = 2,
            explanation = "Shift pattern: E(+2)=G, N(+2)=P, G(+2)=I, I(+3)=L... Pattern is +2 across standard letters: S(+2)=U, T(+3 wait), S(+2)->U, T(+3)->W, U(+1)->V, D(+2)->F, E(+2)->G, N(+2)->P, T(+2)->V. Hence UWVFGPV.",
            tags = listOf("Coding", "Series", "TCS NQT")
        ),
        Question(
            id = "reas_03",
            category = QuestionCategory.REASONING,
            topic = "Number Series",
            difficulty = Difficulty.INTERMEDIATE,
            question = "Find the missing number in the series: 3, 10, 29, 66, 127, ?",
            options = listOf("198", "218", "216", "222"),
            correctAnswerIndex = 1,
            explanation = "The pattern is n^3 + 2. For n=1: 1+2=3; n=2: 8+2=10; n=3: 27+2=29; n=4: 64+2=66; n=5: 125+2=127; n=6: 216+2=218.",
            tags = listOf("Series", "Math Reasoning")
        ),
        Question(
            id = "reas_04",
            category = QuestionCategory.REASONING,
            topic = "Syllogisms",
            difficulty = Difficulty.INTERMEDIATE,
            question = "Statements: Some keys are locks. All locks are doors. Conclusions: I. Some keys are doors. II. All doors are locks.",
            options = listOf("Only conclusion I follows", "Only conclusion II follows", "Both follow", "Neither follows"),
            correctAnswerIndex = 0,
            explanation = "Keys overlap with locks, and all locks are inside doors. Hence the keys that are locks are definitely doors (I follows). But doors not containing locks may exist, so II doesn't follow.",
            tags = listOf("Logic", "Syllogism")
        ),
        Question(
            id = "reas_05",
            category = QuestionCategory.REASONING,
            topic = "Direction Sense",
            difficulty = Difficulty.BEGINNER,
            question = "A man walks 6 km South, turns left and walks 4 km, then turns left again and walks 6 km. In which direction is he from his starting point and how far?",
            options = listOf("East, 4 km", "West, 4 km", "North, 2 km", "South, 6 km"),
            correctAnswerIndex = 0,
            explanation = "South 6 km, then left (East) 4 km, then left (North) 6 km brings him back to the horizontal level of the start, displaced 4 km to the East.",
            tags = listOf("Directions", "Puzzles")
        ),

        // Verbal Ability Questions
        Question(
            id = "verb_01",
            category = QuestionCategory.VERBAL,
            topic = "Sentence Correction",
            difficulty = Difficulty.INTERMEDIATE,
            question = "Identify the error: 'Neither the manager nor the team members (A) / was aware of the changes (B) / implemented by the board. (C) / No error (D)'",
            options = listOf("Part A", "Part B", "Part C", "No error (D)"),
            correctAnswerIndex = 1,
            explanation = "When subjects are connected by 'neither... nor', the verb agrees with the closer subject. 'Team members' is plural, so 'were aware' should be used instead of 'was aware'.",
            tags = listOf("Grammar", "Subject-Verb Agreement")
        ),
        Question(
            id = "verb_02",
            category = QuestionCategory.VERBAL,
            topic = "Synonyms",
            difficulty = Difficulty.INTERMEDIATE,
            question = "Select the most appropriate synonym for the word: 'METICULOUS'",
            options = listOf("Careless", "Thorough & Precise", "Hasty", "Indifferent"),
            correctAnswerIndex = 1,
            explanation = "Meticulous means showing great attention to detail; very careful and precise. Thorough & Precise is the correct synonym.",
            tags = listOf("Vocabulary", "Synonyms")
        ),
        Question(
            id = "verb_03",
            category = QuestionCategory.VERBAL,
            topic = "Reading Comprehension",
            difficulty = Difficulty.ADVANCED,
            question = "What is the primary implication of 'amortizing technical debt early in software engineering'?",
            options = listOf(
                "Postponing documentation until launch",
                "Fixing architectural bottlenecks early to avoid compound development friction",
                "Increasing cloud server RAM dynamically",
                "Hiring more contractors for testing"
            ),
            correctAnswerIndex = 1,
            explanation = "Amortizing technical debt refers to resolving shortcuts, modularity flaws, and code rot proactively before they slow future feature delivery.",
            tags = listOf("Technical Reading", "Comprehension")
        ),

        // Programming MCQ & Output Prediction
        Question(
            id = "prog_01",
            category = QuestionCategory.PROGRAMMING,
            topic = "OOP & Java",
            difficulty = Difficulty.INTERMEDIATE,
            question = "What will be the output of the following Java snippet?\n\nString s1 = \"Hello\";\nString s2 = new String(\"Hello\");\nSystem.out.println((s1 == s2) + \" \" + s1.equals(s2));",
            options = listOf("true true", "false true", "true false", "false false"),
            correctAnswerIndex = 1,
            explanation = "s1 points to a string literal in the String Constant Pool, while s2 points to a distinct heap object. Hence s1 == s2 checks reference equality (false), while s1.equals(s2) checks content value (true).",
            tags = listOf("Java", "Strings", "Memory", "Output Prediction"),
            codeSnippet = "String s1 = \"Hello\";\nString s2 = new String(\"Hello\");\nSystem.out.println((s1 == s2) + \" \" + s1.equals(s2));"
        ),
        Question(
            id = "prog_02",
            category = QuestionCategory.PROGRAMMING,
            topic = "Recursion & Trees",
            difficulty = Difficulty.ADVANCED,
            question = "What is the worst-case time complexity of searching an element in an unbalanced Binary Search Tree (BST) with N nodes?",
            options = listOf("O(log N)", "O(N log N)", "O(N)", "O(1)"),
            correctAnswerIndex = 2,
            explanation = "In an unbalanced BST (skewed like a linked list), searching requires traversing every single node from root to leaf, giving O(N) worst-case time complexity.",
            tags = listOf("DSA", "Trees", "Complexity")
        ),
        Question(
            id = "prog_03",
            category = QuestionCategory.PROGRAMMING,
            topic = "Python Internals",
            difficulty = Difficulty.BEGINNER,
            question = "What does the following Python expression evaluate to?\n\nprint([x*2 for x in range(5) if x % 2 == 0])",
            options = listOf("[0, 2, 4]", "[0, 4, 8]", "[2, 4, 6]", "[0, 1, 2, 3, 4]"),
            correctAnswerIndex = 1,
            explanation = "range(5) produces 0, 1, 2, 3, 4. Even numbers are 0, 2, 4. Multiplying each by 2 yields [0, 4, 8].",
            tags = listOf("Python", "List Comprehension")
        ),

        // Technical Concepts
        Question(
            id = "tech_01",
            category = QuestionCategory.TECHNICAL,
            topic = "Operating Systems",
            difficulty = Difficulty.INTERMEDIATE,
            question = "Which condition is NOT one of Coffman's four necessary conditions for deadlock?",
            options = listOf("Mutual Exclusion", "Hold and Wait", "Preemption allowed", "Circular Wait"),
            correctAnswerIndex = 2,
            explanation = "Coffman's four conditions are: 1. Mutual Exclusion, 2. Hold and Wait, 3. No Preemption (resources cannot be forcibly confiscated), 4. Circular Wait. Thus 'Preemption allowed' breaks deadlock.",
            tags = listOf("OS", "Deadlock", "CS Core")
        ),
        Question(
            id = "tech_02",
            category = QuestionCategory.TECHNICAL,
            topic = "DBMS",
            difficulty = Difficulty.INTERMEDIATE,
            question = "In relational databases, which normal form eliminates transitive dependencies?",
            options = listOf("1NF", "2NF", "3NF", "BCNF"),
            correctAnswerIndex = 2,
            explanation = "3NF requires the relation to be in 2NF and have no transitive dependencies (non-prime attributes must depend solely on candidate keys).",
            tags = listOf("DBMS", "Normalization", "SQL")
        )
    )

    val codingChallenges: List<CodingChallenge> = listOf(
        CodingChallenge(
            id = "code_01",
            title = "Two Sum Problem",
            language = "Java",
            difficulty = Difficulty.BEGINNER,
            topic = "Arrays & Hashing",
            problemStatement = "Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`.\nAssume each input has exactly one solution and you may not use the same element twice.",
            inputFormat = "Line 1: space-separated integers for nums. Line 2: integer target.",
            outputFormat = "Two space-separated 0-based indices.",
            constraints = "2 <= nums.length <= 10^4\n-10^9 <= nums[i] <= 10^9",
            exampleInput = "2 7 11 15\n9",
            exampleOutput = "0 1",
            starterCode = """class Solution {
    public int[] twoSum(int[] nums, int target) {
        // Use a HashMap for O(n) lookup
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        return new int[] {};
    }
}""",
            testCases = listOf(
                TestCase("2 7 11 15\n9", "0 1"),
                TestCase("3 2 4\n6", "1 2"),
                TestCase("3 3\n6", "0 1")
            )
        ),
        CodingChallenge(
            id = "code_02",
            title = "Valid Palindrome",
            language = "Python",
            difficulty = Difficulty.BEGINNER,
            topic = "Strings & Two Pointers",
            problemStatement = "Given a string s, determine if it is a palindrome considering only alphanumeric characters and ignoring cases.",
            inputFormat = "A single string on one line.",
            outputFormat = "'true' if palindrome, 'false' otherwise.",
            constraints = "1 <= s.length <= 2 * 10^5",
            exampleInput = "A man, a plan, a canal: Panama",
            exampleOutput = "true",
            starterCode = """def is_palindrome(s: str) -> bool:
    filtered = [c.lower() for c in s if c.isalnum()]
    return filtered == filtered[::-1]
""",
            testCases = listOf(
                TestCase("A man, a plan, a canal: Panama", "true"),
                TestCase("race a car", "false"),
                TestCase(" ", "true")
            )
        ),
        CodingChallenge(
            id = "code_03",
            title = "Reverse Linked List",
            language = "C++",
            difficulty = Difficulty.INTERMEDIATE,
            topic = "Linked Lists",
            problemStatement = "Given the head of a singly linked list, reverse the list, and return the reversed list values.",
            inputFormat = "Space separated node integers.",
            outputFormat = "Space separated reversed integers.",
            constraints = "0 <= nodes <= 5000",
            exampleInput = "1 2 3 4 5",
            exampleOutput = "5 4 3 2 1",
            starterCode = """ListNode* reverseList(ListNode* head) {
    ListNode* prev = nullptr;
    ListNode* curr = head;
    while (curr != nullptr) {
        ListNode* nextTemp = curr->next;
        curr->next = prev;
        prev = curr;
        curr = nextTemp;
    }
    return prev;
}""",
            testCases = listOf(
                TestCase("1 2 3 4 5", "5 4 3 2 1"),
                TestCase("1 2", "2 1")
            )
        )
    )

    val softSkillLessons: List<SoftSkillLesson> = listOf(
        SoftSkillLesson(
            id = "soft_01",
            title = "Communication & Active Listening",
            concept = "Effective engineering communication is crisp, empathetic, and audience-tailored. Active listening requires confirming assumptions before designing solutions.",
            workplaceExample = "In sprint grooming, instead of assuming requirements, say: 'To make sure we are aligned, you mean the payment gateway should timeout after 3 seconds?'",
            practicalActivity = "Practice answering 'Tell me about yourself' in under 90 seconds using the Present-Past-Future narrative arc.",
            quickQuizQuestion = "When an interviewer clarifies an ambiguous edge case, what is the best practice?",
            quizOptions = listOf(
                "Immediately start coding without acknowledgement",
                "Repeat back your understanding and write a quick test case on the whiteboard",
                "Argue that edge cases don't happen in production",
                "Remain silent until they ask you to speak"
            ),
            correctQuizOptionIndex = 1,
            quizExplanation = "Paraphrasing and formalizing with test cases verifies alignment and shows structured problem solving."
        ),
        SoftSkillLesson(
            id = "soft_02",
            title = "Group Discussion (GD) Mastery",
            concept = "Placement GD rounds evaluate initiation, structured moderation, factual content, and constructive disagreement rather than aggressive shouting.",
            workplaceExample = "When the discussion is stalled in chaos, step in: 'Friends, we have heard great perspectives on costs; let us now examine consumer security.'",
            practicalActivity = "Try framing arguments using PESTLE (Political, Economic, Social, Technological, Legal, Environmental) framework.",
            quickQuizQuestion = "What is the most effective way to enter a fast-paced GD conversation?",
            quizOptions = listOf(
                "Raise your voice higher than everyone else",
                "Acknowledge the previous speaker briefly and transition with a new structured dimension",
                "Interrupt by banging the table",
                "Wait until the final 10 seconds and summarize"
            ),
            correctQuizOptionIndex = 1,
            quizExplanation = "Building on previous points showcases collaborative leadership without being disruptive."
        ),
        SoftSkillLesson(
            id = "soft_03",
            title = "Behavioral STAR Technique",
            concept = "Structure all behavioral responses around Situation, Task, Action, and Result to convey impact with measurable metrics.",
            workplaceExample = "Instead of 'I built a website', say: 'Our college fest faced server crashes (S); I was tasked with load handling (T); I added Redis caching and CDN assets (A); which reduced latency by 65% across 10,000 users (R).'",
            practicalActivity = "Draft 2 stories from college projects illustrating conflict resolution and tight deadline delivery.",
            quickQuizQuestion = "In the STAR methodology, which element demonstrates personal accountability?",
            quizOptions = listOf("Situation", "Task", "Action", "Result"),
            correctQuizOptionIndex = 2,
            quizExplanation = "The Action phase explicitly highlights the technical choices and individual ownership you undertook."
        )
    )

    val sampleMockTests: List<MockTest> = listOf(
        MockTest(
            id = "test_01",
            title = "Full Campus Placement Simulation 2026",
            category = QuestionCategory.APTITUDE,
            durationMinutes = 20,
            totalQuestions = 10,
            description = "Comprehensive standard test mirroring TCS, Infosys & Wipro placement drives. Evaluates Quantitative, Logical Reasoning, and Verbal ability.",
            questionIds = listOf("apt_01", "apt_02", "apt_03", "apt_04", "reas_01", "reas_02", "reas_03", "verb_01", "verb_02", "tech_01")
        ),
        MockTest(
            id = "test_02",
            title = "Aptitude Sprint: Speed & Accuracy",
            category = QuestionCategory.APTITUDE,
            durationMinutes = 10,
            totalQuestions = 5,
            description = "High-pressure timed sprint focusing on arithmetic, time-work, percentages, and number properties.",
            questionIds = listOf("apt_01", "apt_02", "apt_04", "apt_06", "apt_07")
        ),
        MockTest(
            id = "test_03",
            title = "Core CS Technical & Coding Foundation",
            category = QuestionCategory.TECHNICAL,
            durationMinutes = 15,
            totalQuestions = 5,
            description = "Targeted test on Operating Systems, Database Management Systems, Java memory model, and algorithmic complexity.",
            questionIds = listOf("prog_01", "prog_02", "prog_03", "tech_01", "tech_02")
        )
    )

    val sampleAchievements: List<Achievement> = listOf(
        Achievement(
            id = "ach_01",
            title = "First Test",
            description = "Completed your very first mock placement assessment",
            iconName = "School",
            isUnlocked = true,
            currentProgress = 1,
            maxProgress = 1,
            unlockedDate = "Sept 1, 2026"
        ),
        Achievement(
            id = "ach_02",
            title = "10 Tests Completed",
            description = "Consistently evaluated performance across 10 placement mock exams",
            iconName = "AssignmentTurnedIn",
            isUnlocked = false,
            currentProgress = 8,
            maxProgress = 10
        ),
        Achievement(
            id = "ach_03",
            title = "50 Questions Solved",
            description = "Solved 50 practice problems across aptitude, reasoning, and programming",
            iconName = "CheckCircle",
            isUnlocked = false,
            currentProgress = 34,
            maxProgress = 50
        ),
        Achievement(
            id = "ach_04",
            title = "100 Questions Solved",
            description = "Completed 100 questions to sharpen test-taking instincts",
            iconName = "MilitaryTech",
            isUnlocked = false,
            currentProgress = 34,
            maxProgress = 100
        ),
        Achievement(
            id = "ach_05",
            title = "Coding Beginner",
            description = "Successfully submitted your first algorithmic coding challenge",
            iconName = "Code",
            isUnlocked = true,
            currentProgress = 1,
            maxProgress = 1,
            unlockedDate = "Sept 3, 2026"
        ),
        Achievement(
            id = "ach_06",
            title = "Coding Streak",
            description = "Maintained a 5-day continuous coding practice streak",
            iconName = "ElectricBolt",
            isUnlocked = true,
            currentProgress = 5,
            maxProgress = 5,
            unlockedDate = "Today"
        ),
        Achievement(
            id = "ach_07",
            title = "Aptitude Master",
            description = "Attained 80%+ accuracy across Quantitative and Logical modules",
            iconName = "Psychology",
            isUnlocked = true,
            currentProgress = 82,
            maxProgress = 80,
            unlockedDate = "Yesterday"
        ),
        Achievement(
            id = "ach_08",
            title = "Interview Starter",
            description = "Completed an AI mock interview session with detailed feedback",
            iconName = "RecordVoiceOver",
            isUnlocked = true,
            currentProgress = 3,
            maxProgress = 1,
            unlockedDate = "Sept 2, 2026"
        ),
        Achievement(
            id = "ach_09",
            title = "7-Day Streak",
            description = "Engaged with daily study goals for 7 consecutive calendar days",
            iconName = "LocalFireDepartment",
            isUnlocked = false,
            currentProgress = 5,
            maxProgress = 7
        )
    )

    val sampleAdminStudents: List<StudentProfile> = listOf(
        StudentProfile(
            uid = "user_student_101",
            name = "Rahul Sharma",
            email = "rahul.sharma@engg.edu",
            college = "National Institute of Technology",
            department = "Computer Science & Engineering",
            year = "Final Year",
            targetRole = "Software Development Engineer (SDE)",
            testsCompleted = 8,
            problemsSolved = 34,
            interviewsCompleted = 3,
            currentStreak = 5
        ),
        StudentProfile(
            uid = "user_student_102",
            name = "Priya Patel",
            email = "priya.patel@techinst.edu",
            college = "Indian Institute of Information Technology",
            department = "Information Technology",
            year = "Final Year",
            targetRole = "Full Stack Engineer",
            testsCompleted = 12,
            problemsSolved = 56,
            interviewsCompleted = 5,
            currentStreak = 11
        ),
        StudentProfile(
            uid = "user_student_103",
            name = "Ananya Das",
            email = "ananya.das@govengg.ac.in",
            college = "Government College of Engineering",
            department = "Electronics & Communication",
            year = "Pre-Final Year",
            targetRole = "Data Analyst / Associate Software Engineer",
            testsCompleted = 6,
            problemsSolved = 22,
            interviewsCompleted = 2,
            currentStreak = 3
        ),
        StudentProfile(
            uid = "user_student_104",
            name = "Vikram Rao",
            email = "vikram.rao@vitu.edu",
            college = "Vellore Technological University",
            department = "Mechanical Engineering",
            year = "Final Year",
            targetRole = "Systems Engineer (TCS / Infosys)",
            testsCompleted = 15,
            problemsSolved = 48,
            interviewsCompleted = 4,
            currentStreak = 8
        )
    )
}
