package com.numero.storm.domain.calculator

/**
 * Comprehensive numerology interpretations for all numbers.
 * Each interpretation includes general meaning, strengths, challenges,
 * career suggestions, relationship insights, and spiritual guidance.
 */
object NumerologyInterpretations {

    /**
     * Life Path Number interpretations.
     */
    fun getLifePathInterpretation(number: Int): NumberInterpretation {
        return when (number) {
            1 -> NumberInterpretation(
                number = 1,
                title = "The Leader",
                generalMeaning = "Life Path 1 represents the path of the pioneer and innovator. You are here to develop independence, individuality, and self-confidence. Your journey involves learning to stand on your own, initiate action, and lead others through your example.",
                strengths = listOf(
                    "Natural leadership abilities",
                    "Strong determination and willpower",
                    "Original and creative thinking",
                    "Self-motivation and drive",
                    "Courage to take initiative"
                ),
                challenges = listOf(
                    "Tendency toward stubbornness",
                    "May struggle with cooperation",
                    "Risk of becoming domineering",
                    "Impatience with others",
                    "Fear of failure or dependence"
                ),
                careerPaths = listOf(
                    "Entrepreneur",
                    "Executive",
                    "Inventor",
                    "Director",
                    "Freelancer",
                    "Military leader"
                ),
                relationshipInsights = "In relationships, you need a partner who respects your independence while grounding your energy. Avoid controlling behavior and learn to compromise without losing your identity.",
                spiritualGuidance = "Your spiritual lesson is to balance assertiveness with humility. Learn that true leadership comes from serving others, not dominating them.",
                luckyColors = listOf("Red", "Gold", "Yellow"),
                luckyDays = listOf("Sunday", "Monday"),
                compatibleNumbers = listOf(1, 3, 5, 9)
            )

            2 -> NumberInterpretation(
                number = 2,
                title = "The Diplomat",
                generalMeaning = "Life Path 2 represents the path of cooperation and harmony. You are here to bring peace, balance, and partnership into the world. Your sensitivity and intuition make you an excellent mediator and supporter.",
                strengths = listOf(
                    "Deep intuition and sensitivity",
                    "Natural peacemaker abilities",
                    "Cooperative and diplomatic nature",
                    "Patient and understanding",
                    "Strong sense of fairness"
                ),
                challenges = listOf(
                    "Over-sensitivity to criticism",
                    "Tendency to be indecisive",
                    "May become overly dependent",
                    "Difficulty asserting yourself",
                    "Prone to mood swings"
                ),
                careerPaths = listOf(
                    "Counselor",
                    "Mediator",
                    "Diplomat",
                    "Social worker",
                    "Artist",
                    "Healthcare professional"
                ),
                relationshipInsights = "Partnership is essential to your happiness. You thrive in committed relationships where there is mutual respect and emotional support. Avoid losing yourself in your partner's needs.",
                spiritualGuidance = "Your spiritual path involves learning to trust your intuition while developing inner strength. Balance serving others with honoring your own needs.",
                luckyColors = listOf("White", "Cream", "Light green"),
                luckyDays = listOf("Monday", "Friday"),
                compatibleNumbers = listOf(2, 4, 6, 8)
            )

            3 -> NumberInterpretation(
                number = 3,
                title = "The Communicator",
                generalMeaning = "Life Path 3 represents the path of creative expression and joy. You are here to inspire others through your words, art, and infectious optimism. Communication in all forms is your gift to the world.",
                strengths = listOf(
                    "Exceptional communication skills",
                    "Natural creativity and artistry",
                    "Infectious enthusiasm",
                    "Social charm and charisma",
                    "Inspiring and uplifting presence"
                ),
                challenges = listOf(
                    "Tendency to scatter energy",
                    "May avoid emotional depth",
                    "Risk of superficiality",
                    "Difficulty with focus",
                    "Prone to gossip or exaggeration"
                ),
                careerPaths = listOf(
                    "Writer",
                    "Actor",
                    "Musician",
                    "Public speaker",
                    "Marketing professional",
                    "Designer"
                ),
                relationshipInsights = "You bring joy and laughter to relationships but may struggle with emotional depth. Find a partner who appreciates your creativity while helping you stay grounded.",
                spiritualGuidance = "Your spiritual journey involves learning that true creativity comes from emotional authenticity. Express not just your joy, but your full range of human experience.",
                luckyColors = listOf("Yellow", "Orange", "Pink"),
                luckyDays = listOf("Wednesday", "Thursday"),
                compatibleNumbers = listOf(1, 3, 5, 6, 9)
            )

            4 -> NumberInterpretation(
                number = 4,
                title = "The Builder",
                generalMeaning = "Life Path 4 represents the path of stability and foundation. You are here to create lasting structures, whether physical, organizational, or conceptual. Your reliability and dedication make you the backbone of any endeavor.",
                strengths = listOf(
                    "Exceptional organizational skills",
                    "Strong work ethic",
                    "Practical and reliable nature",
                    "Attention to detail",
                    "Patience and perseverance"
                ),
                challenges = listOf(
                    "Tendency toward rigidity",
                    "May resist change",
                    "Risk of workaholism",
                    "Difficulty with spontaneity",
                    "Prone to pessimism"
                ),
                careerPaths = listOf(
                    "Engineer",
                    "Accountant",
                    "Project manager",
                    "Architect",
                    "Surgeon",
                    "Systems analyst"
                ),
                relationshipInsights = "You offer stability and loyalty in relationships but may struggle with emotional expression. Find a partner who values security while helping you embrace flexibility.",
                spiritualGuidance = "Your spiritual lesson involves finding freedom within structure. Learn that true security comes from within, not from external circumstances.",
                luckyColors = listOf("Green", "Brown", "Blue"),
                luckyDays = listOf("Saturday", "Sunday"),
                compatibleNumbers = listOf(2, 4, 6, 7, 8)
            )

            5 -> NumberInterpretation(
                number = 5,
                title = "The Freedom Seeker",
                generalMeaning = "Life Path 5 represents the path of change and adventure. You are here to experience life in all its variety, embracing change as your greatest teacher. Your adaptability and curiosity drive you forward.",
                strengths = listOf(
                    "Exceptional adaptability",
                    "Natural curiosity",
                    "Charismatic and dynamic presence",
                    "Quick thinking",
                    "Resourcefulness"
                ),
                challenges = listOf(
                    "Difficulty with commitment",
                    "Tendency toward excess",
                    "May avoid responsibility",
                    "Restlessness and impatience",
                    "Risk of addiction"
                ),
                careerPaths = listOf(
                    "Travel writer",
                    "Sales representative",
                    "Journalist",
                    "Tour guide",
                    "Event planner",
                    "Consultant"
                ),
                relationshipInsights = "You need freedom within relationships. Find a partner who is independent enough to give you space while adventurous enough to join you on your journey.",
                spiritualGuidance = "Your spiritual path involves learning that true freedom is internal. External experiences are teachers, but wisdom comes from inner reflection.",
                luckyColors = listOf("Turquoise", "Light blue", "Silver"),
                luckyDays = listOf("Wednesday", "Friday"),
                compatibleNumbers = listOf(1, 3, 5, 7, 9)
            )

            6 -> NumberInterpretation(
                number = 6,
                title = "The Nurturer",
                generalMeaning = "Life Path 6 represents the path of love and responsibility. You are here to nurture, heal, and create harmony in your environment. Family, community, and service are central to your purpose.",
                strengths = listOf(
                    "Deep capacity for love",
                    "Natural healing abilities",
                    "Strong sense of responsibility",
                    "Artistic appreciation",
                    "Protective nature"
                ),
                challenges = listOf(
                    "Tendency toward perfectionism",
                    "May become controlling",
                    "Risk of self-sacrifice",
                    "Difficulty saying no",
                    "Prone to worry"
                ),
                careerPaths = listOf(
                    "Teacher",
                    "Nurse",
                    "Interior designer",
                    "Therapist",
                    "Chef",
                    "Veterinarian"
                ),
                relationshipInsights = "You are the romantic idealist, seeking a beautiful partnership. Be mindful of trying to fix or control your partner. Allow them their own journey while offering support.",
                spiritualGuidance = "Your spiritual lesson is to balance giving with receiving. Learn that caring for yourself enables you to better care for others.",
                luckyColors = listOf("Blue", "Pink", "Green"),
                luckyDays = listOf("Friday", "Thursday"),
                compatibleNumbers = listOf(2, 3, 4, 6, 9)
            )

            7 -> NumberInterpretation(
                number = 7,
                title = "The Seeker",
                generalMeaning = "Life Path 7 represents the path of wisdom and spiritual understanding. You are here to search for truth, both external and internal. Your analytical mind and intuitive nature combine to pierce through illusion.",
                strengths = listOf(
                    "Deep analytical abilities",
                    "Strong intuition",
                    "Natural wisdom seeker",
                    "Technical proficiency",
                    "Spiritual awareness"
                ),
                challenges = listOf(
                    "Tendency toward isolation",
                    "May become overly skeptical",
                    "Risk of emotional detachment",
                    "Difficulty trusting others",
                    "Prone to escapism"
                ),
                careerPaths = listOf(
                    "Scientist",
                    "Philosopher",
                    "Researcher",
                    "Psychologist",
                    "Programmer",
                    "Spiritual teacher"
                ),
                relationshipInsights = "You need a partner who respects your need for solitude and intellectual depth. Avoid isolating yourself; vulnerability is strength, not weakness.",
                spiritualGuidance = "Your spiritual path is naturally inclined toward awakening. Balance intellectual understanding with experiential wisdom through meditation and contemplation.",
                luckyColors = listOf("Purple", "Violet", "Gray"),
                luckyDays = listOf("Monday", "Saturday"),
                compatibleNumbers = listOf(4, 5, 7, 9)
            )

            8 -> NumberInterpretation(
                number = 8,
                title = "The Powerhouse",
                generalMeaning = "Life Path 8 represents the path of material mastery and authority. You are here to learn the responsible use of power, money, and influence. Your potential for worldly success is immense.",
                strengths = listOf(
                    "Exceptional business acumen",
                    "Natural authority",
                    "Strong manifestation abilities",
                    "Organizational talent",
                    "Resilience and determination"
                ),
                challenges = listOf(
                    "Tendency toward materialism",
                    "May become ruthless",
                    "Risk of workaholic tendencies",
                    "Difficulty with emotional expression",
                    "Prone to power struggles"
                ),
                careerPaths = listOf(
                    "CEO",
                    "Financial advisor",
                    "Real estate developer",
                    "Lawyer",
                    "Surgeon",
                    "Banker"
                ),
                relationshipInsights = "You may struggle balancing work and relationships. Find a partner who is equally ambitious or secure enough to support your drive without feeling neglected.",
                spiritualGuidance = "Your spiritual lesson is to use material success as a tool for higher purpose. True power lies in empowering others, not in personal accumulation.",
                luckyColors = listOf("Black", "Dark blue", "Brown"),
                luckyDays = listOf("Saturday", "Thursday"),
                compatibleNumbers = listOf(2, 4, 6, 8)
            )

            9 -> NumberInterpretation(
                number = 9,
                title = "The Humanitarian",
                generalMeaning = "Life Path 9 represents the path of universal love and completion. You are here to serve humanity through compassion, wisdom, and selfless giving. Your life encompasses all the lessons of numbers 1-8.",
                strengths = listOf(
                    "Deep compassion for humanity",
                    "Broad-minded perspective",
                    "Natural charisma",
                    "Artistic talents",
                    "Wisdom and insight"
                ),
                challenges = listOf(
                    "Tendency toward martyrdom",
                    "May neglect personal needs",
                    "Risk of disillusionment",
                    "Difficulty with endings",
                    "Prone to scattered energy"
                ),
                careerPaths = listOf(
                    "Humanitarian worker",
                    "Artist",
                    "Philanthropist",
                    "Teacher",
                    "Healer",
                    "International diplomat"
                ),
                relationshipInsights = "You love deeply but may struggle with personal attachment. Find a partner who shares your humanitarian values while helping you stay grounded in personal relationships.",
                spiritualGuidance = "Your spiritual path involves learning to release attachment while remaining engaged with life. You are completing a major cycle of soul evolution.",
                luckyColors = listOf("Red", "Gold", "Crimson"),
                luckyDays = listOf("Tuesday", "Thursday"),
                compatibleNumbers = listOf(1, 3, 5, 6, 7, 9)
            )

            11 -> NumberInterpretation(
                number = 11,
                title = "The Intuitive Visionary",
                generalMeaning = "Master Number 11 represents the path of spiritual illumination. You carry the energy of the 2 amplified by spiritual insight. You are here to inspire others through your visionary abilities and heightened intuition.",
                strengths = listOf(
                    "Exceptional intuitive abilities",
                    "Spiritual awareness",
                    "Inspirational presence",
                    "Creative vision",
                    "Healing abilities"
                ),
                challenges = listOf(
                    "Nervous energy and anxiety",
                    "May feel misunderstood",
                    "Risk of impracticality",
                    "Sensitivity to environment",
                    "Self-doubt despite abilities"
                ),
                careerPaths = listOf(
                    "Spiritual teacher",
                    "Psychic or intuitive",
                    "Inspirational speaker",
                    "Artist",
                    "Counselor",
                    "Film director"
                ),
                relationshipInsights = "You need a partner who understands your spiritual nature and sensitivity. Ground your visionary energy through practical expressions of love.",
                spiritualGuidance = "You are a channel for higher wisdom. Learn to trust your intuition while staying grounded. Your visions are meant to be shared, not hidden.",
                luckyColors = listOf("White", "Silver", "Light blue"),
                luckyDays = listOf("Monday", "Sunday"),
                compatibleNumbers = listOf(2, 4, 6, 11, 22)
            )

            22 -> NumberInterpretation(
                number = 22,
                title = "The Master Builder",
                generalMeaning = "Master Number 22 represents the path of the master builder. You carry the energy of the 4 amplified by spiritual vision. You have the potential to manifest large-scale projects that benefit humanity.",
                strengths = listOf(
                    "Ability to manifest visions into reality",
                    "Exceptional organizational skills",
                    "Practical idealism",
                    "Leadership on a large scale",
                    "Disciplined focus"
                ),
                challenges = listOf(
                    "Overwhelming sense of responsibility",
                    "May feel crushed by potential",
                    "Risk of nervous exhaustion",
                    "Perfectionism to extreme",
                    "Difficulty with small talk"
                ),
                careerPaths = listOf(
                    "Architect of major projects",
                    "International business leader",
                    "Political leader",
                    "Philanthropist",
                    "Engineering visionary",
                    "Institution builder"
                ),
                relationshipInsights = "You need a partner who can match your vision or at least understand it. Don't let your grand projects overshadow personal connection.",
                spiritualGuidance = "You are here to build lasting structures that serve humanity. Balance your material creations with spiritual wisdom.",
                luckyColors = listOf("Coral", "White", "Gold"),
                luckyDays = listOf("Saturday", "Monday"),
                compatibleNumbers = listOf(4, 6, 8, 11, 22)
            )

            33 -> NumberInterpretation(
                number = 33,
                title = "The Master Teacher",
                generalMeaning = "Master Number 33 represents the path of the master teacher and healer. You carry the energy of the 6 amplified by spiritual mastery. You are here to uplift humanity through selfless love and service.",
                strengths = listOf(
                    "Profound healing abilities",
                    "Selfless devotion to others",
                    "Spiritual mastery",
                    "Ability to inspire masses",
                    "Compassion without limits"
                ),
                challenges = listOf(
                    "May sacrifice too much",
                    "Risk of martyrdom",
                    "Difficulty setting boundaries",
                    "Overwhelming sense of duty",
                    "May attract those who drain"
                ),
                careerPaths = listOf(
                    "Spiritual leader",
                    "Humanitarian organization founder",
                    "Healer",
                    "Teacher of teachers",
                    "Counselor",
                    "Philanthropist"
                ),
                relationshipInsights = "Your love is so expansive it can be challenging to contain in one relationship. Find a partner who supports your mission while ensuring you receive love too.",
                spiritualGuidance = "You embody the highest expression of love in action. Remember that self-care is not selfish - you must fill your own cup to pour into others.",
                luckyColors = listOf("Turquoise", "Green", "Pink"),
                luckyDays = listOf("Friday", "Thursday"),
                compatibleNumbers = listOf(6, 9, 11, 22, 33)
            )

            else -> getLifePathInterpretation(NumberReducer.reduceToSingleDigit(number))
        }
    }

