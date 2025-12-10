package com.numero.storm.domain.calculator

/**
 * Handles the reduction of numbers to single digits or master numbers.
 * This is a core component used throughout all numerology calculations.
 */
object NumberReducer {

    /**
     * Reduces a number to a single digit (1-9) or keeps it as a master number (11, 22, 33).
     * This is the standard numerology reduction method.
     *
     * @param number The number to reduce
     * @param preserveMasterNumbers Whether to preserve master numbers (11, 22, 33)
     * @return The reduced number
     */
    fun reduce(number: Int, preserveMasterNumbers: Boolean = true): Int {
        var current = kotlin.math.abs(number)

        while (current > 9) {
            if (preserveMasterNumbers && current in NumerologyConstants.MASTER_NUMBERS) {
                return current
            }
            current = sumDigits(current)
        }

        return current
    }

    /**
     * Reduces a number without preserving master numbers.
     * Used in specific calculations where master numbers should be reduced.
     *
     * @param number The number to reduce
     * @return The single digit result (1-9)
     */
    fun reduceToSingleDigit(number: Int): Int {
        return reduce(number, preserveMasterNumbers = false)
    }

    /**
     * Sums the digits of a number.
     *
     * @param number The number whose digits to sum
     * @return The sum of all digits
     */
    fun sumDigits(number: Int): Int {
        var sum = 0
        var n = kotlin.math.abs(number)

        while (n > 0) {
            sum += n % 10
            n /= 10
        }

        return sum
    }

    /**
     * Gets the intermediate reduction steps for a number.
     * Useful for showing the calculation process to users.
     *
     * @param number The number to reduce
     * @param preserveMasterNumbers Whether to preserve master numbers
     * @return List of intermediate values during reduction
     */
    fun getReductionSteps(number: Int, preserveMasterNumbers: Boolean = true): List<Int> {
        val steps = mutableListOf<Int>()
        var current = kotlin.math.abs(number)

        steps.add(current)

        while (current > 9) {
            if (preserveMasterNumbers && current in NumerologyConstants.MASTER_NUMBERS) {
                break
            }
            current = sumDigits(current)
            steps.add(current)
        }

        return steps
    }

    /**
     * Checks if a number is a master number.
     *
     * @param number The number to check
     * @return True if the number is a master number (11, 22, or 33)
     */
    fun isMasterNumber(number: Int): Boolean {
        return number in NumerologyConstants.MASTER_NUMBERS
    }

    /**
     * Checks if a number contains a karmic debt.
     *
     * @param number The number to check
     * @return True if the number is a karmic debt number (13, 14, 16, or 19)
     */
    fun isKarmicDebtNumber(number: Int): Boolean {
        return number in NumerologyConstants.KARMIC_DEBT_NUMBERS
    }

    /**
     * Gets the karmic debt number if the final reduced number came from a karmic debt.
     * For example, 13 reduces to 4, but we want to know it came from 13.
     *
     * @param originalSum The original sum before final reduction
     * @return The karmic debt number if present, null otherwise
     */
    fun getKarmicDebt(originalSum: Int): Int? {
        val steps = getReductionSteps(originalSum, preserveMasterNumbers = false)
        return steps.firstOrNull { it in NumerologyConstants.KARMIC_DEBT_NUMBERS }
    }

    /**
     * Reduces a string of digits by summing each digit.
     *
     * @param digits String containing only numeric characters
     * @param preserveMasterNumbers Whether to preserve master numbers
     * @return The reduced number
     */
    fun reduceDigitString(digits: String, preserveMasterNumbers: Boolean = true): Int {
        val sum = digits.filter { it.isDigit() }.sumOf { it.digitToInt() }
        return reduce(sum, preserveMasterNumbers)
    }
}
