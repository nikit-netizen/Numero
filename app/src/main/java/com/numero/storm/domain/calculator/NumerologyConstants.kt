package com.numero.storm.domain.calculator

/**
 * Core numerology constants and mappings for various calculation systems.
 * These mappings are based on established numerological traditions and provide
 * the foundation for all numerology calculations in the application.
 */
object NumerologyConstants {

    /**
     * Pythagorean Numerology System (Western)
     * Most widely used system where each letter is assigned a number 1-9
     * based on its position in the alphabet.
     */
    val PYTHAGOREAN_VALUES: Map<Char, Int> = mapOf(
        'A' to 1, 'B' to 2, 'C' to 3, 'D' to 4, 'E' to 5, 'F' to 6, 'G' to 7, 'H' to 8, 'I' to 9,
        'J' to 1, 'K' to 2, 'L' to 3, 'M' to 4, 'N' to 5, 'O' to 6, 'P' to 7, 'Q' to 8, 'R' to 9,
        'S' to 1, 'T' to 2, 'U' to 3, 'V' to 4, 'W' to 5, 'X' to 6, 'Y' to 7, 'Z' to 8
    )

    /**
     * Chaldean Numerology System (Ancient Babylonian)
     * Older system with different letter-number assignments.
     * Numbers 1-8 are used (9 is considered sacred and not assigned to letters).
     */
    val CHALDEAN_VALUES: Map<Char, Int> = mapOf(
        'A' to 1, 'B' to 2, 'C' to 3, 'D' to 4, 'E' to 5, 'F' to 8, 'G' to 3, 'H' to 5, 'I' to 1,
        'J' to 1, 'K' to 2, 'L' to 3, 'M' to 4, 'N' to 5, 'O' to 7, 'P' to 8, 'Q' to 1, 'R' to 2,
        'S' to 3, 'T' to 4, 'U' to 6, 'V' to 6, 'W' to 6, 'X' to 5, 'Y' to 1, 'Z' to 7
    )

    /**
     * Vowels for Soul Urge Number calculation.
     * Y is treated as a vowel when it functions as one in a name.
     */
    val VOWELS: Set<Char> = setOf('A', 'E', 'I', 'O', 'U')

    /**
     * Consonants for Personality Number calculation.
     */
    val CONSONANTS: Set<Char> = ('A'..'Z').toSet() - VOWELS

    /**
     * Master Numbers in numerology.
     * These numbers are not reduced further as they carry special significance.
     */
    val MASTER_NUMBERS: Set<Int> = setOf(11, 22, 33)

    /**
     * Karmic Debt Numbers.
     * Numbers that indicate karmic lessons from past lives.
     */
    val KARMIC_DEBT_NUMBERS: Set<Int> = setOf(13, 14, 16, 19)

    /**
     * Karmic Lesson Numbers.
     * Numbers missing from a name indicate karmic lessons.
     */
    val ALL_SINGLE_DIGITS: Set<Int> = (1..9).toSet()

    /**
     * Personal Year Cycle length.
     */
    const val PERSONAL_YEAR_CYCLE: Int = 9

    /**
     * Pinnacle cycle divisions based on Life Path Number.
     * First Pinnacle: Birth to 36 minus Life Path Number
     * Second Pinnacle: Next 9 years
     * Third Pinnacle: Next 9 years
     * Fourth Pinnacle: Remainder of life
     */
    const val FIRST_PINNACLE_BASE_AGE: Int = 36
    const val PINNACLE_CYCLE_LENGTH: Int = 9

    /**
     * Challenge cycle divisions (same timing as pinnacles).
     */
    const val CHALLENGE_CYCLE_LENGTH: Int = 9

    /**
     * Life Path Period divisions.
     * Based on the reduction of birth month, day, and year.
     */
    const val FIRST_PERIOD_END_AGE_BASE: Int = 28
    const val PERIOD_LENGTH: Int = 27

    /**
     * Nepali Devnagari numerals mapping.
     */
    val NEPALI_NUMERALS: Map<Int, Char> = mapOf(
        0 to '०', 1 to '१', 2 to '२', 3 to '३', 4 to '४',
        5 to '५', 6 to '६', 7 to '७', 8 to '८', 9 to '९'
    )

    /**
     * Nepali Devnagari to Arabic numeral mapping.
     */
    val NEPALI_TO_ARABIC: Map<Char, Int> = mapOf(
        '०' to 0, '१' to 1, '२' to 2, '३' to 3, '४' to 4,
        '५' to 5, '६' to 6, '७' to 7, '८' to 8, '९' to 9
    )

    /**
     * Nepali vowels (Devnagari script) for name calculations.
     */
    val NEPALI_VOWELS: Set<Char> = setOf(
        'अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ए', 'ऐ', 'ओ', 'औ',
        'ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'े', 'ै', 'ो', 'ौ'
    )

    /**
     * Nepali consonants (Devnagari script base characters).
     */
    val NEPALI_CONSONANTS: Set<Char> = setOf(
        'क', 'ख', 'ग', 'घ', 'ङ',
        'च', 'छ', 'ज', 'झ', 'ञ',
        'ट', 'ठ', 'ड', 'ढ', 'ण',
        'त', 'थ', 'द', 'ध', 'न',
        'प', 'फ', 'ब', 'भ', 'म',
        'य', 'र', 'ल', 'व',
        'श', 'ष', 'स', 'ह'
    )

    /**
     * Nepali conjunct consonants (Devnagari script - multiple character sequences).
     * These are stored as strings since they consist of multiple Unicode characters.
     */
    val NEPALI_CONJUNCT_CONSONANTS: Set<String> = setOf(
        "क्ष", "त्र", "ज्ञ"
    )

    /**
     * Nepali Devnagari letter to number mapping (based on traditional Vedic numerology).
     */
    val NEPALI_LETTER_VALUES: Map<Char, Int> = buildMap {
        // क वर्ग (Ka varga) - 1
        put('क', 1); put('ख', 2); put('ग', 3); put('घ', 4); put('ङ', 5)
        // च वर्ग (Cha varga) - 6
        put('च', 6); put('छ', 7); put('ज', 8); put('झ', 9); put('ञ', 1)
        // ट वर्ग (Ta varga) - 2
        put('ट', 2); put('ठ', 3); put('ड', 4); put('ढ', 5); put('ण', 6)
        // त वर्ग (Ta varga) - 7
        put('त', 7); put('थ', 8); put('द', 9); put('ध', 1); put('न', 2)
        // प वर्ग (Pa varga) - 3
        put('प', 3); put('फ', 4); put('ब', 5); put('भ', 6); put('म', 7)
        // अन्तस्थ (Antastha) - 8
        put('य', 8); put('र', 9); put('ल', 1); put('व', 2)
        // ऊष्म (Ushma) - 3
        put('श', 3); put('ष', 4); put('स', 5); put('ह', 6)
        // Vowels
        put('अ', 1); put('आ', 2); put('इ', 3); put('ई', 4); put('उ', 5)
        put('ऊ', 6); put('ऋ', 7); put('ए', 8); put('ऐ', 9); put('ओ', 1)
        put('औ', 2)
        // Matras (vowel marks)
        put('ा', 2); put('ि', 3); put('ी', 4); put('ु', 5); put('ू', 6)
        put('ृ', 7); put('े', 8); put('ै', 9); put('ो', 1); put('ौ', 2)
        put('ं', 3); put('ः', 4); put('ँ', 5)
    }
}