    /**
     * Expression Number interpretations.
     */
    fun getExpressionInterpretation(number: Int): String {
        return when (NumberReducer.reduce(number)) {
            1 -> "Your Expression Number 1 reveals natural talents in leadership and pioneering. You are meant to express originality and independence. Your destiny involves creating new paths and inspiring others to follow their own unique vision."
            2 -> "Your Expression Number 2 shows talents in diplomacy and cooperation. You are destined to bring harmony and balance. Your natural abilities lie in supporting others, mediating conflicts, and creating peaceful environments."
            3 -> "Your Expression Number 3 indicates creative and communicative talents. You are meant to express yourself through art, writing, or speaking. Your destiny involves inspiring joy and optimism in others through your creative gifts."
            4 -> "Your Expression Number 4 reveals practical and organizational talents. You are destined to build lasting foundations. Your abilities lie in systematic thinking, detailed work, and creating stable structures."
            5 -> "Your Expression Number 5 shows talents in adaptability and communication. You are meant to experience and share life's variety. Your destiny involves promoting change, freedom, and progressive ideas."
            6 -> "Your Expression Number 6 indicates nurturing and artistic talents. You are destined to create harmony in home and community. Your natural abilities lie in healing, teaching, and caring for others."
            7 -> "Your Expression Number 7 reveals analytical and spiritual talents. You are meant to seek and share wisdom. Your destiny involves deep research, spiritual exploration, and teaching others what you discover."
            8 -> "Your Expression Number 8 shows executive and financial talents. You are destined for material achievement and authority. Your abilities lie in organization, business, and manifesting abundance."
            9 -> "Your Expression Number 9 indicates humanitarian and artistic talents. You are meant to serve humanity broadly. Your destiny involves compassion, wisdom, and contributing to the greater good."
            11 -> "Your Expression Number 11 reveals visionary and intuitive talents. You are destined to inspire and illuminate. Your abilities lie in spiritual insight, artistic vision, and uplifting others."
            22 -> "Your Expression Number 22 shows master building talents. You are destined to create lasting institutions. Your abilities lie in manifesting grand visions into practical reality."
            33 -> "Your Expression Number 33 indicates master teaching talents. You are destined to heal and uplift humanity. Your abilities lie in selfless service, healing, and spiritual guidance."
            else -> getExpressionInterpretation(NumberReducer.reduceToSingleDigit(number))
        }
    }

