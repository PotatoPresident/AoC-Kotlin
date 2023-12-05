package y23

import findDigits
import puzzle

/**
 * [AOC 2023 Day 1](https://adventofcode.com/2023/day/1)
 */
fun main() = puzzle(2023, 1) {
    val digits = inputLines.map { it.findDigits() } // Convert list of strings to list of ints with regex

    submit {
        digits.sumOf { "${it.first()}${it.last()}".toLong() } // Sum of first and last digits concatenated
    }

    val words = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )
    val searchValues =
        words.entries.flatMap { (name, num) -> listOf(name, num.toString()) } // List of all words and numbers

    submit {
        inputLines.sumOf { input ->
            val first = input.findAnyOf(searchValues)!!.let { (index, result) -> words[result] ?: result.toInt() } // Find the first occurrence of any word or number and convert word to int
            val last = input.findLastAnyOf(searchValues)!!.let { (index, result) -> words[result] ?: result.toInt() } // Find the last occurrence of any word or number and convert word to int

            "$first$last".toLong() // Sum of first and last digits concatenated
        }
    }
}