    /**
     * Soul Urge Number interpretations.
     */
    fun getSoulUrgeInterpretation(number: Int): String {
        return when (NumberReducer.reduce(number)) {
            1 -> "Your Soul Urge 1 reveals an inner desire for independence and achievement. Deep down, you crave being first, original, and recognized for your individual accomplishments."
            2 -> "Your Soul Urge 2 shows an inner desire for love and partnership. At your core, you crave harmony, deep connections, and working cooperatively with others."
            3 -> "Your Soul Urge 3 reveals an inner desire for creative expression and joy. Deep down, you crave artistic fulfillment, social recognition, and the freedom to express yourself."
            4 -> "Your Soul Urge 4 shows an inner desire for security and order. At your core, you crave stability, a solid foundation, and the satisfaction of hard work paying off."
            5 -> "Your Soul Urge 5 reveals an inner desire for freedom and adventure. Deep down, you crave variety, new experiences, and the liberty to explore life without restrictions."
            6 -> "Your Soul Urge 6 shows an inner desire for love and family. At your core, you crave a harmonious home, meaningful relationships, and the opportunity to nurture others."
            7 -> "Your Soul Urge 7 reveals an inner desire for knowledge and understanding. Deep down, you crave wisdom, spiritual connection, and time alone to reflect and analyze."
            8 -> "Your Soul Urge 8 shows an inner desire for success and recognition. At your core, you crave material achievement, authority, and the respect that comes with accomplishment."
            9 -> "Your Soul Urge 9 reveals an inner desire to make a difference. Deep down, you crave contributing to humanity, universal love, and leaving a meaningful legacy."
            11 -> "Your Soul Urge 11 shows an inner desire for spiritual illumination. At your core, you crave inspiring others, receiving spiritual insights, and living a purpose-driven life."
            22 -> "Your Soul Urge 22 reveals an inner desire to build something lasting. Deep down, you crave manifesting great visions that benefit humanity on a large scale."
            33 -> "Your Soul Urge 33 shows an inner desire for universal healing. At your core, you crave uplifting humanity through selfless love and spiritual guidance."
            else -> getSoulUrgeInterpretation(NumberReducer.reduceToSingleDigit(number))
        }
    }

    /**
     * Personality Number interpretations.
     */
    fun getPersonalityInterpretation(number: Int): String {
        return when (NumberReducer.reduce(number)) {
            1 -> "Others see you as confident, independent, and a natural leader. You project an image of strength and self-reliance that others find inspiring."
            2 -> "Others see you as cooperative, diplomatic, and supportive. You project an image of gentleness and approachability that puts people at ease."
            3 -> "Others see you as creative, charming, and optimistic. You project an image of joy and enthusiasm that attracts social attention."
            4 -> "Others see you as reliable, practical, and hardworking. You project an image of stability and trustworthiness that people depend on."
            5 -> "Others see you as dynamic, adventurous, and versatile. You project an image of freedom and excitement that intrigues those around you."
            6 -> "Others see you as nurturing, responsible, and artistic. You project an image of warmth and domesticity that makes people feel cared for."
            7 -> "Others see you as intelligent, reserved, and mysterious. You project an image of wisdom and depth that commands respect."
            8 -> "Others see you as powerful, successful, and authoritative. You project an image of capability and ambition that impresses others."
            9 -> "Others see you as compassionate, sophisticated, and worldly. You project an image of wisdom and humanitarianism that inspires trust."
            11 -> "Others see you as inspirational, intuitive, and visionary. You project an image of spiritual depth that fascinates and uplifts."
            22 -> "Others see you as capable, disciplined, and visionary. You project an image of someone who can achieve great things."
            33 -> "Others see you as loving, healing, and selflessly devoted. You project an image of spiritual mastery and unconditional love."
            else -> getPersonalityInterpretation(NumberReducer.reduceToSingleDigit(number))
        }
    }

    /**
     * Birthday Number interpretations.
     */
    fun getBirthdayInterpretation(day: Int): String {
        return when (day) {
            1 -> "Born on the 1st, you have natural leadership abilities and a strong independent streak. You are a pioneer at heart."
            2 -> "Born on the 2nd, you are naturally cooperative and diplomatic. Partnership and harmony are important to you."
            3 -> "Born on the 3rd, you have natural creative and communicative abilities. Self-expression is your gift."
            4 -> "Born on the 4th, you have strong organizational abilities and a practical nature. You build solid foundations."
            5 -> "Born on the 5th, you have natural versatility and love of freedom. Change energizes you."
            6 -> "Born on the 6th, you have strong nurturing instincts and artistic appreciation. Home and family matter deeply."
            7 -> "Born on the 7th, you have natural analytical abilities and spiritual inclinations. You seek deeper truth."
            8 -> "Born on the 8th, you have strong business instincts and executive abilities. Material success is achievable."
            9 -> "Born on the 9th, you have natural humanitarian instincts and broad vision. You think globally."
            10 -> "Born on the 10th, you are a natural leader with creative flair. Independence with cooperation is your path."
            11 -> "Born on the 11th, you have heightened intuition and inspirational abilities. You are meant to illuminate."
            12 -> "Born on the 12th, you combine creativity with practicality. Expression through structured channels is your gift."
            13 -> "Born on the 13th, you have the ability to transform through discipline. Hard work leads to creative breakthroughs."
            14 -> "Born on the 14th, you seek freedom through change. Balance adventure with responsibility for best results."
            15 -> "Born on the 15th, you have magnetic charm and domestic talents. Creating beauty in relationships is natural."
            16 -> "Born on the 16th, you seek spiritual depth through introspection. Inner wisdom comes through life challenges."
            17 -> "Born on the 17th, you combine spiritual insight with business acumen. Material success serves higher purpose."
            18 -> "Born on the 18th, you have leadership abilities with humanitarian vision. Power should serve the greater good."
            19 -> "Born on the 19th, you are learning independence through experience. Self-reliance is your ultimate lesson."
            20 -> "Born on the 20th, you have heightened sensitivity and partnership abilities. Cooperation is your strength."
            21 -> "Born on the 21st, you have strong creative and social abilities. Self-expression brings success."
            22 -> "Born on the 22nd, you have master builder potential. Large-scale achievements are possible with discipline."
            23 -> "Born on the 23rd, you have versatile communication skills. Freedom of expression is essential to happiness."
            24 -> "Born on the 24th, you have strong family orientation and practical skills. Creating harmony is natural."
            25 -> "Born on the 25th, you have intuitive and analytical abilities combined. Wisdom comes through reflection."
            26 -> "Born on the 26th, you have business abilities with family focus. Material success supports loved ones."
            27 -> "Born on the 27th, you have compassion combined with analytical skills. Healing through understanding is your gift."
            28 -> "Born on the 28th, you have leadership with humanitarian vision. Independence serves the greater good."
            29 -> "Born on the 29th, you have heightened intuition and sensitivity. Spiritual service is your calling."
            30 -> "Born on the 30th, you have creative and expressive abilities. Artistic expression brings fulfillment."
            31 -> "Born on the 31st, you have practical creativity. Building through artistic or technical means is your path."
            else -> getBirthdayInterpretation(NumberReducer.reduceToSingleDigit(day))
        }
    }

    /**
     * Personal Year interpretations.
     */
    fun getPersonalYearInterpretation(number: Int): String {
        return when (number) {
            1 -> "A Personal Year 1 marks new beginnings. Plant seeds for the future, start new projects, and assert your independence. This year sets the tone for the next nine-year cycle."
            2 -> "A Personal Year 2 focuses on patience and partnerships. This is a year to nurture what you started, develop relationships, and exercise diplomacy. Avoid forcing results."
            3 -> "A Personal Year 3 brings creativity and self-expression to the forefront. Social opportunities increase, and creative projects flourish. Enjoy life but avoid scattering energy."
            4 -> "A Personal Year 4 requires hard work and foundation building. Focus on practical matters, organization, and long-term planning. Discipline brings rewards."
            5 -> "A Personal Year 5 brings change and adventure. Expect the unexpected, embrace new opportunities, and allow flexibility. Avoid impulsive decisions."
            6 -> "A Personal Year 6 centers on home, family, and responsibility. Relationships deepen, domestic matters require attention, and service to others is highlighted."
            7 -> "A Personal Year 7 calls for reflection and inner development. Step back from worldly concerns, pursue knowledge, and focus on spiritual growth. Trust your intuition."
            8 -> "A Personal Year 8 brings material opportunities and karmic balance. Business matters flourish, financial decisions are important, and past efforts bear fruit."
            9 -> "A Personal Year 9 marks endings and completions. Release what no longer serves you, tie up loose ends, and prepare for new beginnings. Practice forgiveness."
            else -> getPersonalYearInterpretation(NumberReducer.reduceToSingleDigit(number))
        }
    }

    /**
     * Karmic Debt Number interpretations.
     */
    fun getKarmicDebtInterpretation(number: Int): String {
        return when (number) {
            13 -> "Karmic Debt 13 indicates past life laziness or taking shortcuts. This life requires dedicated hard work and persistence. Success comes through honest effort, not luck or manipulation. Avoid the temptation to give up when progress seems slow."
            14 -> "Karmic Debt 14 indicates past life abuse of freedom or excessive indulgence. This life requires learning moderation and commitment. Avoid addiction, restlessness, and irresponsibility. True freedom comes through self-discipline."
            16 -> "Karmic Debt 16 indicates past life ego and relationship issues. This life involves ego death and rebuilding from humility. Unexpected upheavals destroy false pride. Embrace these as opportunities for spiritual growth and authentic connection."
            19 -> "Karmic Debt 19 indicates past life abuse of power or selfish behavior. This life requires learning to help others while maintaining healthy boundaries. Balance independence with cooperation. True leadership serves others."
            else -> "This number does not carry traditional karmic debt significance."
        }
    }
}

/**
 * Complete interpretation for a numerology number.
 */
data class NumberInterpretation(
    val number: Int,
    val title: String,
    val generalMeaning: String,
    val strengths: List<String>,
    val challenges: List<String>,
    val careerPaths: List<String>,
    val relationshipInsights: String,
    val spiritualGuidance: String,
    val luckyColors: List<String>,
    val luckyDays: List<String>,
    val compatibleNumbers: List<Int>
)
